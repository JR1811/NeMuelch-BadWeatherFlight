package net.shirojr.fallflyingrestrictions.config.structure;

import net.minecraft.network.PacketByteBuf;

@SuppressWarnings({"FieldMayBeFinal"})
public class WarningData {
    private boolean modifiedMovement;
    private boolean flyingTooHigh;
    private boolean blockedInventory;
    private boolean blockedEating;

    public WarningData() {
        this.modifiedMovement = true;
        this.blockedInventory = true;
        this.blockedEating = true;
        this.flyingTooHigh = true;
    }

    public boolean enabledMovementWarning() {
        return modifiedMovement;
    }

    public boolean enabledBlockedInventoryWarning() {
        return blockedInventory;
    }

    public boolean enabledEatingWhileFlyingWarning() {
        return blockedEating;
    }

    public boolean enabledFlyingTooHighWarning() {
        return flyingTooHigh;
    }

    public static WarningData fromPacketByteBuf(PacketByteBuf buf) {
        WarningData data = new WarningData();
        data.modifiedMovement = buf.readBoolean();
        data.flyingTooHigh = buf.readBoolean();
        data.blockedInventory = buf.readBoolean();
        data.blockedEating = buf.readBoolean();
        return data;
    }

    public static void toPacketByteBuf(PacketByteBuf buf, WarningData data) {
        buf.writeBoolean(data.modifiedMovement);
        buf.writeBoolean(data.flyingTooHigh);
        buf.writeBoolean(data.blockedInventory);
        buf.writeBoolean(data.blockedEating);
    }
}
