package com.nukkitx.protocol.bedrock.v2168.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import com.nukkitx.protocol.bedrock.v291.serializer.ResourcePackClientResponseSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket.Status;

/**
 * 1.26.40 writes the status as an unsigned varint followed by its name, and
 * drops the pack id array unless the status is SEND_PACKS. The varint is the
 * status ordinal minus one because the NONE placeholder is not on the wire.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePackClientResponseSerializer_v2168 extends ResourcePackClientResponseSerializer_v291 {

    public static final ResourcePackClientResponseSerializer_v2168 INSTANCE = new ResourcePackClientResponseSerializer_v2168();

    private static final String[] RESPONSE_STATUS = {"cancel", "downloading", "downloadingfinished", "resourcepackstackfinished"};

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackClientResponsePacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getStatus().ordinal() - 1);

        helper.writeString(buffer, RESPONSE_STATUS[packet.getStatus().ordinal() - 1]);

        if (packet.getStatus() != Status.SEND_PACKS) {
            return;
        }

        helper.writeArray(buffer, packet.getPackIds(), helper::writeString);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, ResourcePackClientResponsePacket packet) {
        packet.setStatus(Status.values()[VarInts.readUnsignedInt(buffer) + 1]);

        helper.readString(buffer); // type enum

        if (packet.getStatus() != Status.SEND_PACKS) {
            return;
        }

        helper.readArray(buffer, packet.getPackIds(), helper::readString);
    }
}
