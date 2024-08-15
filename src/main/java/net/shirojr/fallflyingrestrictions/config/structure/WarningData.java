package net.shirojr.fallflyingrestrictions.config.structure;

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
}
