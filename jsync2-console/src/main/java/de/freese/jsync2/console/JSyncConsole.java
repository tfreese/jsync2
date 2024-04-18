// Created: 23.10.2016
package de.freese.jsync2.console;

import java.net.URI;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.JSync;
import de.freese.jsync2.Options;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.client.listener.ConsoleClientListener;
import de.freese.jsync2.console.arguments.ArgumentParser;
import de.freese.jsync2.console.arguments.ArgumentParserApacheCommonsCli;
import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.filter.PathFilterEndsWith;

/**
 * @author Thomas Freese
 */
public final class JSyncConsole {
    public static final Logger LOGGER = LoggerFactory.getLogger(JSyncConsole.class);

    public static void main(final String[] args) throws Exception {
        String[] arguments = args;

        if (arguments.length == 0) {
            final String fileSrc = "file://" + System.getProperty("user.dir") + "/jsync2-console";
            final String fileDst = "file://" + System.getProperty("java.io.tmpdir") + "/jsync2-console";

            // final String fileSrc = "file:///mnt/mediathek/serien/Dexter/Staffel01";
            // final String fileDst = "file:///tmp/Dexter/Staffel01";

            arguments = new String[]{
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
        }

        ArgumentParser argumentParser = null;

        try {
            argumentParser = new ArgumentParserApacheCommonsCli(arguments);
            // argumentParser = new ArgumentParserJopt(arguments);
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

    // private static void disableLogging() {
    // // ch.qos.logback.classic.Logger Logger rootLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    // // rootLogger.setLevel(Level.OFF);
    // //
    // // LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    // // Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    // // rootLogger.setLevel(Level.INFO);
    // }

    public static void run(final ArgumentParser argumentParser) throws Exception {
        final Options options = new Options.Builder()
                .delete(argumentParser.delete())
                .followSymLinks(argumentParser.followSymlinks())
                .dryRun(argumentParser.dryRun())
                .checksum(argumentParser.checksum())
                .build();

        final URI senderUri = new URI(argumentParser.sender());
        final URI receiverUri = new URI(argumentParser.receiver());

        System.out.println("Start synchronisation");
        syncDirectories(options, senderUri, receiverUri, new ConsoleClientListener());
        System.out.println("Synchronisation finished");
    }

    public static void syncDirectories(final Options options, final URI senderUri, final URI receiverUri, final ClientListener clientListener) {
        final PathFilter pathFilter = new PathFilterEndsWith(Set.of("target", "build", ".settings", ".idea", ".gradle"), Set.of(".class", ".log"));

        JSync.syncDirectories(options, senderUri, receiverUri, clientListener, pathFilter);
    }

    private JSyncConsole() {
        super();
    }
}
