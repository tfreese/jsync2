// Created: 22.10.2016
package de.freese.jsync2.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.freese.jsync2.filter.PathFilter;
import de.freese.jsync2.filter.PathFilterEndsWith;
import de.freese.jsync2.filter.PathFilterNoOp;
import de.freese.jsync2.generator.DefaultGenerator;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
class TestJSyncGenerator extends AbstractJSyncIoTest {
    private static final Path PATH_DEST = createDestPath(TestJSyncGenerator.class);
    private static final Path PATH_SOURCE = createSourcePath(TestJSyncGenerator.class);

    @AfterEach
    void afterEach() throws Exception {
        deletePaths(PATH_SOURCE, PATH_DEST);
    }

    @BeforeEach
    void beforeEach() throws Exception {
        createSourceStructure(PATH_SOURCE);
    }

    @Test
    void testFileAttributes() throws Exception {
        List<SyncItem> list = new ArrayList<>();
        new DefaultGenerator().generateItems(System.getProperty("user.dir"), false, PathFilterNoOp.INSTANCE, list::add);

        SyncItem syncItem = list.stream().filter(si -> si.getRelativePath().endsWith("pom.xml")).findFirst().get();

        assertNotNull(syncItem);
        assertTrue(syncItem.getLastModifiedTime() > 0);
        assertTrue(syncItem.getSize() > 0);
    }

    @Test
    void testFilter() throws Exception {
        PathFilter filter = new PathFilterEndsWith(Set.of("src", "target", ".settings"), Set.of(".classpath", ".project"));
        List<SyncItem> list = new ArrayList<>();
        new DefaultGenerator().generateItems(System.getProperty("user.dir"), false, filter, list::add);

        Map<String, SyncItem> map = list.stream().collect(Collectors.toMap(SyncItem::getRelativePath, Function.identity()));

        assertFalse(map.isEmpty());

        assertTrue(map.keySet().stream().noneMatch(path -> path.endsWith(".classpath")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.endsWith(".project")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains("src/")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains("target/")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains(".settings")));
    }

    @Test
    void testGenerator() throws Exception {
        List<SyncItem> syncItems = new ArrayList<>();

        new DefaultGenerator().generateItems(PATH_SOURCE.toString(), false, PathFilterNoOp.INSTANCE, syncItems::add);

        assertEquals(7, syncItems.size());
    }
}
