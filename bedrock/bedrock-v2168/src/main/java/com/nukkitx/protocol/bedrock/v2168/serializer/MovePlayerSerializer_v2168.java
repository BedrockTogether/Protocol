package com.nukkitx.protocol.bedrock.v2168.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.v419.serializer.MovePlayerSerializer_v419;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The teleport data is behind an explicit presence flag in 1.26.40 instead of
 * being implied by the mode, and it is written after the riding entity id.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovePlayerSerializer_v2168 extends MovePlayerSerializer_v419 {

    public static final MovePlayerSerializer_v2168 INSTANCE = new MovePlayerSerializer_v2168();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, MovePlayerPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeVector3f(buffer, packet.getPosition());
        helper.writeVector3f(buffer, packet.getRotation());
        buffer.writeByte(packet.getMode().ordinal());
        buffer.writeBoolean(packet.isOnGround());
        VarInts.writeUnsignedLong(buffer, packet.getRidingRuntimeEntityId());

        if (packet.getMode() == MovePlayerPacket.Mode.TELEPORT) {
            buffer.writeBoolean(true);
            buffer.writeIntLE(packet.getTeleportationCause().ordinal());
            buffer.writeIntLE(packet.getEntityType());
        } else {
            buffer.writeBoolean(false);
        }

        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, MovePlayerPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setRotation(helper.readVector3f(buffer));
        packet.setMode(MovePlayerPacket.Mode.values()[buffer.readUnsignedByte()]);
        packet.setOnGround(buffer.readBoolean());
        packet.setRidingRuntimeEntityId(VarInts.readUnsignedLong(buffer));

        if (buffer.readBoolean()) {
            packet.setTeleportationCause(MovePlayerPacket.TeleportationCause.byId(buffer.readIntLE()));
            packet.setEntityType(buffer.readIntLE());
        }

        packet.setTick(VarInts.readUnsignedLong(buffer));
    }
}
