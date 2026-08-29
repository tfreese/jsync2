package de.freese.jsync2.swing.view;

import de.freese.jsync2.model.SyncPair;

/**
 * @author Thomas Freese
 * @since 15.04.2023
 */
public interface SyncView extends View {
    void addSyncPair(SyncPair syncPair);

    void restoreState();

    void saveState();
}
