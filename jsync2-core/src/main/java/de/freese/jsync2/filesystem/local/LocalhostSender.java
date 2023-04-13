// Created: 05.04.2018
package de.freese.jsync2.filesystem.local;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import de.freese.jsync2.filesystem.Sender;

/**
 * {@link Sender} for Localhost-Filesystems.
 *
 * @author Thomas Freese
 */
public class LocalhostSender extends AbstractLocalFileSystem implements Sender {
    @Override
    public ReadableByteChannel readFile(final String baseDir, final String relativeFile, final long sizeOfFile) {
        Path path = Paths.get(baseDir, relativeFile);

        if (!Files.exists(path)) {
            String message = String.format("file doesn't exist: %s", path);
            getLogger().warn(message);

            return null;
        }

        try {
            return FileChannel.open(path, StandardOpenOption.READ);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
