package net.shirojr.fallflyingrestrictions.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.shirojr.fallflyingrestrictions.network.S2CNetworking;

public class CommonEvents {
    static {
        handlePlayerJoinEvent();
        handleSleepEvent();
    }

    private static void handlePlayerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                S2CNetworking.sendServerConfigUpdateResponse(handler.player));
    }

    private static void handleSleepEvent() {
        EntitySleepEvents.START_SLEEPING.register((entity, sleepingPos) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;
            S2CNetworking.sendServerConfigUpdateResponse(player);
        });
    }

    public static void initialize() {
    }
}
