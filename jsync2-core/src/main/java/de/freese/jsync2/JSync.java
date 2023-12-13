// Created: 16.04.23
package de.freese.jsync2;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import de.freese.jsync2.client.Client;
import de.freese.jsync2.client.DefaultClient;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.filesystem.EFileSystem;
import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.filter.PathFilterNoOp;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.model.SyncPair;

/**
 * @author Thomas Freese
 */
public final class JSync {

    public static void syncDirectories(final Options options, final URI senderUri, final URI receiverUri, final ClientListener clientListener, final PathFilter pathFilter) {
        Client client = new DefaultClient(options, senderUri, receiverUri);
        client.connectFileSystems();

        List<SyncItem> syncItemsSender = new ArrayList<>();

        client.generateSyncItems(EFileSystem.SENDER, pathFilter, syncItem -> {
            syncItemsSender.add(syncItem);

            String checksum = client.generateChecksum(EFileSystem.SENDER, syncItem, bytesRead -> clientListener.checksumProgress(options, syncItem, bytesRead));
            syncItem.setChecksum(checksum);
        });

        List<SyncItem> syncItemsReceiver = new ArrayList<>();

        client.generateSyncItems(EFileSystem.RECEIVER, PathFilterNoOp.INSTANCE, syncItem -> {
            syncItemsReceiver.add(syncItem);

            String checksum = client.generateChecksum(EFileSystem.RECEIVER, syncItem, bytesRead -> clientListener.checksumProgress(options, syncItem, bytesRead));
            syncItem.setChecksum(checksum);
        });

        List<SyncPair> syncPairs = new ArrayList<>();
        client.mergeSyncItems(syncItemsSender, syncItemsReceiver, syncPairs::add);

        syncPairs.forEach(SyncPair::validateStatus);

        client.syncReceiver(syncPairs, clientListener);

        client.disconnectFileSystems();
    }

    private JSync() {
        super();
    }
}
