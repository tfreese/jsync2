// Created: 15.08.2021
package de.freese.jsync2.filter;

import java.nio.file.Path;
import java.util.Set;

/**
 * Exclude-Filter
 *
 * @author Thomas Freese
 */
public interface PathFilter {
    Set<String> getDirectoryFilter();

    Set<String> getFileFilter();

    boolean isExcludedDirectory(Path dir);

    boolean isExcludedFile(Path file);
}
