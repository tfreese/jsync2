// Created: 26.10.2016
package de.freese.jsync2.utils.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * @author Thomas Freese
 */
public class ObserverableWritableByteChannel implements WritableByteChannel {
    private final boolean closeDelegate;
    private final WritableByteChannel delegate;

    private long bytesWritten;
    private LongConsumer bytesWrittenConsumer;

    public ObserverableWritableByteChannel(final WritableByteChannel delegate, final boolean closeDelegate) {
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

    public ObserverableWritableByteChannel onBytesWritten(final LongConsumer bytesWrittenConsumer) {
        this.bytesWrittenConsumer = Objects.requireNonNull(bytesWrittenConsumer, "bytesWrittenConsumer required");

        return this;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int writeCount = this.delegate.write(src);

        if (writeCount > 0) {
            this.bytesWritten += writeCount;

            if (bytesWrittenConsumer != null) {
                this.bytesWrittenConsumer.accept(this.bytesWritten);
            }
        }

        return writeCount;
    }
}
