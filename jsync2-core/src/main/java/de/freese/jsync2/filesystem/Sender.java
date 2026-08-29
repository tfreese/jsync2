package de.freese.jsync2.filesystem;

import java.nio.channels.ReadableByteChannel;

/**
 * Source of the File.
 *
 * @author Thomas Freese
 * @since 28.04.2020
 */
public interface Sender extends FileSystem {
    ReadableByteChannel readFile(String baseDir, String relativeFile, long sizeOfFile);
}
