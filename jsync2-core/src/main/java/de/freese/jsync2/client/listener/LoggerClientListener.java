// Created: 23.11.2018
package de.freese.jsync2.client.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jsync2.Options;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
public class LoggerClientListener extends AbstractClientListener {
    private final Logger logger = LoggerFactory.getLogger("Client");

    public void checksumProgress(final Options options, final SyncItem syncItem, final long bytesRead) {
        final String message = checksumProgressMessage(options, syncItem, bytesRead);

        if (message == null) {
            return;
        }

        getLogger().info(message);
    }

    @Override
    public void copyProgress(final Options options, final SyncItem syncItem, final long bytesTransferred) {
        final String message = copyProgressMessage(options, syncItem, bytesTransferred);

        if (message == null) {
            return;
        }

        getLogger().info(message);
    }

    @Override
    public void delete(final Options options, final SyncItem syncItem) {
        final String message = deleteMessage(options, syncItem);

        getLogger().info(message);
    }

    @Override
    public void error(final String message, final Throwable th) {
        getLogger().error(message, th);
    }

    @Override
    public void update(final Options options, final SyncItem syncItem) {
        final String message = updateMessage(options, syncItem);

        getLogger().info(message);
    }

    @Override
    public void validate(final Options options, final SyncItem syncItem) {
        final String message = validateMessage(options, syncItem);

        getLogger().info(message);
    }

    protected Logger getLogger() {
        return this.logger;
    }
}
