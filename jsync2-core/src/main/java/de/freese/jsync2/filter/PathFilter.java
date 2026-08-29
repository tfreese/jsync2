package de.freese.jsync2.filter;

import java.nio.file.Path;
import java.util.Set;

/**
 * Exclude-Filter
 *
 * @author Thomas Freese
 * @since 15.08.2021
 */
public interface PathFilter {
    Set<String> directoryFilter();

    Set<String> fileFilter();

    boolean isExcludedDirectory(Path dir);

    boolean isExcludedFile(Path file);
}
