// Created: 15.04.23
package de.freese.jsync2.swing.controller;

import java.awt.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.swing.Messages;
import de.freese.jsync2.swing.view.View;

/**
 * @author Thomas Freese
 */
public abstract class AbstractController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final View view;

    protected AbstractController(final View view) {
        super();

        this.view = view;
    }

    @Override
    public Component init(final Messages resourceBundle) {
        return getView().init(this, resourceBundle);
    }

    protected Logger getLogger() {
        return logger;
    }

    protected View getView() {
        return view;
    }
}
