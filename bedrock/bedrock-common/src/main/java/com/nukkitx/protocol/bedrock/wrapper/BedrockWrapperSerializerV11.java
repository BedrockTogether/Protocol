package com.nukkitx.protocol.bedrock.wrapper;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.data.PacketCompressionAlgorithm;
import com.nukkitx.protocol.bedrock.exception.PacketSerializeException;
import com.nukkitx.protocol.bedrock.wrapper.compression.CompressionSerializer;
import com.nukkitx.protocol.bedrock.wrapper.compression.NoCompression;
import com.nukkitx.protocol.bedrock.wrapper.compression.SnappyCompression;
import com.nukkitx.protocol.bedrock.wrapper.compression.ZlibCompression;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

import java.util.Collection;
import java.util.zip.DataFormatException;

public class BedrockWrapperSerializerV11 extends BedrockWrapperSerializer {

    private CompressionSerializer compressionSerializer = NoCompression.INSTANCE;
    private boolean prefixed = false;

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketCodec codec, Collection<BedrockPacket> packets, int level, BedrockSession session) {
        ByteBuf uncompressed = ByteBufAllocator.DEFAULT.ioBuffer(packets.size() << 3);
        try {
            for (BedrockPacket packet : packets) {
                ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
                try {
                    int id = codec.getId(packet);
                    int header = 0;
                    header |= (id & 0x3ff);
                    header |= (packet.getSenderId() & 3) << 10;
                    header |= (packet.getClientId() & 3) << 12;
                    VarInts.writeUnsignedInt(packetBuffer, header);
                    codec.tryEncode(packetBuffer, packet, session);

                    VarInts.writeUnsignedInt(uncompressed, packetBuffer.readableBytes());
                    uncompressed.writeBytes(packetBuffer);
                } catch (PacketSerializeException e) {
                    log.debug("Error occurred whilst encoding " + packet.getClass().getSimpleName(), e);
                } finally {
                    packetBuffer.release();
                }
            }
            if (prefixed) {
                buffer.writeByte(this.getCompressionHeader(compressionSerializer));
                this.compressionSerializer.compress(uncompressed, buffer, level);
            } else {
                this.compressionSerializer.compress(uncompressed, buffer, level);
            }
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to deflate buffer data", e);
        } finally {
            uncompressed.release();
        }
    }

    @Override
    public void deserialize(ByteBuf compressed, BedrockPacketCodec codec, Collection<BedrockPacket> packets, BedrockSession session) {
        ByteBuf decompressed = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            CompressionSerializer compression;
            if (this.prefixed) {
                compression = this.getCompression(compressed.readByte());
            } else {
                compression = compressionSerializer;
            }
            compression.decompress(compressed, decompressed, 12 * 1024 * 1024); // 12MBs

            while (decompressed.isReadable()) {
                int length = VarInts.readUnsignedInt(decompressed);
                ByteBuf packetBuffer = decompressed.readSlice(length);

                if (!packetBuffer.isReadable()) {
                    throw new DataFormatException("Packet cannot be empty");
                }

                try {
                    int header = VarInts.readUnsignedInt(packetBuffer);
                    int packetId = header & 0x3ff;
                    BedrockPacket packet = codec.tryDecode(packetBuffer, packetId, session);
                    packet.setPacketId(packetId);
                    packet.setSenderId((header >>> 10) & 3);
                    packet.setClientId((header >>> 12) & 3);
                    packets.add(packet);
                } catch (PacketSerializeException e) {
                    log.debug("Error occurred whilst decoding packet", e);
                    if (log.isTraceEnabled()) {
                        log.trace("Packet contents\n{}", ByteBufUtil.prettyHexDump(packetBuffer.readerIndex(0)));
                    }
                }
            }
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to inflate buffer data", e);
        } finally {
            decompressed.release();
        }
    }

    public void setCompressionSerializer(CompressionSerializer compressionSerializer, boolean prefixed) {
        this.compressionSerializer = compressionSerializer;
        this.prefixed = prefixed;
    }

    public CompressionSerializer getCompressionSerializer() {
        return compressionSerializer;
    }

    public boolean isPrefixed() {
        return prefixed;
    }

    protected final byte getCompressionHeader(CompressionSerializer compression) {
        if (compression instanceof NoCompression) {
            return (byte) 0xff;
        } else if (compression instanceof ZlibCompression) {
            return 0x00;
        } else if (compression instanceof SnappyCompression) {
            return 0x01;
        }

        throw new IllegalArgumentException("Unknown compression algorithm " + compression);
    }

    protected final CompressionSerializer getCompression(byte header) {
        switch (header) {
            case 0x00:
                return ZlibCompression.INSTANCE;
            case 0x01:
                return SnappyCompression.INSTANCE;
            case (byte) 0xff:
                return NoCompression.INSTANCE;
        }

        throw new IllegalArgumentException("Unknown compression algorithm " + header);
    }
}
