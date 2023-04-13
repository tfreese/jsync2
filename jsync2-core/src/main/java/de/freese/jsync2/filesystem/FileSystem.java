// Created: 28.04.2020
package de.freese.jsync2.filesystem;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
public interface FileSystem {
    void connect(URI uri);

    void disconnect();

    String generateChecksum(String baseDir, String relativeFile, LongConsumer checksumBytesReadConsumer);

    void generateSyncItems(String baseDir, boolean followSymLinks, PathFilter pathFilter, Consumer<SyncItem> consumer);
}
