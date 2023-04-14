// Created: 18.07.2021
package de.freese.jsync2.test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jsync2.Options;
import de.freese.jsync2.client.listener.ClientListener;
import de.freese.jsync2.client.listener.EmptyClientListener;
import de.freese.jsync2.console.JSyncConsole;

/**
 * @author Thomas Freese
 */
class TestJSyncClient extends AbstractJSyncIoTest {
    /**
     * @author Thomas Freese
     */
    private static class TestClientListener extends EmptyClientListener {
        @Override
        public void error(final String message, final Throwable th) {
            assertNull(th);
        }
    }

    private static Options options;

    @BeforeAll
    static void beforeAll() throws Exception {
        options = new Options.Builder().delete(true).checksum(true).followSymLinks(false).dryRun(false).build();
    }

    @Test
    void testLocalToLocal() throws Exception {
        System.out.println();

        URI senderUri = PATH_QUELLE.toUri();
        URI receiverUri = PATH_ZIEL.toUri();

        syncDirectories(options, senderUri, receiverUri, new TestClientListener());

        assertTrue(true);
    }

    private void syncDirectories(final Options options, final URI senderUri, final URI receiverUri, final ClientListener clientListener) throws Exception {
        JSyncConsole.syncDirectories(options, senderUri, receiverUri, clientListener);
    }
}
