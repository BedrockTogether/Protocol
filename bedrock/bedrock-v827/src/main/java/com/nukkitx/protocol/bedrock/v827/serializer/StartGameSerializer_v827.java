package com.nukkitx.protocol.bedrock.v827.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v818.serializer.StartGameSerializer_v818;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v827 extends StartGameSerializer_v818 {

    public static final StartGameSerializer_v827 INSTANCE = new StartGameSerializer_v827();

    @Override
    protected void readBeforeNetworkPermissions(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        packet.setTickDeathSystemsEnabled(buffer.readBoolean());
        packet.setNetworkPermissions(this.readNetworkPermissions(buffer, helper));
    }

    @Override
    protected void writeBeforeNetworkPermissions(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        buffer.writeBoolean(packet.isTickDeathSystemsEnabled());
        this.writeNetworkPermissions(buffer, helper, packet.getNetworkPermissions());
    }
}