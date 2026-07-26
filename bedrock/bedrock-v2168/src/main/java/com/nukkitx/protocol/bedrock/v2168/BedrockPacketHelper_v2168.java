package com.nukkitx.protocol.bedrock.v2168;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.util.Preconditions;
import com.nukkitx.protocol.bedrock.data.GameRuleData;
import com.nukkitx.protocol.bedrock.v622.BedrockPacketHelper_v622;
import io.netty.buffer.ByteBuf;

public class BedrockPacketHelper_v2168 extends BedrockPacketHelper_v622 {
    public static final BedrockPacketHelper_v2168 INSTANCE = new BedrockPacketHelper_v2168();

    /**
     * Integer game rule values are written as a little endian int instead of a
     * varint since 1.26.40. StartGame and GameRulesChanged share this encoding.
     */
    @Override
    public void writeGameRule(ByteBuf buffer, GameRuleData<?> gameRule) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(gameRule, "gameRule");

        Object value = gameRule.getValue();
        int type = this.gameRuleTypes.getInt(value.getClass());

        writeString(buffer, gameRule.getName());
        buffer.writeBoolean(gameRule.isEditable());
        VarInts.writeUnsignedInt(buffer, type);
        switch (type) {
            case 1:
                buffer.writeBoolean((boolean) value);
                break;
            case 2:
                buffer.writeIntLE((int) value);
                break;
            case 3:
                buffer.writeFloatLE((float) value);
                break;
        }
    }

    @Override
    public GameRuleData<?> readGameRule(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");

        String name = readString(buffer);
        boolean editable = buffer.readBoolean();
        int type = VarInts.readUnsignedInt(buffer);

        switch (type) {
            case 1:
                return new GameRuleData<>(name, editable, buffer.readBoolean());
            case 2:
                return new GameRuleData<>(name, editable, buffer.readIntLE());
            case 3:
                return new GameRuleData<>(name, editable, buffer.readFloatLE());
        }
        throw new IllegalStateException("Invalid gamerule type received");
    }
}
