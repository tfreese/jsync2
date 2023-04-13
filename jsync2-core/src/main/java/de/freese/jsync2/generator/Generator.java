// Created: 22.10.2016
package de.freese.jsync2.generator;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.SyncItem;

/**
 * The Generator collects all relevant Information of the FileSystem for the chosen {@link Path}.
 *
 * @author Thomas Freese
 */
public interface Generator {
    /**
     * @param consumerChecksumBytesRead {@link LongConsumer}; optional
     */
    String generateChecksum(String baseDir, String relativeFile, LongConsumer consumerChecksumBytesRead);

    void generateItems(String baseDir, boolean followSymLinks, PathFilter pathFilter, Consumer<SyncItem> consumer);
}
