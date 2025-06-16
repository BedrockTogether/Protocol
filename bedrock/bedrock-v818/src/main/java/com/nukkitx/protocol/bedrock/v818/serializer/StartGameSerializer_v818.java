package com.nukkitx.protocol.bedrock.v818.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.data.SyncedPlayerMovementSettings;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v776.serializer.StartGameSerializer_v776;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v818 extends StartGameSerializer_v776 {

    public static final StartGameSerializer_v818 INSTANCE = new StartGameSerializer_v818();

    @Override
    protected void writeLevelSettings(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        super.writeLevelSettings(buffer, helper, packet);
        helper.writeString(buffer, packet.getOwnerId());
    }

    @Override
    protected void readLevelSettings(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        super.readLevelSettings(buffer, helper, packet);
        packet.setOwnerId(helper.readString(buffer));
    }

    @Override
    protected void writeSyncedPlayerMovementSettings(ByteBuf buffer, BedrockPacketHelper helper, SyncedPlayerMovementSettings playerMovementSettings) {
        VarInts.writeInt(buffer, playerMovementSettings.getRewindHistorySize());
        buffer.writeBoolean(playerMovementSettings.isServerAuthoritativeBlockBreaking());
    }

    @Override
    protected SyncedPlayerMovementSettings readSyncedPlayerMovementSettings(ByteBuf buffer, BedrockPacketHelper helper) {
        SyncedPlayerMovementSettings playerMovementSettings = new SyncedPlayerMovementSettings();
        playerMovementSettings.setRewindHistorySize(VarInts.readInt(buffer));
        playerMovementSettings.setServerAuthoritativeBlockBreaking(buffer.readBoolean());
        return playerMovementSettings;
    }
}
