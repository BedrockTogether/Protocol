package com.nukkitx.protocol.bedrock.packet;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.data.biome.BiomeDefinitions;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class BiomeDefinitionListPacket extends BedrockPacket {
    private NbtMap definitions;
    /**
     * @since v800 (1.21.80)
     */
    private BiomeDefinitions biomes;

    @Override
    public boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.BIOME_DEFINITIONS_LIST;
    }
}
