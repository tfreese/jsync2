// Created: 06.04.2018
package de.freese.jsync2.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.freese.jsync2.utils.JSyncUtils;

/**
 * @author Thomas Freese
 */
abstract class AbstractJSyncIoTest {

    private static final Path PATH_TEST = Paths.get(System.getProperty("java.io.tmpdir"), "jsync");

    protected static Path createDestPath(final Class<? extends AbstractJSyncIoTest> testClass) {
        return PATH_TEST.resolve("dest").resolve(testClass.getSimpleName());
    }

    protected static Path createSourcePath(final Class<? extends AbstractJSyncIoTest> testClass) {
        return PATH_TEST.resolve("source").resolve(testClass.getSimpleName());
    }

    protected static void createSourceStructure(final Path pathSource) throws IOException {
        // Create Source-Files.
        Path path = pathSource;
        Path pathFile = path.resolve("file.txt");

        if (Files.notExists(pathFile)) {
            Files.createDirectories(path);

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(pathFile.toFile()))) {
                writer.print("file.txt");
                writer.flush();
            }
        }

        path = pathSource.resolve("v1");
        pathFile = path.resolve("file.txt");

        if (Files.notExists(pathFile)) {
            Files.createDirectories(path);

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(pathFile.toFile()))) {
                writer.print("file1.txt");
                writer.flush();
            }
        }

        pathFile = pathSource.resolve("largeFile.bin");

        if (Files.notExists(pathFile)) {
            try (RandomAccessFile raf = new RandomAccessFile(pathFile.toFile(), "rw")) {
                // 32 MB and some Bytes...
                raf.setLength((1024 * 1024 * 32) + 1024);
                raf.getFD().sync();
            }
        }

        // Create Target-Files.
        path = pathSource.resolve("v2");
        pathFile = path.resolve("file.txt");

        if (Files.notExists(pathFile)) {
            Files.createDirectories(path);

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(pathFile.toFile()))) {
                writer.print("file.txt");
                writer.flush();
            }
        }

        pathFile = path.resolve("file2.txt");

        if (Files.notExists(pathFile)) {
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(pathFile.toFile()))) {
                writer.print("file2.txt");
                writer.flush();
            }
        }
    }

    protected static void deletePaths(final Path pathSource, final Path pathDest) throws IOException {
        JSyncUtils.delete(pathSource, false);
        JSyncUtils.delete(pathDest, false);
    }
}
