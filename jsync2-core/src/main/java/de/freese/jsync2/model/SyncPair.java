// Created: 22.10.2016
package de.freese.jsync2.model;

import java.util.Objects;

/**
 * Object for Informationen about Source- and Target-Destination.<br>
 *
 * @author Thomas Freese
 */
public class SyncPair {
    private final SyncItem receiverItem;
    private final SyncItem senderItem;

    private SyncStatus status = SyncStatus.UNKNOWN;

    /**
     * Only one of the {@link SyncItem}s can be null.
     *
     * @param senderItem {@link SyncItem}; if null only existing in Receiver
     * @param receiverItem {@link SyncItem}; if null only existing in Sender
     */
    public SyncPair(final SyncItem senderItem, final SyncItem receiverItem) {
        super();

        this.senderItem = senderItem;
        this.receiverItem = receiverItem;

        if (senderItem == null && receiverItem == null) {
            throw new IllegalArgumentException("only one SyncItem can be null");
        }
    }

    public SyncItem getReceiverItem() {
        return receiverItem;
    }

    public String getRelativePath() {
        return getSenderItem() != null ? getSenderItem().getRelativePath() : getReceiverItem().getRelativePath();
    }

    public SyncItem getSenderItem() {
        return senderItem;
    }

    public SyncStatus getStatus() {
        return status;
    }

    public boolean isFile() {
        return getSenderItem() != null ? getSenderItem().isFile() : getReceiverItem().isFile();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SyncPair [");
        builder.append("relativePath=").append(getRelativePath());
        builder.append(", status=").append(getStatus());
        builder.append("]");

        return builder.toString();
    }

    /**
     * Compares Source with Target.
     */
    public void validateStatus() {
        if (getSenderItem() == null && getReceiverItem() != null) {
            // Delete: only available in Target but not in Source.
            status = SyncStatus.ONLY_IN_TARGET;
        }
        else if (getSenderItem() != null && getReceiverItem() == null) {
            // Copy: only available in Source but not in Target.
            status = SyncStatus.ONLY_IN_SOURCE;
        }
        else if (getSenderItem() != null && getReceiverItem() != null) {
            // Copy: Different Attributes
            if (getSenderItem().getLastModifiedTime() != getReceiverItem().getLastModifiedTime()) {
                status = SyncStatus.DIFFERENT_LAST_MODIFIED_TIME;
            }
            else if (getSenderItem().getSize() != getReceiverItem().getSize()) {
                status = SyncStatus.DIFFERENT_SIZE;
            }
            else if (!Objects.equals(getSenderItem().getChecksum(), getReceiverItem().getChecksum())) {
                status = SyncStatus.DIFFERENT_CHECKSUM;
            }
            else {
                status = SyncStatus.SYNCHRONIZED;
            }
        }
        else {
            status = SyncStatus.UNKNOWN;
        }
    }
}
