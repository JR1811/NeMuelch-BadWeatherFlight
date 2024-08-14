package net.shirojr.fallflyingrestrictions.config.structure;

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
}
