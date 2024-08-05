package com.nukkitx.protocol.bedrock.v712.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket.DisconnectFailReason;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisconnectSerializer_v712 implements BedrockPacketSerializer<DisconnectPacket> {
    public static final DisconnectSerializer_v712 INSTANCE = new DisconnectSerializer_v712();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, DisconnectPacket packet) {
        VarInts.writeInt(buffer, packet.getReason().ordinal());
        buffer.writeBoolean(packet.isMessageSkipped());
        if (!packet.isMessageSkipped()) {
            helper.writeString(buffer, packet.getKickMessage());
            helper.writeString(buffer, packet.getFilteredMessage());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, DisconnectPacket packet) {
        packet.setReason(DisconnectFailReason.values()[VarInts.readInt(buffer)]);
        packet.setMessageSkipped(buffer.readBoolean());
        if (!packet.isMessageSkipped()) {
            packet.setKickMessage(helper.readString(buffer));
            packet.setFilteredMessage(helper.readString(buffer));
        }
    }
}
