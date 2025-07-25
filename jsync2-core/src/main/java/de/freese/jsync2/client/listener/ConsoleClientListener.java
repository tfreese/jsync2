// Created: 23.11.2018
package de.freese.jsync2.client.listener;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import de.freese.jsync2.Options;
import de.freese.jsync2.model.SyncItem;

/**
 * @author Thomas Freese
 */
public class ConsoleClientListener extends AbstractClientListener {
    private final PrintStream printStream;
    private final PrintStream printStreamError;

    public ConsoleClientListener() {
        super();

        // final Console console = System.console();
        //
        // if (console != null) {
        // printStream = console.writer();
        // }
        // else {
        // printStream = System.out;
        // }

        printStream = System.out;

        printStreamError = System.err;
    }

    @Override
    public void checksumProgress(final Options options, final SyncItem syncItem, final long bytesRead) {
        if (bytesRead == 0) {
            return;
        }

        final String message = checksumProgressMessage(options, syncItem, bytesRead);

        if (message == null) {
            return;
        }

        getPrintStream().print(message + "\r");

        if (bytesRead == syncItem.getSize()) {
            getPrintStream().println();
        }
    }

    @Override
    public void copyProgress(final Options options, final SyncItem syncItem, final long bytesTransferred) {
        if (bytesTransferred == 0) {
            return;
        }

        final String message = copyProgressMessage(options, syncItem, bytesTransferred);

        if (message == null) {
            return;
        }

        getPrintStream().print(message + "\r");

        if (bytesTransferred == syncItem.getSize()) {
            getPrintStream().println();
        }
    }

    @Override
    public void delete(final Options options, final SyncItem syncItem) {
        final String message = deleteMessage(options, syncItem);

        getPrintStream().println(message);
    }

    @Override
    public void error(final String message, final Throwable th) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        th.printStackTrace(printWriter);

        final String stackTrace = stringWriter.toString();

        getPrintStreamError().println();
        getPrintStreamError().println("ERROR - " + (message == null ? "" : message));
        getPrintStreamError().println(stackTrace);
    }

    @Override
    public void update(final Options options, final SyncItem syncItem) {
        final String message = updateMessage(options, syncItem);

        getPrintStream().println(message);
    }

    @Override
    public void validate(final Options options, final SyncItem syncItem) {
        final String message = validateMessage(options, syncItem);

        getPrintStream().println(message);
    }

    protected PrintStream getPrintStream() {
        return printStream;
    }

    protected PrintStream getPrintStreamError() {
        return printStreamError;
    }
}
