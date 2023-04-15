// Created: 15.04.23
package de.freese.jsync2.swing.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.swing.JSyncContext;

/**
 * @author Thomas Freese
 */
public abstract class AbstractView implements View {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Logger getLogger() {
        return this.logger;
    }

    protected JFrame getMainFrame() {
        return JSyncContext.getMainFrame();
    }

    protected String getMessage(final String key) {
        return JSyncContext.getMessages().getString(key);
    }

    protected void runInEdt(final Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        }
        else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}
