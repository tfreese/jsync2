// Created: 06.08.2021
package de.freese.jsync2.swing;

import java.util.concurrent.ExecutorService;

import javax.swing.JFrame;

import de.freese.jsync2.utils.JSyncUtils;

/**
 * @author Thomas Freese
 */
public final class JSyncContext {
    private static ExecutorService executorService;
    private static JFrame mainFrame;
    private static Messages messages;

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static Messages getMessages() {
        return messages;
    }

    public static void shutdown() {
        JSyncUtils.shutdown(executorService, JSyncSwing.getLogger());

        executorService = null;
        messages = null;
        mainFrame = null;
    }

    static void setExecutorService(final ExecutorService executorService) {
        JSyncContext.executorService = executorService;
    }

    static void setMainFrame(final JFrame mainFrame) {
        JSyncContext.mainFrame = mainFrame;
    }

    static void setMessages(final Messages messages) {
        JSyncContext.messages = messages;
    }

    private JSyncContext() {
        super();
    }
}
