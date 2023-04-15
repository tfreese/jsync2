// Created: 15.04.23
package de.freese.jsync2.swing.view;

import de.freese.jsync2.model.SyncPair;

/**
 * @author Thomas Freese
 */
public interface SyncView extends View {
    void addSyncPair(SyncPair syncPair);

    void restoreState();

    void saveState();
}
