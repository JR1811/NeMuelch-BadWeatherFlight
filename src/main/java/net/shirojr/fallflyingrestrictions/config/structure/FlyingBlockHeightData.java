package net.shirojr.fallflyingrestrictions.config.structure;

import net.minecraft.network.PacketByteBuf;

@SuppressWarnings("FieldMayBeFinal")
public class FlyingBlockHeightData {
    private double badWeatherDownForce;
    private double flyingTooHighDownForce;

    private int safeAboveGroundHeight;
    private int flyingHeightLimit;

    public FlyingBlockHeightData() {
        this.badWeatherDownForce = 0.05;
        this.safeAboveGroundHeight = 5;
        this.flyingTooHighDownForce = 0.08;
        this.flyingHeightLimit = 300;
    }

    public double getBadWeatherDownForce() {
        return badWeatherDownForce;
    }

    public double getFlyingTooHighDownForce() {
        return flyingTooHighDownForce;
    }

    public int getSafeAboveGroundHeight() {
        return safeAboveGroundHeight;
    }

    public int getFlyingHeightLimit() {
        return flyingHeightLimit;
    }

    public static FlyingBlockHeightData fromPacketByteBuf(PacketByteBuf buf) {
        FlyingBlockHeightData data = new FlyingBlockHeightData();
        data.badWeatherDownForce = buf.readDouble();
        data.flyingTooHighDownForce = buf.readDouble();
        data.safeAboveGroundHeight = buf.readInt();
        data.flyingHeightLimit = buf.readInt();
        return data;
    }

    public static void toPacketByteBuf(PacketByteBuf buf, FlyingBlockHeightData data) {
        buf.writeDouble(data.badWeatherDownForce);
        buf.writeDouble(data.flyingTooHighDownForce);
        buf.writeInt(data.safeAboveGroundHeight);
        buf.writeInt(data.flyingHeightLimit);
    }
}
