// Created: 15.04.23
package de.freese.jsync2.swing.controller;

import java.awt.Component;

import de.freese.jsync2.swing.Messages;
import de.freese.jsync2.swing.view.DefaultSyncView;
import de.freese.jsync2.swing.view.SyncView;

/**
 * @author Thomas Freese
 */
public class JSyncController extends AbstractController {
    public JSyncController() {
        super(new DefaultSyncView());
    }

    @Override
    public Component init(final Messages resourceBundle) {
        final Component component = super.init(resourceBundle);

        getView().restoreState();

        return component;
    }

    @Override
    public void shutdown() {
        getView().saveState();
    }

    @Override
    protected SyncView getView() {
        return (SyncView) super.getView();
    }
}
