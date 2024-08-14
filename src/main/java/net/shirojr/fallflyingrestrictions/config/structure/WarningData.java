package net.shirojr.fallflyingrestrictions.config.structure;

@SuppressWarnings({"FieldMayBeFinal"})
public class WarningData {
    private boolean modifiedMovement;
    private boolean blockedInventory;
    private boolean blockedEating;

    public WarningData() {
        this.modifiedMovement = true;
        this.blockedInventory = true;
        this.blockedEating = true;
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
}
