package net.shirojr.fallflyingrestrictions.event;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.shirojr.fallflyingrestrictions.command.ZoneRestrictionCommands;
import net.shirojr.fallflyingrestrictions.data.PersistentWorldData;
import net.shirojr.fallflyingrestrictions.data.VolumeData;
import net.shirojr.fallflyingrestrictions.data.shape.Volume;
import net.shirojr.fallflyingrestrictions.network.ChannelIdentifiers;
import net.shirojr.fallflyingrestrictions.network.S2CNetworking;

public class CommonEvents {
    static {
        handlePlayerJoinEvent();
        handleSleepEvent();
        handleCommandRegistrationEvents();
    }

    private static void handleCommandRegistrationEvents() {
        CommandRegistrationCallback.EVENT.register(ZoneRestrictionCommands::register);
    }

    private static void handlePlayerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            S2CNetworking.sendServerConfigUpdateResponse(handler.player);

            PersistentWorldData persistentWorldData = PersistentWorldData.getServerState(server, handler.player.world.getRegistryKey());
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(persistentWorldData.getNoFlyingZones().size());

            for (VolumeData data : persistentWorldData.getNoFlyingZones()) {
                Identifier identifier = data.identifier();
                Volume volume = data.volume();

                buf.writeIdentifier(identifier);
                volume.toPacketByteBuf(buf);
            }
            ServerPlayNetworking.send(handler.player, ChannelIdentifiers.UPDATE_ZONE_CACHE_S2C, buf);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                ServerPlayNetworking.send(handler.player, ChannelIdentifiers.CLEAR_ZONE_CACHE_S2C, PacketByteBufs.empty())
        );
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
