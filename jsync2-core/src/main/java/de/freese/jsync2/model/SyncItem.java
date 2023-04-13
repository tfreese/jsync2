// Created: 30.10.2016
package de.freese.jsync2.model;

/**
 * @author Thomas Freese
 */
public interface SyncItem {
    String getChecksum();

    long getLastModifiedTime();

    String getRelativePath();

    /**
     * Directory: Number of 1st-Level Children<br>
     * File: Size in Bytes
     */
    long getSize();

    //    User getUser();

    default boolean isDirectory() {
        return !isFile();
    }

    boolean isFile();

    void setChecksum(String checksum);

    void setLastModifiedTime(long lastModifiedTime);

    //    void setPermissions(Set<PosixFilePermission> permissions);

    /**
     * Directory: Number of 1st-Level Children<br>
     * File: Size in Bytes
     */
    void setSize(long size);
}
