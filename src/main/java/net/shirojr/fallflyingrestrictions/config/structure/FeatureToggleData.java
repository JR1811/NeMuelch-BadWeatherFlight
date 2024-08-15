package net.shirojr.fallflyingrestrictions.config.structure;
@SuppressWarnings({"FieldMayBeFinal"})
public class FeatureToggleData {
    private boolean movementChanges, flightHeightAboveGroundSafety, flyingTooHigh, roofAboveHeadSafety, inventoryBlock, eatingBlock;

    public FeatureToggleData() {
        this.movementChanges = true;
        this.flightHeightAboveGroundSafety = true;
        this.flyingTooHigh = false;
        this.roofAboveHeadSafety = true;
        this.inventoryBlock = true;
        this.eatingBlock = false;
    }

    public boolean enabledMovementChanges() {
        return movementChanges;
    }

    public boolean enabledSafeFlightHeight() {
        return flightHeightAboveGroundSafety;
    }

    public boolean enabledFlyingTooHigh() {
        return flyingTooHigh;
    }

    public boolean enabledRoofAboveHeadSafety() {
        return roofAboveHeadSafety;
    }

    public boolean enabledInventoryBlock() {
        return inventoryBlock;
    }

    public boolean enabledEatingWhileFlyingBlock() {
        return eatingBlock;
    }
}
