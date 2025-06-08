// Created: 05.04.2018
package de.freese.jsync2.filesystem;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.Options;
import de.freese.jsync2.generator.DefaultGenerator;
import de.freese.jsync2.generator.Generator;

/**
 * Base-Implementation of a {@link FileSystem}.
 *
 * @author Thomas Freese
 */
public abstract class AbstractFileSystem implements FileSystem {

    private final Generator generator = new DefaultGenerator();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Charset getCharset() {
        return Options.CHARSET;
    }

    protected Generator getGenerator() {
        return generator;
    }

    protected Logger getLogger() {
        return logger;
    }
}
