// Created: 23.10.2016
package de.freese.jsync2.console;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.Options;
import de.freese.jsync2.client.Client;
import de.freese.jsync2.client.DefaultClient;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.client.listener.ConsoleClientListener;
import de.freese.jsync2.console.arguments.ArgumentParser;
import de.freese.jsync2.console.arguments.ArgumentParserApacheCommonsCli;
import de.freese.jsync2.filesystem.EFileSystem;
import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.filter.PathFilterEndsWith;
import de.freese.jsync2.filter.PathFilterNoOp;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.model.SyncPair;

/**
 * @author Thomas Freese
 */
public final class JSyncConsole {
    public static final Logger LOGGER = LoggerFactory.getLogger(JSyncConsole.class);

    public static void main(final String[] args) throws Exception {
        String[] args2 = args;

        if (args2.length == 0) {
            String fileSrc = "file://" + System.getProperty("user.dir") + "/jsync2-console";
            String fileDst = "file://" + System.getProperty("java.io.tmpdir") + "/jsync2-console";

            // @formatter:off
            args2 = new String[]{
                    "--delete",
                    "--follow-symlinks",
                    "--checksum",
                    "--dry-run",
                    "-s",
                    fileSrc,
                    //"jsync://localhost:8001/home/tommy/git/jsync/jsync-console",
                    "-r",
                    //"jsync://localhost:8002/tmp/jsync/target"
                    fileDst
            };
            // @formatter:on
        }

        ArgumentParser argumentParser = null;

        try {
            argumentParser = new ArgumentParserApacheCommonsCli(args2);
            // argumentParser = new ArgumentParserJopt(args2);
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        if (!argumentParser.hasArgs()) {
            argumentParser.printHelp(System.out);

            System.exit(0);
        }

        run(argumentParser);
    }

    // private static void disableLogging()
    // {
    // // ch.qos.logback.classic.Logger Logger rootLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    // // rootLogger.setLevel(Level.OFF);
    // //
    // // LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    // // Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    // // rootLogger.setLevel(Level.INFO);
    // }

    public static void run(final ArgumentParser argumentParser) throws Exception {
        // @formatter:off
        Options options = new Options.Builder()
                .delete(argumentParser.delete())
                .followSymLinks(argumentParser.followSymlinks())
                .dryRun(argumentParser.dryRun())
                .checksum(argumentParser.checksum())
                .build()
                ;
        // @formatter:on

        URI senderUri = new URI(argumentParser.sender());
        URI receiverUri = new URI(argumentParser.receiver());

        System.out.println("Start synchronisation");
        syncDirectories(options, senderUri, receiverUri, new ConsoleClientListener());
        System.out.println("Synchronisation finished");
    }

    public static void syncDirectories(final Options options, final URI senderUri, final URI receiverUri, final ClientListener clientListener) {
        PathFilter pathFilter = new PathFilterEndsWith(Set.of("target", "build", ".settings", ".idea", ".gradle"), Set.of(".class", ".log"));

        Client client = new DefaultClient(options, senderUri, receiverUri);
        client.connectFileSystems();

        List<SyncItem> syncItemsSender = new ArrayList<>();

        client.generateSyncItems(EFileSystem.SENDER, pathFilter, syncItem -> {
            syncItemsSender.add(syncItem);

            String checksum = client.generateChecksum(EFileSystem.SENDER, syncItem, bytesRead -> {
                // System.out.println("Sender Bytes read: " + i);
            });
            syncItem.setChecksum(checksum);
        });

        List<SyncItem> syncItemsReceiver = new ArrayList<>();

        client.generateSyncItems(EFileSystem.RECEIVER, PathFilterNoOp.INSTANCE, syncItem -> {
            syncItemsReceiver.add(syncItem);

            String checksum = client.generateChecksum(EFileSystem.RECEIVER, syncItem, bytesRead -> {
                // System.out.println("Sender Bytes read: " + i);
            });
            syncItem.setChecksum(checksum);
        });

        List<SyncPair> syncPairs = new ArrayList<>();
        client.mergeSyncItems(syncItemsSender, syncItemsReceiver, syncPairs::add);

        syncPairs.forEach(SyncPair::validateStatus);

        client.syncReceiver(syncPairs, clientListener);

        client.disconnectFileSystems();
    }
}
