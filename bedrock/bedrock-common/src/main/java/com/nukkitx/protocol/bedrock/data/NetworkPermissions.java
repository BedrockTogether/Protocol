package com.nukkitx.protocol.bedrock.data;

import lombok.Data;

/**
 * Stores server authoritative flags that have been set on the server.
 */
@Data
public class NetworkPermissions {

    public static final NetworkPermissions DEFAULT = new NetworkPermissions(false);

    private final boolean serverAuthSounds;
}
