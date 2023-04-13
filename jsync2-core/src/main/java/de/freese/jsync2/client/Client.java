// Created: 05.04.2018
package de.freese.jsync2.client;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.filesystem.EFileSystem;
import de.freese.jsync2.filesystem.Receiver;
import de.freese.jsync2.filesystem.Sender;
import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.model.SyncPair;

/**
 * Coordinates {@link Sender} and {@link Receiver}.
 *
 * @author Thomas Freese
 */
public interface Client {
    void connectFileSystems();

    void disconnectFileSystems();

    String generateChecksum(EFileSystem fileSystem, SyncItem syncItem, LongConsumer consumerChecksumBytesRead);

    void generateSyncItems(EFileSystem fileSystem, PathFilter pathFilter, Consumer<SyncItem> consumer);

    /**
     * Merges the {@link SyncItem} from {@link Sender} and {@link Receiver}.<br>
     * The Entries of the Sender are the Reference.<br>
     * Is an Item not existing in the Receiver, it must be copied.<br>
     * Is an Item only in the Receiver, it must be deleted.<br>
     */
    void mergeSyncItems(List<SyncItem> syncItemsSender, List<SyncItem> syncItemsReceiver, Consumer<SyncPair> consumer);

    void syncReceiver(List<SyncPair> syncPairs, ClientListener clientListener);
}
