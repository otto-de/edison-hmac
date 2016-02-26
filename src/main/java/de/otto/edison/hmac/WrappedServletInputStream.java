package de.otto.edison.hmac;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class WrappedServletInputStream extends ServletInputStream {
    private final InputStream inputStream;

    public WrappedServletInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // using default InputStream.read method would blow
        // runtime as it falls back to single byte reading
        // in ServletInputStream. Be smarter, offer larger read chunk
        return inputStream.read(b, off, len);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public boolean markSupported() {
        return inputStream.markSupported();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        try {
            return inputStream.available()>0;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void setReadListener(ReadListener listener) {

    }
}