package com.nukkitx.protocol.bedrock.data.biome;

import lombok.Value;

import java.util.Map;

@Value
public class BiomeDefinitions {

    Map<String, BiomeDefinitionData> definitions;
}
