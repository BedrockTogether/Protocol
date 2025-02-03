package com.nukkitx.protocol.bedrock.v776.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v748.serializer.ResourcePacksInfoSerializer_v748;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import serializer.StartGameSerializer_v685;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v776 extends StartGameSerializer_v685 {
    public static final StartGameSerializer_v776 INSTANCE = new StartGameSerializer_v776();

    @Override
    protected void writeItemEntries(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet) {
        // noop
    }

    @Override
    protected void readItemEntries(ByteBuf buffer, BedrockPacketHelper helper, StartGamePacket packet, BedrockSession session) {
        // noop
    }
}

