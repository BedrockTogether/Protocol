package com.nukkitx.protocol.bedrock.v2168.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.v818.serializer.ResourcePacksInfoSerializer_v818;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * The pack list length prefix changed from a little endian short to an unsigned
 * varint in 1.26.40. The entries themselves are unchanged.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcePacksInfoSerializer_v2168 extends ResourcePacksInfoSerializer_v818 {

    public static final ResourcePacksInfoSerializer_v2168 INSTANCE = new ResourcePacksInfoSerializer_v2168();

    @Override
    protected void readPacks(ByteBuf buffer, Collection<ResourcePacksInfoPacket.Entry> array, BedrockPacketHelper helper,
                             boolean resource) {
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            array.add(this.readEntry(buffer, helper, resource));
        }
    }

    @Override
    protected void writePacks(ByteBuf buffer, Collection<ResourcePacksInfoPacket.Entry> array, BedrockPacketHelper helper,
                              boolean resource) {
        VarInts.writeUnsignedInt(buffer, array.size());
        for (ResourcePacksInfoPacket.Entry entry : array) {
            this.writeEntry(buffer, helper, entry, resource);
        }
    }
}
