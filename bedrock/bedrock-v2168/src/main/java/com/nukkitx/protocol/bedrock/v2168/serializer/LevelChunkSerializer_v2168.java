package com.nukkitx.protocol.bedrock.v2168.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.v649.serializer.LevelChunkSerializer_v649;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 1.26.40 stops overloading the sub chunk count with the -1/-2 sub chunk request
 * sentinels: the count is a plain unsigned varint followed by an optional sub
 * chunk limit. The blob id array is also always length prefixed now instead of
 * being written only when caching is enabled.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelChunkSerializer_v2168 extends LevelChunkSerializer_v649 {

    public static final LevelChunkSerializer_v2168 INSTANCE = new LevelChunkSerializer_v2168();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, LevelChunkPacket packet) {
        writeChunkLocation(buffer, packet);

        VarInts.writeUnsignedInt(buffer, packet.getSubChunksLength());

        buffer.writeBoolean(packet.isRequestSubChunks());
        if (packet.isRequestSubChunks()) {
            VarInts.writeInt(buffer, packet.getSubChunkLimit());
        }

        buffer.writeBoolean(packet.isCachingEnabled());

        LongList blobIds = packet.getBlobIds();
        VarInts.writeUnsignedInt(buffer, blobIds.size());

        for (long blobId : blobIds) {
            buffer.writeLongLE(blobId);
        }

        helper.writeByteArray(buffer, packet.getData());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, LevelChunkPacket packet) {
        readChunkLocation(buffer, packet);

        packet.setSubChunksLength(VarInts.readUnsignedInt(buffer));

        if (buffer.readBoolean()) {
            packet.setRequestSubChunks(true);
            packet.setSubChunkLimit(VarInts.readInt(buffer));
        }

        packet.setCachingEnabled(buffer.readBoolean());

        LongList blobIds = packet.getBlobIds();
        int length = VarInts.readUnsignedInt(buffer);

        for (int i = 0; i < length; i++) {
            blobIds.add(buffer.readLongLE());
        }

        packet.setData(helper.readByteArray(buffer));
    }
}
