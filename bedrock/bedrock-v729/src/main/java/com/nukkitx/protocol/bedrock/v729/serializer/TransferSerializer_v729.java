package com.nukkitx.protocol.bedrock.v729.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import com.nukkitx.protocol.bedrock.v291.serializer.TransferSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferSerializer_v729 extends TransferSerializer_v291 {

    public static final TransferSerializer_v729 INSTANCE = new TransferSerializer_v729();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, TransferPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isReloadWorld());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, TransferPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setReloadWorld(buffer.readBoolean());
    }
}
