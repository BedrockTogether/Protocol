package com.nukkitx.protocol.bedrock.v2168;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.data.AuthoritativeMovementMode;
import com.nukkitx.protocol.bedrock.data.ChatRestrictionLevel;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameType;
import com.nukkitx.protocol.bedrock.data.NetworkPermissions;
import com.nukkitx.protocol.bedrock.data.PlayerPermission;
import com.nukkitx.protocol.bedrock.data.SpawnBiomeType;
import com.nukkitx.protocol.bedrock.data.SyncedPlayerMovementSettings;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Conformance: what this codec writes for 1.26.40 must be byte-identical to what
 * <a href="https://github.com/CloudburstMC/Protocol">CloudburstMC's Protocol</a>,
 * the reference implementation, produces for the same logical packet.
 * <p>
 * {@code v2168.vectors} holds those reference bytes. It is generated, not written
 * by hand, and the field values below must match the ones it was generated from —
 * they are not arbitrary. Two are chosen so a wrong encoding cannot pass: a varint,
 * an unsigned varint and a byte all encode 0 identically, so an all-default packet
 * would still match with the pre-1.26.40 encodings in place.
 */
public class ConformanceTest {

    private static final UUID WORLD_TEMPLATE_ID =
            UUID.fromString("6f8d3c21-4b7a-4e15-9a02-c7d1e8b45f39");
    /**
     * 64 zigzags to 128, so it needs two varint bytes but only one unsigned varint
     * byte: the value fails loudly if the pre-1.26.40 signed encoding comes back.
     */
    private static final int EDU_EDITION_OFFERS = 64;

    private static final Map<String, byte[]> VECTORS = loadVectors();

    @Test
    public void protocolVersionMatchesVectors() {
        assertEquals(2168, Bedrock_v2168.Bedrock_v2168.getProtocolVersion(),
                "codec protocol version");
        assertEquals("1.26.40", Bedrock_v2168.Bedrock_v2168.getMinecraftVersion(),
                "codec Minecraft version");
        assertTrue(VECTORS.containsKey("StartGame"), "vectors did not load");
    }

    @Test
    public void startGameMatchesReference() {
        StartGamePacket packet = new StartGamePacket();
        packet.setUniqueEntityId(1);
        packet.setRuntimeEntityId(1);
        packet.setPlayerGameType(GameType.CREATIVE);
        packet.setPlayerPosition(Vector3f.from(0, 0, 0));
        packet.setRotation(Vector2f.from(1, 1));

        packet.setSeed(-1);
        packet.setSpawnBiomeType(SpawnBiomeType.DEFAULT);
        packet.setCustomBiomeName("");
        packet.setDimensionId(0);
        packet.setGeneratorId(1);
        packet.setLevelGameType(GameType.SURVIVAL);
        packet.setHardcore(false);
        packet.setDifficulty(0);
        packet.setDefaultSpawn(Vector3i.from(0, 0, 0));
        packet.setAchievementsDisabled(false);
        packet.setWorldEditor(false);
        packet.setCreatedInEditor(false);
        packet.setExportedFromEditor(false);
        packet.setDayCycleStopTime(0);
        packet.setEduEditionOffers(EDU_EDITION_OFFERS);
        packet.setEduFeaturesEnabled(false);
        packet.setEducationProductionId("");
        packet.setRainLevel(0);
        packet.setLightningLevel(0);
        packet.setPlatformLockedContentConfirmed(false);
        packet.setMultiplayerGame(true);
        packet.setBroadcastingToLan(true);
        packet.setXblBroadcastMode(GamePublishSetting.PUBLIC);
        packet.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
        packet.setCommandsEnabled(true);
        packet.setTexturePacksRequired(false);
        packet.setExperimentsPreviouslyToggled(false);
        packet.setBonusChestEnabled(false);
        packet.setStartingWithMap(false);
        packet.setDefaultPlayerPermission(PlayerPermission.OPERATOR);
        packet.setServerChunkTickRange(4);
        packet.setBehaviorPackLocked(false);
        packet.setResourcePackLocked(false);
        packet.setFromLockedWorldTemplate(false);
        packet.setUsingMsaGamertagsOnly(false);
        packet.setFromWorldTemplate(false);
        packet.setWorldTemplateOptionLocked(false);
        packet.setOnlySpawningV1Villagers(true);
        packet.setDisablingPersonas(false);
        packet.setDisablingCustomSkins(false);
        packet.setEmoteChatMuted(false);
        packet.setVanillaVersion("1.17.40");
        packet.setLimitedWorldWidth(0);
        packet.setLimitedWorldHeight(0);
        packet.setNetherType(false);
        packet.setForceExperimentalGameplay(false);
        packet.setChatRestrictionLevel(ChatRestrictionLevel.NONE);
        packet.setDisablingPlayerInteractions(false);

        packet.setLevelId("world");
        packet.setLevelName("LAN Proxy");
        packet.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
        packet.setTrial(false);
        packet.setAuthoritativeMovementMode(AuthoritativeMovementMode.CLIENT);
        SyncedPlayerMovementSettings movement = new SyncedPlayerMovementSettings();
        movement.setMovementMode(AuthoritativeMovementMode.CLIENT);
        movement.setRewindHistorySize(0);
        movement.setServerAuthoritativeBlockBreaking(true);
        packet.setPlayerMovementSettings(movement);
        packet.setCurrentTick(0);
        packet.setEnchantmentSeed(0);
        packet.setMultiplayerCorrelationId("");
        packet.setInventoriesServerAuthoritative(false);
        packet.setServerEngine("");
        packet.setPlayerPropertyData(NbtMap.EMPTY);
        packet.setBlockRegistryChecksum(0);
        packet.setWorldTemplateId(WORLD_TEMPLATE_ID);
        packet.setClientSideGenerationEnabled(false);
        packet.setBlockNetworkIdsHashed(false);
        packet.setNetworkPermissions(new NetworkPermissions(false));
        packet.setServerId("");
        packet.setScenarioId("");
        packet.setWorldId("");
        packet.setOwnerId("");

        assertMatchesReference("StartGame", packet);
    }

