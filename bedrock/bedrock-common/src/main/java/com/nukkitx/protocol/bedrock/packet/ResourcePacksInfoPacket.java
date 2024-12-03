package com.nukkitx.protocol.bedrock.packet;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class ResourcePacksInfoPacket extends BedrockPacket {
    private final List<Entry> behaviorPackInfos = new ObjectArrayList<>();
    private final List<Entry> resourcePackInfos = new ObjectArrayList<>();
    private boolean forcedToAccept;
    /**
     * @since v662
     */
    private boolean hasAddonPacks;
    private boolean scriptingEnabled;
    private boolean forcingServerPacksEnabled;
    /**
     * @since v618
     * @deprecated since v729
     */
    private List<CDNEntry> CDNEntries = new ObjectArrayList<>();
    /**
     * @since v765
     */
    private UUID worldTemplateId;
    /**
     * @since v765
     */
    private String worldTemplateVersion;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACKS_INFO;
    }

    @Value
    public static class Entry {
        private final UUID packId;
        private final String packVersion;
        private final long packSize;
        private final String contentKey;
        private final String subPackName;
        private final String contentId;
        private final boolean scripting;
        private final boolean raytracingCapable;
        /**
         * @since v712
         */
        private final boolean addonPack;
        /**
         * @since v748
         */
        String cdnUrl;

        public static Entry from(String packId, String packVersion, long packSize, String contentKey, String subPackName, String contentId, boolean scripting, boolean raytracingCapable, boolean addonPack, String cdnUrl) {
            return new Entry(UUID.fromString(packId), packVersion, packSize, contentKey, subPackName, contentId, scripting, raytracingCapable, addonPack, cdnUrl);
        }

        public String getPackIdString() {
            return packId.toString();
        }
    }

    @Value
    public static class CDNEntry {
        private final String packId;
        private final String remoteUrl;
    }
}
