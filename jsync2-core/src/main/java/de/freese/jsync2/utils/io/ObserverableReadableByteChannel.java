// Created: 11.01.2017
package de.freese.jsync2.utils.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * @author Thomas Freese
 */
public class ObserverableReadableByteChannel implements ReadableByteChannel {
    private final boolean closeDelegate;

    private final ReadableByteChannel delegate;

    private long bytesRead;

    private LongConsumer bytesReadConsumer;

    public ObserverableReadableByteChannel(final ReadableByteChannel delegate, final boolean closeDelegate) {
        super();

        this.delegate = Objects.requireNonNull(delegate, "delegate required");
        this.closeDelegate = closeDelegate;
    }

    @Override
    public void close() throws IOException {
        if (this.closeDelegate) {
            this.delegate.close();
        }
    }

    @Override
    public boolean isOpen() {
        return this.delegate.isOpen();
    }

    public ObserverableReadableByteChannel onBytesRead(final LongConsumer bytesReadConsumer) {
        this.bytesReadConsumer = Objects.requireNonNull(bytesReadConsumer, "bytesReadConsumer required");

        return this;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        int readCount = this.delegate.read(dst);

        if (readCount > 0) {
            this.bytesRead += readCount;

            if (bytesReadConsumer != null) {
                this.bytesReadConsumer.accept(this.bytesRead);
            }
        }

        return readCount;
    }
}
