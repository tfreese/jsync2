// Created: 30.10.2016
package de.freese.jsync2.model;

import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class DefaultSyncItem implements SyncItem {
    private final boolean isFile;
    private final String relativePath;
    private String checksum;
    private long lastModifiedTime;

    private long size;

    public DefaultSyncItem(final String relativePath) {
        this(relativePath, false);
    }

    public DefaultSyncItem(final String relativePath, final boolean isFile) {
        super();

        this.relativePath = Objects.requireNonNull(relativePath, "relativePath required");
        this.isFile = isFile;
    }

    @Override
    public String getChecksum() {
        return checksum;
    }

    @Override
    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isFile() {
        return isFile;
    }

    @Override
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    @Override
    public void setLastModifiedTime(final long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public void setSize(final long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SyncItem [");
        sb.append("relativePath=").append(getRelativePath());

        if (isFile()) {
            sb.append(", size=").append(getSize());
        }

        sb.append("]");

        return sb.toString();
    }
}
