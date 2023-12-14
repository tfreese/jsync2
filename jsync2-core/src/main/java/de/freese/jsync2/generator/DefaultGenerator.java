// Created: 05.04.2018
package de.freese.jsync2.generator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.model.DefaultSyncItem;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.utils.DigestUtils;
import de.freese.jsync2.utils.JSyncUtils;
import de.freese.jsync2.utils.io.FileVisitorHierarchie;

/**
 * @author Thomas Freese
 */
public class DefaultGenerator implements Generator {

    @Override
    public String generateChecksum(final String baseDir, final String relativeFile, final LongConsumer consumerChecksumBytesRead) {
        final Path path = Paths.get(baseDir, relativeFile);

        return DigestUtils.sha256DigestAsHex(path, consumerChecksumBytesRead);
    }

    @Override
    public void generateItems(final String baseDir, final boolean followSymLinks, final PathFilter pathFilter, final Consumer<SyncItem> consumer) {
        final Path basePath = Paths.get(baseDir);

        final FileVisitOption[] visitOptions = JSyncUtils.getFileVisitOptions(followSymLinks);
        final LinkOption[] linkOptions = JSyncUtils.getLinkOptions(followSymLinks);

        final Consumer<Path> pathConsumer = path -> {
            final SyncItem syncItem;

            if (Files.isDirectory(path)) {
                syncItem = toDirectoryItem(path, basePath.relativize(path).toString(), linkOptions);
            }
            else {
                syncItem = toFileItem(path, basePath.relativize(path).toString(), linkOptions);
            }

            consumer.accept(syncItem);
        };

        try {
            Files.walkFileTree(basePath, Set.of(visitOptions), Integer.MAX_VALUE, new FileVisitorHierarchie(basePath, pathFilter, pathConsumer));
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * @param linkOptions {@link LinkOption}; if LinkOption#NOFOLLOW_LINKS null than Follow
     */
    protected SyncItem toDirectoryItem(final Path directory, final String relativeDir, final LinkOption[] linkOptions) {
        if (relativeDir.isEmpty()) {
            // relativeDir = directory
            return null;
        }

        final SyncItem syncItem = new DefaultSyncItem(relativeDir);

        try {
            try (Stream<Path> children = Files.list(directory)) {
                // @formatter:off
                final long count = children
                        .filter(child -> !child.equals(directory)) // We do not want the Base-Directory.
                        .count()
                        ;
                // @formatter:on

                syncItem.setSize(count);
            }

            final long lastModifiedTime = Files.getLastModifiedTime(directory, linkOptions).to(TimeUnit.SECONDS);
            syncItem.setLastModifiedTime(lastModifiedTime);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return syncItem;
    }

    /**
     * @param linkOptions {@link LinkOption}; if LinkOption#NOFOLLOW_LINKS null than Follow
     */
    protected SyncItem toFileItem(final Path file, final String relativeFile, final LinkOption[] linkOptions) {
        final SyncItem syncItem = new DefaultSyncItem(relativeFile, true);

        try {
            final BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class, linkOptions);
            syncItem.setLastModifiedTime(basicFileAttributes.lastModifiedTime().to(TimeUnit.SECONDS));
            syncItem.setSize(basicFileAttributes.size());
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return syncItem;
    }
}
