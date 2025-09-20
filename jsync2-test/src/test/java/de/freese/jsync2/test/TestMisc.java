// Created: 16.04.23
package de.freese.jsync2.test;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

/**
 * @author Thomas Freese
 */
class TestMisc {
    // private static final Logger LOGGER = LoggerFactory.getLogger(TestMisc.class);

    static void main() {
        new TestMisc().testPrintSameLine();
    }

    void testPrintSameLine() {
        for (int i = 0; i <= 100; i++) {
            // LOGGER.info("{}", "[%-50s] %d %%".formatted("#".repeat(i / 2), i));
            System.out.printf("[%-50s] %d %%\r", "#".repeat(i / 2), i);

            // TimeUnit.MILLISECONDS.sleep(250L);
            await().pollDelay(250L, TimeUnit.MILLISECONDS).until(() -> true);
        }
    }
}
