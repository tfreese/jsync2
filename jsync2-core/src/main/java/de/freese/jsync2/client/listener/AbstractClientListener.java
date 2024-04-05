// Created: 23.11.2018
package de.freese.jsync2.client.listener;

import de.freese.jsync2.Options;
import de.freese.jsync2.model.SyncItem;
import de.freese.jsync2.utils.JSyncUtils;

/**
 * @author Thomas Freese
 */
public abstract class AbstractClientListener implements ClientListener {
    protected String appendDryRun(final Options options, final String message) {
        String msg = message;

        if (msg != null && options.isDryRun()) {
            msg += " (dry-run)";
        }

        return msg;
    }

    protected String checksumProgressMessage(final Options options, final SyncItem syncItem, final long bytesRead) {
        final double percent = JSyncUtils.getPercent(bytesRead, syncItem.getSize());
        String message = null;

        if (bytesRead == 0 || (int) percent % 2 == 0) {
            message = String.format("checksum %s: %s / %s = %6.2f %%",
                    syncItem.getRelativePath(),
                    JSyncUtils.toHumanReadableSize(bytesRead),
                    JSyncUtils.toHumanReadableSize(syncItem.getSize()),
                    percent);
        }

        return message;
    }

    protected String copyMessage(final Options options, final SyncItem syncItem) {
        String message = String.format("copy: %s", syncItem.getRelativePath());

        message = appendDryRun(options, message);

        return message;
    }

    protected String copyProgressMessage(final Options options, final SyncItem syncItem, final long bytesTransferred) {
        final double percent = JSyncUtils.getPercent(bytesTransferred, syncItem.getSize());
        String message = null;

        if (bytesTransferred == 0 || (int) percent % 2 == 0) {
            message = String.format("copy %s: %s / %s = %6.2f %%",
                    syncItem.getRelativePath(),
                    JSyncUtils.toHumanReadableSize(bytesTransferred),
                    JSyncUtils.toHumanReadableSize(syncItem.getSize()),
                    percent);
        }

        message = appendDryRun(options, message);

        return message;
    }

    protected String deleteMessage(final Options options, final SyncItem syncItem) {
        String message = String.format("delete: %s", syncItem.getRelativePath());

        message = appendDryRun(options, message);

        return message;
    }

    protected String updateMessage(final Options options, final SyncItem syncItem) {
        String message = String.format("update attributes: %s", syncItem.getRelativePath());

        message = appendDryRun(options, message);

        return message;
    }

    protected String validateMessage(final Options options, final SyncItem syncItem) {
        String message = String.format("validate: %s", syncItem.getRelativePath());

        message = appendDryRun(options, message);

        return message;
    }
}
