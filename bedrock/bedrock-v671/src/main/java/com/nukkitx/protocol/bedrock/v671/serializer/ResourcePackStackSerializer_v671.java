package com.nukkitx.protocol.bedrock.v671.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket;
import com.nukkitx.protocol.bedrock.v419.serializer.ResourcePackStackSerializer_v419;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePackStackSerializer_v671 extends ResourcePackStackSerializer_v419 {
    public static final ResourcePackStackSerializer_v671 INSTANCE = new ResourcePackStackSerializer_v671();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackStackPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isHasEditorPacks());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackStackPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setHasEditorPacks(buffer.readBoolean());
    }
}

