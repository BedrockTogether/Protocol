package com.nukkitx.protocol.bedrock.v589.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.data.NetworkPermissions;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v582.serializer.StartGameSerializer_v582;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v589 extends StartGameSerializer_v582 {

    public static final StartGameSerializer_v589 INSTANCE = new StartGameSerializer_v589();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet, BedrockSession session) {
        super.serialize(buffer, helper, packet, session);
        this.writeNetworkPermissions(buffer, helper, packet.getNetworkPermissions());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet, BedrockSession session) {
        super.deserialize(buffer, helper, packet, session);
        packet.setNetworkPermissions(this.readNetworkPermissions(buffer, helper));
    }

    protected NetworkPermissions readNetworkPermissions(ByteBuf buffer, BedrockPacketHelper helper) {
        boolean serverAuthSound = buffer.readBoolean();
        return new NetworkPermissions(serverAuthSound);
    }

    protected void writeNetworkPermissions(ByteBuf buffer, BedrockPacketHelper helper, NetworkPermissions permissions) {
        buffer.writeBoolean(permissions.isServerAuthSounds());
    }
}
