package com.nukkitx.protocol.bedrock.data.biome;

import lombok.Value;

import java.util.List;

@Value
public class BiomeDefinitionData {

    String id;
    float temperature;
    float downfall;
    float redSporeDensity;
    float blueSporeDensity;
    float ashDensity;
    float whiteAshDensity;
    float depth;
    float scale;
    Color mapWaterColor;
    boolean rain;
    List<String> tags;
}
