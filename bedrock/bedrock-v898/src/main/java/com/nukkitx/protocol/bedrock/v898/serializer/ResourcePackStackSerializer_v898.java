package com.nukkitx.protocol.bedrock.v898.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v671.serializer.ResourcePackStackSerializer_v671;
import com.nukkitx.protocol.bedrock.v827.serializer.StartGameSerializer_v827;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePackStackSerializer_v898 extends ResourcePackStackSerializer_v671 {

    public static final ResourcePackStackSerializer_v898 INSTANCE = new ResourcePackStackSerializer_v898();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackStackPacket packet) {
        buffer.writeBoolean(packet.isForcedToAccept());
        helper.writeArray(buffer, packet.getResourcePacks(), this::writeEntry);
        helper.writeString(buffer, packet.getGameVersion());
        helper.writeExperiments(buffer, packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyToggled());
        buffer.writeBoolean(packet.isHasEditorPacks());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackStackPacket packet) {
        packet.setForcedToAccept(buffer.readBoolean());
        helper.readArray(buffer, packet.getResourcePacks(), this::readEntry);
        packet.setGameVersion(helper.readString(buffer));
        helper.readExperiments(buffer, packet.getExperiments());
        packet.setExperimentsPreviouslyToggled(buffer.readBoolean());
        packet.setHasEditorPacks(buffer.readBoolean());
    }
}