package com.nukkitx.protocol.bedrock.v662.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import com.nukkitx.protocol.bedrock.v291.serializer.SetEntityMotionSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SetEntityMotionSerializer_v662 extends SetEntityMotionSerializer_v291 {
    public static final SetEntityMotionSerializer_v662 INSTANCE = new SetEntityMotionSerializer_v662();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, SetEntityMotionPacket packet, BedrockSession session) {
        super.serialize(buffer, helper, packet);
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, SetEntityMotionPacket packet, BedrockSession session) {
        super.deserialize(buffer, helper, packet);
        packet.setTick(VarInts.readUnsignedLong(buffer));
    }
}

