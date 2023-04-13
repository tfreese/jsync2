// Created: 18.07.2021
package de.freese.jsync2.test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jsync2.Options;
import de.freese.jsync2.client.Client;
import de.freese.jsync2.client.DefaultClient;
import de.freese.jsync2.client.listener.EmptyClientListener;
import de.freese.jsync2.filesystem.EFileSystem;
import de.freese.jsync2.filter.PathFilterNoOp;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.model.SyncPair;

/**
 * @author Thomas Freese
 */
class TestJSyncClient extends AbstractJSyncIoTest {
    /**
     * @author Thomas Freese
     */
    private static class TestClientListener extends EmptyClientListener {
        @Override
        public void error(final String message, final Throwable th) {
            assertNull(th);
        }
    }

    private static Options options;

    @BeforeAll
    static void beforeAll() throws Exception {
        options = new Options.Builder().delete(true).checksum(true).followSymLinks(false).dryRun(false).build();
    }

    @Test
    void testLocalToLocal() throws Exception {
        System.out.println();

        URI senderUri = PATH_QUELLE.toUri();
        URI receiverUri = PATH_ZIEL.toUri();

        syncDirectories(options, senderUri, receiverUri);

        assertTrue(true);
    }

    private void syncDirectories(final Options options, final URI senderUri, final URI receiverUri) throws Exception {
        Client client = new DefaultClient(options, senderUri, receiverUri);
        client.connectFileSystems();

        List<SyncItem> syncItemsSender = new ArrayList<>();
        client.generateSyncItems(EFileSystem.SENDER, PathFilterNoOp.INSTANCE, syncItem -> {
            syncItemsSender.add(syncItem);
            String checksum = client.generateChecksum(EFileSystem.SENDER, syncItem, i -> {
                // System.out.println("Sender Bytes read: " + i);
            });
            syncItem.setChecksum(checksum);
        });

        List<SyncItem> syncItemsReceiver = new ArrayList<>();
        client.generateSyncItems(EFileSystem.RECEIVER, PathFilterNoOp.INSTANCE, syncItem -> {
            syncItemsReceiver.add(syncItem);
            String checksum = client.generateChecksum(EFileSystem.RECEIVER, syncItem, i -> {
                // System.out.println("Sender Bytes read: " + i);
            });
            syncItem.setChecksum(checksum);
        });

        List<SyncPair> syncPairs = new ArrayList<>();
        client.mergeSyncItems(syncItemsSender, syncItemsReceiver, syncPairs::add);

        syncPairs.forEach(SyncPair::validateStatus);

        client.syncReceiver(syncPairs, new TestClientListener());

        client.disconnectFileSystems();
    }
}
