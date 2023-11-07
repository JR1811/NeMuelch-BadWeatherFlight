package net.shirojr.fallflyingrestrictions.config.structure;

@SuppressWarnings({"FieldMayBeFinal"})
public class WarningData {
    private boolean modifiedMovement;
    private boolean blockedInventory;
    public WarningData() {
        this.modifiedMovement = true;
        this.blockedInventory = true;
    }

    public boolean enabledMovementWanring() {
        return modifiedMovement;
    }

    public boolean enabledBlockedInventoryWarning() {
        return blockedInventory;
    }
}
