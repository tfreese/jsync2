// Created: 07.08.2021
package de.freese.jsync2.filesystem;

import java.net.URI;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
public class ReceiverDelegateLogger implements Receiver {
    private final Receiver delegate;

    private final Logger logger;

    public ReceiverDelegateLogger(final Receiver delegate) {
        super();

        this.delegate = Objects.requireNonNull(delegate, "delegate required");

        if (this.delegate instanceof AbstractFileSystem fs) {
            this.logger = fs.getLogger();
        }
        else {
            this.logger = LoggerFactory.getLogger(this.delegate.getClass());
        }
    }

    @Override
    public void connect(final URI uri) {
        getLogger().info("connect to {}", uri);

        this.delegate.connect(uri);
    }

    @Override
    public void createDirectory(final String baseDir, final String relativePath) {
        getLogger().info("create: {}/{}", baseDir, relativePath);

        this.delegate.createDirectory(baseDir, relativePath);
    }

    @Override
    public void delete(final String baseDir, final String relativePath, final boolean followSymLinks) {
        getLogger().info("delete: {}/{}", baseDir, relativePath);

        this.delegate.delete(baseDir, relativePath, followSymLinks);
    }

    @Override
    public void disconnect() {
        getLogger().info("disconnect");

        this.delegate.disconnect();
    }

    @Override
    public String generateChecksum(final String baseDir, final String relativeFile, final LongConsumer consumerChecksumBytesRead) {
        getLogger().info("create checksum: {}/{}", baseDir, relativeFile);

        return this.delegate.generateChecksum(baseDir, relativeFile, consumerChecksumBytesRead);
    }

    @Override
    public void generateSyncItems(final String baseDir, final boolean followSymLinks, final PathFilter pathFilter, final Consumer<SyncItem> consumer) {
        getLogger().info("generate SyncItems: {}, followSymLinks={}", baseDir, followSymLinks);

        this.delegate.generateSyncItems(baseDir, followSymLinks, pathFilter, consumer);
    }

    @Override
    public void update(final String baseDir, final SyncItem syncItem) {
        getLogger().info("update: {}/{}", baseDir, syncItem.getRelativePath());

        this.delegate.update(baseDir, syncItem);
    }

    @Override
    public void validateFile(final String baseDir, final SyncItem syncItem, final boolean withChecksum, final LongConsumer consumerChecksumBytesRead) {
        getLogger().info("validate file: {}/{}, withChecksum={}", baseDir, syncItem.getRelativePath(), withChecksum);

        this.delegate.validateFile(baseDir, syncItem, withChecksum, consumerChecksumBytesRead);
    }

    @Override
    public void writeFile(final String baseDir, final String relativeFile, final long sizeOfFile, final ReadableByteChannel readableByteChannel,
                          final LongConsumer consumerBytesRead) {
        getLogger().info("write file: {}/{}, sizeOfFile={}", baseDir, relativeFile, sizeOfFile);

        this.delegate.writeFile(baseDir, relativeFile, sizeOfFile, readableByteChannel, consumerBytesRead);
    }

    protected Logger getLogger() {
        return this.logger;
    }
}
