// Created: 05.04.2018
package de.freese.jsync2.filesystem.local;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;

import de.freese.jsync2.Options;
import de.freese.jsync2.filesystem.Receiver;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.utils.DigestUtils;
import de.freese.jsync2.utils.JSyncUtils;
import de.freese.jsync2.utils.io.ObserverableWritableByteChannel;

/**
 * {@link Receiver} for Localhost-Filesystems.
 *
 * @author Thomas Freese
 */
public class LocalhostReceiver extends AbstractLocalFileSystem implements Receiver {
    @Override
    public void createDirectory(final String baseDir, final String relativePath) {
        final Path path = Paths.get(baseDir, relativePath);

        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void delete(final String baseDir, final String relativePath, final boolean followSymLinks) {
        final Path path = Paths.get(baseDir, relativePath);

        try {
            JSyncUtils.delete(path, followSymLinks);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void update(final String baseDir, final SyncItem syncItem) {
        final Path path = Paths.get(baseDir, syncItem.getRelativePath());

        // TimeUnit = SECONDS
        final long lastModifiedTime = syncItem.getLastModifiedTime();

        try {
            Files.setLastModifiedTime(path, FileTime.from(lastModifiedTime, TimeUnit.SECONDS));
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void validateFile(final String baseDir, final SyncItem syncItem, final boolean withChecksum, final LongConsumer consumerChecksumBytesRead) {
        final Path path = Paths.get(baseDir, syncItem.getRelativePath());

        try {
            if (Files.size(path) != syncItem.getSize()) {
                final String message = String.format("fileSize does not match with source: %s/%s", baseDir, syncItem.getRelativePath());
                throw new IllegalStateException(message);
            }

            if (withChecksum) {
                getLogger().debug("building Checksum: {}/{}", baseDir, syncItem.getRelativePath());

                final String checksum = DigestUtils.sha256DigestAsHex(path, consumerChecksumBytesRead);

                if (!checksum.equals(syncItem.getChecksum())) {
                    final String message = String.format("checksum does not match with source: %s/%s", baseDir, syncItem.getRelativePath());
                    throw new IllegalStateException(message);
                }
            }
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void writeFile(final String baseDir, final String relativeFile, final long sizeOfFile, final ReadableByteChannel readableByteChannel, final LongConsumer consumerBytesWritten) {
        final Path path = Paths.get(baseDir, relativeFile);
        final Path parentPath = path.getParent();

        try {
            if (Files.notExists(parentPath)) {
                Files.createDirectories(parentPath);
            }

            //            if (Files.notExists(path)) {
            //                Files.createFile(path);
            //            }

            final ByteBuffer buffer = ByteBuffer.allocate(Options.BUFFER_SIZE);

            try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
                 WritableByteChannel writableByteChannel = new ObserverableWritableByteChannel(fileChannel, true).onBytesWritten(consumerBytesWritten)) {

                //                fileChannel.transferFrom(readableByteChannel, 0, sizeOfFile);
                while (readableByteChannel.read(buffer) != -1) {
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        writableByteChannel.write(buffer);
                    }

                    buffer.clear();
                }

                fileChannel.force(false);
            }
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
