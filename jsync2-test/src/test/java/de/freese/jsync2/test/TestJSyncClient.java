// Created: 18.07.2021
package de.freese.jsync2.test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.freese.jsync2.JSync;
import de.freese.jsync2.Options;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.client.listener.EmptyClientListener;
import de.freese.jsync2.filter.PathFilterNoOp;

/**
 * @author Thomas Freese
 */
class TestJSyncClient extends AbstractJSyncIoTest {
    private static final Path PATH_DEST = createDestPath(TestJSyncClient.class);
    private static final Path PATH_SOURCE = createSourcePath(TestJSyncClient.class);

    private static Options options;

    /**
     * @author Thomas Freese
     */
    private static final class TestClientListener extends EmptyClientListener {
        @Override
        public void error(final String message, final Throwable th) {
            assertNull(th);
        }
    }

    @BeforeAll
    static void beforeAll() {
        options = new Options.Builder().delete(true).checksum(true).followSymLinks(false).dryRun(false).build();
    }

    @AfterEach
    void afterEach() throws Exception {
        deletePaths(PATH_SOURCE, PATH_DEST);
    }

    @BeforeEach
    void beforeEach() throws Exception {
        createSourceStructure(PATH_SOURCE);
    }

    @Test
    void testLocalToLocal() {
        final URI senderUri = PATH_SOURCE.toUri();
        final URI receiverUri = PATH_DEST.toUri();

        syncDirectories(options, senderUri, receiverUri, new TestClientListener());

        assertTrue(true);
    }

    private void syncDirectories(final Options options, final URI senderUri, final URI receiverUri, final ClientListener clientListener) {
        JSync.syncDirectories(options, senderUri, receiverUri, clientListener, PathFilterNoOp.INSTANCE);
    }
}
