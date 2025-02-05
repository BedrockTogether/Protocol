package com.nukkitx.protocol.bedrock.v776.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.packet.ItemComponentPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemComponentSerializer_v776 implements BedrockPacketSerializer<ItemComponentPacket> {
    public static final ItemComponentSerializer_v776 INSTANCE = new ItemComponentSerializer_v776();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ItemComponentPacket packet) {
        VarInts.writeUnsignedInt(buffer, 0);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ItemComponentPacket packet) {
        //NOOP
    }
}
