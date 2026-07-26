package com.nukkitx.protocol.bedrock.v2168.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import com.nukkitx.protocol.bedrock.v729.serializer.TransferSerializer_v729;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 1.26.40 appends an optional GatheringsConfigurationJoinInfo. This library has
 * no model for gatherings, so an absent value is always written and a present
 * one is rejected rather than silently mis-parsed.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferSerializer_v2168 extends TransferSerializer_v729 {

    public static final TransferSerializer_v2168 INSTANCE = new TransferSerializer_v2168();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, TransferPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(false); // gatheringsConfigurationJoinInfo
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, TransferPacket packet) {
        super.deserialize(buffer, helper, packet);
        if (buffer.readBoolean()) {
            throw new UnsupportedOperationException("TransferPacket gatherings configuration is not supported");
        }
    }
}
