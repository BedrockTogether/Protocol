package com.nukkitx.protocol.bedrock.v1001.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.data.ChatRestrictionLevel;
import com.nukkitx.protocol.bedrock.data.EduSharedUriResource;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameType;
import com.nukkitx.protocol.bedrock.data.SpawnBiomeType;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v898.serializer.StartGameSerializer_v898;
import com.nukkitx.protocol.bedrock.v924.serializer.StartGameSerializer_v924;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v1001 extends StartGameSerializer_v924 {

    public static final StartGameSerializer_v1001 INSTANCE = new StartGameSerializer_v1001();

    @Override
    protected void writeLevelSettings(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        super.writeLevelSettings(buffer, helper, packet);

        VarInts.writeInt(buffer, 0); // getServerEditorConnectionPolicy
        buffer.writeBoolean(false); // isAllowAnonymousBlockDropsInEditorWorlds
    }

    @Override
    protected void writeBeforeNetworkPermissions(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        this.writeNetworkPermissions(buffer, helper, packet.getNetworkPermissions());

        buffer.writeBoolean(false); // isLoggingChat
    }
}