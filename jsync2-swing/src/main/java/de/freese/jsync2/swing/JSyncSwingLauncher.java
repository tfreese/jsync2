// Created: 31.07.2021
package de.freese.jsync2.swing;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

/**
 * @author Thomas Freese
 */
public final class JSyncSwingLauncher {
    static void main(final String[] args) {
        if (args == null) {
            JSyncSwing.getLogger().info("init");
        }
        else {
            if (JSyncSwing.getLogger().isInfoEnabled()) {
                JSyncSwing.getLogger().info("init: {}", Arrays.toString(args));
            }
        }

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> JSyncSwing.getLogger().error("Default exception handler", ex));

        JSyncContext.setExecutorService(Executors.newFixedThreadPool(8));
        JSyncContext.setMessages(new Messages(Locale.getDefault()));

        // To avoid Comparator Exceptions.
        // System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                new JSyncSwing().initGui(frame -> {
                    // frame.setSize(800, 600);
                    // frame.setSize(1024, 768);
                    // frame.setSize(1280, 768);
                    // frame.setSize(1280, 1024);
                    // frame.setSize(1680, 1050);
                    // frame.setSize(1920, 1080);
                    frame.setSize(1920, 768);
                    // frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    JSyncContext.setMainFrame(frame);
                });
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private JSyncSwingLauncher() {
        super();
    }
}
