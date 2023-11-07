package net.shirojr.fallflyingrestrictions.config.structure;

@SuppressWarnings({"FieldMayBeFinal"})
public class WarningData {
    private boolean modifiedMovement;
    private boolean blockedInventory;
    public WarningData(boolean modifiedMovement, boolean blockedInventory) {
        this.modifiedMovement = modifiedMovement;
        this.blockedInventory = blockedInventory;
    }

    public boolean enabledMovementWanring() {
        return modifiedMovement;
    }

    public boolean enabledBlockedInventoryWarning() {
        return blockedInventory;
    }
}
