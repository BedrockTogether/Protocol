package com.nukkitx.protocol.bedrock.v662.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.v589.serializer.ResourcePacksInfoSerializer_v618;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePacksInfoSerializer_v622 extends ResourcePacksInfoSerializer_v618 {
    public static final ResourcePacksInfoSerializer_v622 INSTANCE = new ResourcePacksInfoSerializer_v622();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePacksInfoPacket packet, BedrockSession session) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isHasAddonPacks());
        buffer.writeBoolean(packet.isScriptingEnabled());
        buffer.writeBoolean(packet.isForcingServerPacksEnabled());
        writePacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        writePacks(buffer, packet.getResourcePackInfos(), helper, true);
        this.writeCDNEntries(buffer, helper, packet);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePacksInfoPacket packet, BedrockSession session) {
        packet.setForcedToAccept(buffer.readBoolean());
        packet.setHasAddonPacks(buffer.readBoolean());
        packet.setScriptingEnabled(buffer.readBoolean());
        packet.setForcingServerPacksEnabled(buffer.readBoolean());
        readPacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        readPacks(buffer, packet.getResourcePackInfos(), helper, true);
        this.readCDNEntries(buffer, helper, packet);
    }
}

