package com.nukkitx.protocol.bedrock.v649.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket.DisconnectFailReason;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.v291.serializer.DisconnectSerializer_v291;
import com.nukkitx.protocol.bedrock.v486.serializer.LevelChunkSerializer_v486;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelChunkSerializer_v649 extends LevelChunkSerializer_v486 {
    public static final LevelChunkSerializer_v649 INSTANCE = new LevelChunkSerializer_v649();

    @Override
    protected void writeChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        super.writeChunkLocation(buffer, packet);
        VarInts.writeInt(buffer, packet.getDimension());
    }

    @Override
    protected void readChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        super.readChunkLocation(buffer, packet);
        packet.setDimension(VarInts.readInt(buffer));
    }
}

