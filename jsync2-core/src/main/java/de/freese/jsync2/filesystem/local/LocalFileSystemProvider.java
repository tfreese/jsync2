// Created: 21.07.2021
package de.freese.jsync2.filesystem.local;

import java.net.URI;

import de.freese.jsync2.filesystem.FileSystemProvider;
import de.freese.jsync2.filesystem.Receiver;
import de.freese.jsync2.filesystem.Sender;
import de.freese.jsync2.model.JSyncProtocol;

/**
 * @author Thomas Freese
 */
public class LocalFileSystemProvider implements FileSystemProvider {
    @Override
    public Receiver createReceiver(final URI uri) {
        return new LocalhostReceiver();
    }

    @Override
    public Sender createSender(final URI uri) {
        return new LocalhostSender();
    }

    @Override
    public boolean supportsProtocol(final String scheme) {
        return JSyncProtocol.FILE.getScheme().equals(scheme);
    }
}
