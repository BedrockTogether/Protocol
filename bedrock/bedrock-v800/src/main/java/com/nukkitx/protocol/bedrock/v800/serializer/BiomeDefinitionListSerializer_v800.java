package com.nukkitx.protocol.bedrock.v800.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.data.biome.BiomeDefinitionData;
import com.nukkitx.protocol.bedrock.data.biome.BiomeDefinitions;
import com.nukkitx.protocol.bedrock.packet.BiomeDefinitionListPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BiomeDefinitionListSerializer_v800 implements BedrockPacketSerializer<BiomeDefinitionListPacket> {

    public static final BiomeDefinitionListSerializer_v800 INSTANCE = new BiomeDefinitionListSerializer_v800();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, BiomeDefinitionListPacket packet) {
        ArrayList<String> strings = new ArrayList<>();
        BiomeDefinitions biomeDefinitions = packet.getBiomes();
        Map<String, BiomeDefinitionData> biomes = biomeDefinitions.getDefinitions();
        helper.writeArray(buffer, biomes.entrySet(), (byteBuf, aHelper, entry) -> {
            String name = entry.getKey();
            BiomeDefinitionData definition = entry.getValue();
            if (!strings.contains(name)) {
                strings.add(name);
            }
            byteBuf.writeShortLE(strings.indexOf(name));
            writeDefinition(byteBuf, aHelper, definition, strings);
        });
        helper.writeArray(buffer, strings, (byteBuf, BedrockPacketHelper, biomeName) -> {
            BedrockPacketHelper.writeString(byteBuf, biomeName);
        });
    }

    protected void writeDefinition(ByteBuf buffer, BedrockPacketHelper helper, BiomeDefinitionData definition, ArrayList<String> strings) {
        helper.writeOptional(buffer, Objects::nonNull, definition.getId(), (buf, id) -> {
            if (!strings.contains(id)) {
                strings.add(id);
            }
            buf.writeShortLE(strings.indexOf(id));
        });
        buffer.writeFloatLE(definition.getTemperature());
        buffer.writeFloatLE(definition.getDownfall());
        buffer.writeFloatLE(definition.getRedSporeDensity());
        buffer.writeFloatLE(definition.getBlueSporeDensity());
        buffer.writeFloatLE(definition.getAshDensity());
        buffer.writeFloatLE(definition.getWhiteAshDensity());
        buffer.writeFloatLE(definition.getDepth());
        buffer.writeFloatLE(definition.getScale());
        buffer.writeIntLE(definition.getMapWaterColor().getRGB());
        buffer.writeBoolean(definition.isRain());
        helper.writeOptional(buffer, Objects::nonNull, definition.getTags(), (byteBuf, tags) -> {
            VarInts.writeUnsignedInt(byteBuf, tags.size());
            for (String tag : tags) {
                if (!strings.contains(tag)) {
                    strings.add(tag);
                }
                byteBuf.writeShortLE(strings.indexOf(tag));
            }
        });
        helper.writeOptional(buffer, Objects::nonNull, null, (a, b) -> {
        });
    }
}
