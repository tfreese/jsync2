// Created: 05.04.2018
package de.freese.jsync2.client;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.freese.jsync2.Options;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.client.listener.EmptyClientListener;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.model.SyncPair;
import de.freese.jsync2.model.SyncStatus;

/**
 * @author Thomas Freese
 */
public class DefaultClient extends AbstractClient {
    public DefaultClient(final Options options, final URI senderUri, final URI receiverUri) {
        super(options, senderUri, receiverUri);
    }

    @Override
    public void mergeSyncItems(final List<SyncItem> syncItemsSender, final List<SyncItem> syncItemsReceiver, final Consumer<SyncPair> consumer) {
        // Map of ReceiverItems.
        Map<String, SyncItem> mapReceiver = syncItemsReceiver.stream().collect(Collectors.toMap(SyncItem::getRelativePath, Function.identity()));

        // @formatter:off
        syncItemsSender.stream()
                .map(senderItem -> new SyncPair(senderItem, mapReceiver.remove(senderItem.getRelativePath())))
                .forEach(consumer)
                ;
        // @formatter:on

        // What it is now in the Map must be deleted (source = null).
        mapReceiver.forEach((key, value) -> consumer.accept(new SyncPair(null, value)));
    }

    @Override
    public void syncReceiver(final List<SyncPair> syncPairs, final ClientListener clientListener) {
        ClientListener cl = clientListener != null ? clientListener : new EmptyClientListener();

        // Filter all items, which are synchronized.
        Predicate<SyncPair> isSynchronised = p -> SyncStatus.SYNCHRONIZED.equals(p.getStatus());
        List<SyncPair> sync = syncPairs.stream().filter(isSynchronised.negate()).toList();

        // Delete
        if (getOptions().isDelete()) {
            deleteFiles(sync, cl);
            deleteDirectories(sync, cl);
        }

        // Copy new or changed Files.
        copyFiles(sync, cl);

        // Update File-Attributes.
        updateFiles(sync, cl);

        // Create new and empty Directories.
        createDirectories(sync, clientListener);

        // Update Directory-Attributes.
        updateDirectories(sync, cl);
    }
}
