package com.nukkitx.protocol.bedrock.data.biome;

import com.nukkitx.protocol.util.index.Indexable;

import java.util.Map;

final class UnindexedBiomes implements Indexable<Map<String, BiomeDefinitionData>> {
    private final Map<String, BiomeDefinitionData> biomes;

    UnindexedBiomes(Map<String, BiomeDefinitionData> biomes) {
        this.biomes = biomes;
    }

    @Override
    public Map<String, BiomeDefinitionData> get() {
        return this.biomes;
    }
}
