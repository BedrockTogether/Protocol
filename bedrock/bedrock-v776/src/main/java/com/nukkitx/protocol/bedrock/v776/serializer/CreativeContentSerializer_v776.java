package com.nukkitx.protocol.bedrock.v776.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.packet.CreativeContentPacket;
import com.nukkitx.protocol.bedrock.packet.ItemComponentPacket;
import com.nukkitx.protocol.bedrock.v407.serializer.CreativeContentSerializer_v407;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreativeContentSerializer_v776 extends CreativeContentSerializer_v407 {
    public static final CreativeContentSerializer_v776 INSTANCE = new CreativeContentSerializer_v776();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, CreativeContentPacket packet, BedrockSession session) {
        VarInts.writeUnsignedInt(buffer, 0); // group size
        VarInts.writeUnsignedInt(buffer, 0); // item size
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, CreativeContentPacket packet, BedrockSession session) {
        //NOOP
    }
}
