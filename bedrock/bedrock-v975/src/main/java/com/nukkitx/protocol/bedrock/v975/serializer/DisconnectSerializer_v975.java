package com.nukkitx.protocol.bedrock.v975.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket.DisconnectFailReason;
import com.nukkitx.protocol.bedrock.v712.serializer.DisconnectSerializer_v712;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisconnectSerializer_v975 extends DisconnectSerializer_v712 {
    public static final DisconnectSerializer_v975 INSTANCE = new DisconnectSerializer_v975();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, DisconnectPacket packet) {
        VarInts.writeInt(buffer, packet.getReason().ordinal());
        VarInts.writeUnsignedInt(buffer, 1);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, DisconnectPacket packet) {
        packet.setReason(DisconnectFailReason.values()[VarInts.readInt(buffer)]);
        packet.setMessageSkipped(VarInts.readUnsignedInt(buffer) != 0);
    }
}
