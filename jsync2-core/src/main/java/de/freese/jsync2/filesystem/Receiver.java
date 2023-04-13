// Created: 28.04.2020
package de.freese.jsync2.filesystem;

import java.nio.channels.ReadableByteChannel;
import java.util.function.LongConsumer;

import de.freese.jsync2.model.SyncItem;

/**
 * Sink of the File.
 *
 * @author Thomas Freese
 */
public interface Receiver extends FileSystem {
    void createDirectory(String baseDir, String relativePath);

    void delete(String baseDir, String relativePath, boolean followSymLinks);

    void update(String baseDir, SyncItem syncItem);

    void validateFile(String baseDir, SyncItem syncItem, boolean withChecksum, LongConsumer consumerChecksumBytesRead);

    void writeFile(String baseDir, String relativeFile, long sizeOfFile, ReadableByteChannel readableByteChannel, LongConsumer consumerBytesWritten);
}
