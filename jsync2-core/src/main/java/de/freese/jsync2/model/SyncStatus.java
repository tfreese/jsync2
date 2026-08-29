package de.freese.jsync2.model;

/**
 * Differences between Source and Target.
 *
 * @author Thomas Freese
 * @since 22.10.2016
 */
public enum SyncStatus {
    DIFFERENT_CHECKSUM,
    DIFFERENT_LAST_MODIFIED_TIME,
    DIFFERENT_SIZE,
    ONLY_IN_SOURCE,
    ONLY_IN_TARGET,
    SYNCHRONIZED,
    UNKNOWN
}
