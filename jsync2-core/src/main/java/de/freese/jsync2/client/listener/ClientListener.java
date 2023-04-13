// Created: 23.11.2018
package de.freese.jsync2.client.listener;

import java.util.EventListener;

import de.freese.jsync2.Options;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
public interface ClientListener extends EventListener {
    void checksumProgress(Options options, SyncItem syncItem, long bytesRead);

    void copyProgress(Options options, SyncItem syncItem, long bytesTransferred);

    void delete(Options options, SyncItem syncItem);

    void error(String message, Throwable th);

    void update(Options options, SyncItem syncItem);

    void validate(Options options, SyncItem syncItem);
}