    @Test
    public void transferMatchesReference() {
        TransferPacket packet = new TransferPacket();
        packet.setAddress("play.example.com");
        packet.setPort(19132);
        packet.setReloadWorld(false);
        assertMatchesReference("Transfer", packet);
    }

    @Test
    public void resourcePacksInfoMatchesReference() {
        ResourcePacksInfoPacket packet = new ResourcePacksInfoPacket();
        packet.setForcedToAccept(false);
        packet.setHasAddonPacks(false);
        packet.setScriptingEnabled(false);
        packet.setVibrantVisualsForceDisabled(false);
        packet.setWorldTemplateId(WORLD_TEMPLATE_ID);
        packet.setWorldTemplateVersion("");
        assertMatchesReference("ResourcePacksInfo", packet);
    }

    @Test
    public void movePlayerMatchesReference() {
        MovePlayerPacket packet = new MovePlayerPacket();
        packet.setRuntimeEntityId(1);
        packet.setPosition(Vector3f.from(0, 0, 0));
        packet.setRotation(Vector3f.from(0, 0, 0));
        packet.setMode(MovePlayerPacket.Mode.NORMAL);
        packet.setOnGround(false);
        packet.setRidingRuntimeEntityId(0);
        packet.setTick(0);
        assertMatchesReference("MovePlayer", packet);
    }

    @Test
    public void levelChunkMatchesReference() {
        LevelChunkPacket packet = new LevelChunkPacket();
        packet.setChunkX(0);
        packet.setChunkZ(0);
        packet.setDimension(0);
        packet.setSubChunksLength(0);
        packet.setRequestSubChunks(false);
        packet.setCachingEnabled(false);
        packet.setData(emptyChunkData());
        assertMatchesReference("LevelChunk", packet);
    }

    /**
     * The resource pack response is client-bound, so the vector is decoded rather
     * than encoded: this asserts the codec can read what a real 1.26.40 client sends.
     */
    @Test
    public void resourcePackClientResponseDecodesReference() {
        ResourcePackClientResponsePacket completed = decode("ResourcePackClientResponse");
        assertEquals(ResourcePackClientResponsePacket.Status.COMPLETED, completed.getStatus());
        assertTrue(completed.getPackIds().isEmpty(), "COMPLETED carries no pack ids");

        ResourcePackClientResponsePacket sendPacks = decode("ResourcePackClientResponseSendPacks");
        assertEquals(ResourcePackClientResponsePacket.Status.SEND_PACKS, sendPacks.getStatus());
        assertEquals(1, sendPacks.getPackIds().size());
        assertEquals("d8f0a1b2-1111-2222-3333-444455556666_1.0.0", sendPacks.getPackIds().get(0));
    }

    private static byte[] emptyChunkData() {
        byte[] data = new byte[258];
        data[0] = 0x0A; // TAG_Compound
        data[1] = 0x00; // name length (little endian short), low byte
        data[2] = 0x00; // name length, high byte
        data[3] = 0x00; // TAG_End
        return data;
    }

    private static void assertMatchesReference(String name, BedrockPacket packet) {
        byte[] expected = VECTORS.get(name);
        if (expected == null) {
            fail("no reference vector named " + name);
        }

        ByteBuf buf = Unpooled.buffer();
        byte[] actual;
        try {
            Bedrock_v2168.Bedrock_v2168.tryEncode(buf, packet, null);
            actual = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), actual);
        } finally {
            buf.release();
        }

        if (!Arrays.equals(expected, actual)) {
            fail(name + " does not match the reference vector"
                    + "\n  first difference at byte " + firstDifference(expected, actual)
                    + "\n  reference: " + expected.length + " bytes, " + hex(expected)
                    + "\n  ours:      " + actual.length + " bytes, " + hex(actual));
        }
        assertArrayEquals(expected, actual, name);
    }

    private static ResourcePackClientResponsePacket decode(String name) {
        byte[] vector = VECTORS.get(name);
        if (vector == null) {
            fail("no reference vector named " + name);
        }
        int id = Bedrock_v2168.Bedrock_v2168.getId(ResourcePackClientResponsePacket.class);
        ByteBuf buf = Unpooled.wrappedBuffer(vector);
        try {
            return (ResourcePackClientResponsePacket) Bedrock_v2168.Bedrock_v2168.tryDecode(buf, id, null);
        } finally {
            buf.release();
        }
    }

    private static int firstDifference(byte[] a, byte[] b) {
        int limit = Math.min(a.length, b.length);
        for (int i = 0; i < limit; i++) {
            if (a[i] != b[i]) {
                return i;
            }
        }
        return limit;
    }

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    private static Map<String, byte[]> loadVectors() {
        Map<String, byte[]> vectors = new HashMap<>();
        try (InputStream in = ConformanceTest.class.getResourceAsStream("/v2168.vectors")) {
            if (in == null) {
                throw new IllegalStateException("v2168.vectors is missing from the test resources");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int split = line.indexOf('=');
                if (split < 0) {
                    throw new IllegalStateException("malformed vector line: " + line);
                }
                String key = line.substring(0, split);
                if (key.equals("protocol") || key.equals("minecraftVersion")) {
                    continue;
                }
                vectors.put(key, unhex(line.substring(split + 1)));
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not read v2168.vectors", e);
        }
        return vectors;
    }

    private static byte[] unhex(String hex) {
        byte[] out = new byte[hex.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
}
