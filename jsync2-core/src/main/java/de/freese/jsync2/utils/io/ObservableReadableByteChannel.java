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
public class ObservableReadableByteChannel implements ReadableByteChannel {
    private final boolean closeDelegate;
    private final ReadableByteChannel delegate;

    private long bytesRead;
    private LongConsumer bytesReadConsumer;

    public ObservableReadableByteChannel(final ReadableByteChannel delegate, final boolean closeDelegate) {
        super();

        this.delegate = Objects.requireNonNull(delegate, "delegate required");
        this.closeDelegate = closeDelegate;
    }

    @Override
    public void close() throws IOException {
        if (closeDelegate) {
            delegate.close();
        }
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    public ObservableReadableByteChannel onBytesRead(final LongConsumer bytesReadConsumer) {
        this.bytesReadConsumer = Objects.requireNonNull(bytesReadConsumer, "bytesReadConsumer required");

        return this;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final int readCount = delegate.read(dst);

        if (readCount > 0) {
            bytesRead += readCount;

            if (bytesReadConsumer != null) {
                bytesReadConsumer.accept(bytesRead);
            }
        }

        return readCount;
    }
}
