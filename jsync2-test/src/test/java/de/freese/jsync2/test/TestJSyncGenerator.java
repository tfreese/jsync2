// Created: 22.10.2016
package de.freese.jsync2.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Order;
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
    @Test
    @Order(40)
    void testFileAttributes() throws Exception {
        System.out.println();

        List<SyncItem> list = new ArrayList<>();
        new DefaultGenerator().generateItems(System.getProperty("user.dir"), false, PathFilterNoOp.INSTANCE, list::add);

        SyncItem syncItem = list.stream().filter(si -> si.getRelativePath().endsWith("pom.xml")).findFirst().get();

        assertNotNull(syncItem);
        assertTrue(syncItem.getLastModifiedTime() > 0);
        assertTrue(syncItem.getSize() > 0);
    }

    @Test
    @Order(30)
    void testFilter() throws Exception {
        System.out.println();

        PathFilter filter = new PathFilterEndsWith(Set.of("src", "target", ".settings"), Set.of(".classpath", ".project"));
        List<SyncItem> list = new ArrayList<>();
        new DefaultGenerator().generateItems(System.getProperty("user.dir"), false, filter, list::add);

        Map<String, SyncItem> map = list.stream().collect(Collectors.toMap(SyncItem::getRelativePath, Function.identity()));

        assertTrue(map.size() >= 1);

        map.keySet().stream().sorted().forEach(System.out::println);

        assertTrue(map.keySet().stream().noneMatch(path -> path.endsWith(".classpath")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.endsWith(".project")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains("src/")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains("target/")));
        assertTrue(map.keySet().stream().noneMatch(path -> path.contains(".settings")));
    }

    @Test
    @Order(10)
    void testGeneratorQuelle() throws Exception {
        System.out.println();

        Path base = PATH_QUELLE;
        System.out.printf("Quelle: %s%n", base);

        List<SyncItem> syncItems = new ArrayList<>();
        new DefaultGenerator().generateItems(base.toString(), false, PathFilterNoOp.INSTANCE, syncItems::add);

        System.out.printf("Anzahl SyncItems: %d%n", syncItems.size());

        assertEquals(4, syncItems.size());

        syncItems.forEach(syncItem -> System.out.printf("%s%n", syncItem));
    }

    @Test
    @Order(20)
    void testGeneratorZiel() throws Exception {
        System.out.println();

        Path base = PATH_ZIEL;
        System.out.printf("Ziel: %s%n", base);

        List<SyncItem> syncItems = new ArrayList<>();
        new DefaultGenerator().generateItems(base.toString(), false, PathFilterNoOp.INSTANCE, syncItems::add);

        System.out.printf("Anzahl SyncItems: %d%n", syncItems.size());

        assertEquals(3, syncItems.size());

        syncItems.forEach(syncItem -> System.out.printf("%s%n", syncItem));
    }
}
