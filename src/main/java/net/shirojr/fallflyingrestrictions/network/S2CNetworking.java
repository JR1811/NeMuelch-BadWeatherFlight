package net.shirojr.fallflyingrestrictions.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictionsClient;
import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.config.structure.FeatureToggleData;
import net.shirojr.fallflyingrestrictions.config.structure.FlyingBlockHeightData;
import net.shirojr.fallflyingrestrictions.config.structure.WarningData;
import net.shirojr.fallflyingrestrictions.data.VolumeData;
import net.shirojr.fallflyingrestrictions.data.shape.BoxShape;
import net.shirojr.fallflyingrestrictions.data.shape.SphereShape;
import net.shirojr.fallflyingrestrictions.data.shape.Volume;

import java.util.ArrayList;
import java.util.List;

public class S2CNetworking {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ChannelIdentifiers.CONFIG_UPDATE_RESPONSE_S2C, S2CNetworking::handleServerConfigUpdateResponse);
        ClientPlayNetworking.registerGlobalReceiver(ChannelIdentifiers.UPDATE_ZONE_CACHE_S2C, S2CNetworking::handleZoneCacheUpdate);
        ClientPlayNetworking.registerGlobalReceiver(ChannelIdentifiers.CLEAR_ZONE_CACHE_S2C, S2CNetworking::handleZoneCacheClear);
    }

    private static void handleZoneCacheClear(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        client.execute(() -> FallFlyingRestrictionsClient.CACHED_ZONES.clear());
    }

    private static void handleZoneCacheUpdate(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        int size = buf.readVarInt();
        List<VolumeData> data = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Identifier identifier = buf.readIdentifier();

            Volume volume = null;
            if (identifier.equals(BoxShape.IDENTIFIER)) {
                volume = BoxShape.fromPacketByteBuf(buf);
            } else if (identifier.equals(SphereShape.IDENTIFIER)) {
                volume = SphereShape.fromPacketByteBuf(buf);
            }
            if (volume == null) continue;

            data.add(new VolumeData(identifier, volume));
        }
        client.execute(() -> {
            FallFlyingRestrictionsClient.CACHED_ZONES.clear();
            FallFlyingRestrictionsClient.CACHED_ZONES.addAll(data);
        });
    }

    public static void sendServerConfigUpdateResponse(ServerPlayerEntity target) {
        PacketByteBuf buf = PacketByteBufs.create();

        WarningData.toPacketByteBuf(buf, ConfigInit.CONFIG.displayWarning);
        FeatureToggleData.toPacketByteBuf(buf, ConfigInit.CONFIG.toggleFeatures);
        FlyingBlockHeightData.toPacketByteBuf(buf, ConfigInit.CONFIG.restrictionValues);

        ServerPlayNetworking.send(target, ChannelIdentifiers.CONFIG_UPDATE_RESPONSE_S2C, buf);
    }

    private static void handleServerConfigUpdateResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {

        WarningData warningData = WarningData.fromPacketByteBuf(buf);
        FeatureToggleData toggleData = FeatureToggleData.fromPacketByteBuf(buf);
        FlyingBlockHeightData flyingBlockHeightData = FlyingBlockHeightData.fromPacketByteBuf(buf);

        client.execute(() -> {
            ConfigInit.CONFIG.displayWarning = warningData;
            ConfigInit.CONFIG.toggleFeatures = toggleData;
            ConfigInit.CONFIG.restrictionValues = flyingBlockHeightData;
        });
    }
}
