package com.nukkitx.protocol.util;

import com.nukkitx.natives.util.Natives;
import com.nukkitx.natives.zlib.Deflater;
import com.nukkitx.natives.zlib.Inflater;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.zip.DataFormatException;

public class Zlib {
    public static final Zlib DEFAULT = new Zlib(false);
    public static final Zlib RAW = new Zlib(true);

    private static final int CHUNK = 8192;

    private final ThreadLocal<Inflater> inflaterLocal;
    private final ThreadLocal<Deflater> deflaterLocal;

    private Zlib(boolean raw) {
        // Required for Android API versions prior to 26.
        this.inflaterLocal = new ThreadLocal<Inflater>() {
            @Override
            public Inflater initialValue() {
                return Natives.ZLIB.get().create(raw);
            }
        };
        this.deflaterLocal = new ThreadLocal<Deflater>(){
            @Override
            protected Deflater initialValue() {
                return Natives.ZLIB.get().create(7, raw);
            }
        };
    }

    public void inflate(ByteBuf compressed, ByteBuf decompressed, int maxSize) throws DataFormatException {
        ByteBuf source = null;
        try {
            if (!compressed.isDirect()) {
                // We don't have a direct buffer. Create one.
                ByteBuf temporary = ByteBufAllocator.DEFAULT.ioBuffer();
                temporary.writeBytes(compressed);
                source = temporary;
            } else {
                source = compressed;
            }

            Inflater inflater = inflaterLocal.get();
            inflater.reset();
            inflater.setInput(source.internalNioBuffer(source.readerIndex(), source.readableBytes()));
            inflater.finished();

            while (!inflater.finished()) {
                decompressed.ensureWritable(CHUNK);
                int index = decompressed.writerIndex();
                int written = inflater.inflate(decompressed.internalNioBuffer(index, CHUNK));
                if (written < 1) {
                    break;
                }
                decompressed.writerIndex(index + written);
                if (maxSize > 0 && decompressed.writerIndex() >= maxSize) {
                    throw new DataFormatException("Inflated data exceeds maximum size");
                }
            }
        } finally {
            if (source != null && source != compressed) {
                source.release();
            }
        }
    }

    public void deflate(ByteBuf uncompressed, ByteBuf compressed, int level) throws DataFormatException {
        ByteBuf destination = null;
        ByteBuf source = null;
        try {
            if (!uncompressed.isDirect()) {
                // Source is not a direct buffer. Work on a temporary direct buffer and then write the contents out.
                source = ByteBufAllocator.DEFAULT.ioBuffer();
                source.writeBytes(uncompressed);
            } else {
                source = uncompressed;
            }

            if (!compressed.isDirect()) {
                // Destination is not a direct buffer. Work on a temporary direct buffer and then write the contents out.
                destination = ByteBufAllocator.DEFAULT.ioBuffer();
            } else {
                destination = compressed;
            }

            Deflater deflater = deflaterLocal.get();
            deflater.reset();
            deflater.setLevel(level);
            deflater.setInput(source.internalNioBuffer(source.readerIndex(), source.readableBytes()));

            while (!deflater.finished()) {
                int index = destination.writerIndex();
                destination.ensureWritable(CHUNK);
                int written = deflater.deflate(destination.internalNioBuffer(index, CHUNK));
                destination.writerIndex(index + written);
            }

            if (destination != compressed) {
                compressed.writeBytes(destination);
            }
        } finally {
            if (source != null && source != uncompressed) {
                source.release();
            }
            if (destination != null && destination != compressed) {
                destination.release();
            }
        }
    }
}