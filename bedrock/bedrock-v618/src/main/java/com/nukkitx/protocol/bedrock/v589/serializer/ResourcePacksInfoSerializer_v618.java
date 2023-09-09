package com.nukkitx.protocol.bedrock.v589.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.v448.serializer.ResourcePacksInfoSerializer_v448;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePacksInfoSerializer_v618 extends ResourcePacksInfoSerializer_v448 {

    public static final ResourcePacksInfoSerializer_v618 INSTANCE = new ResourcePacksInfoSerializer_v618();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePacksInfoPacket packet, BedrockSession session) {
        super.serialize(buffer, helper, packet);
        helper.writeArray(buffer, packet.getCDNEntries(), (buf, entry) -> {
            helper.writeString(buf, entry.getPackId());
            helper.writeString(buf, entry.getRemoteUrl());
        });
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePacksInfoPacket packet, BedrockSession session) {
        super.deserialize(buffer, helper, packet);
        helper.readArray(buffer, packet.getCDNEntries(), buf -> new ResourcePacksInfoPacket.CDNEntry(helper.readString(buf), helper.readString(buf)));
    }
}

