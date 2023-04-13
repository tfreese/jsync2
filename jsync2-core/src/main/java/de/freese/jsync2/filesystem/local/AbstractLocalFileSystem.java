// Created: 05.04.2018
package de.freese.jsync2.filesystem.local;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import de.freese.jsync2.filesystem.AbstractFileSystem;
import de.freese.jsync2.filesystem.FileSystem;
import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.SyncItem;

/**
 * Base-Implementation of a {@link FileSystem} for Localhost-Filesystems.
 *
 * @author Thomas Freese
 */
public abstract class AbstractLocalFileSystem extends AbstractFileSystem {

    @Override
    public void connect(final URI uri) {
        // Empty
    }

    @Override
    public void disconnect() {
        // Empty
    }

    @Override
    public String generateChecksum(final String baseDir, final String relativeFile, final LongConsumer consumerChecksumBytesRead) {
        return getGenerator().generateChecksum(baseDir, relativeFile, consumerChecksumBytesRead);
    }

    @Override
    public void generateSyncItems(final String baseDir, final boolean followSymLinks, final PathFilter pathFilter, final Consumer<SyncItem> consumer) {
        getGenerator().generateItems(baseDir, followSymLinks, pathFilter, consumer);
    }
}
