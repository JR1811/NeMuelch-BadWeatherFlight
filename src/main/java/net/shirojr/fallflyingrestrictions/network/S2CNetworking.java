package net.shirojr.fallflyingrestrictions.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.config.structure.FeatureToggleData;
import net.shirojr.fallflyingrestrictions.config.structure.FlyingBlockHeightData;
import net.shirojr.fallflyingrestrictions.config.structure.WarningData;

public class S2CNetworking {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ChannelIdentifiers.CONFIG_UPDATE_RESPONSE_CHANNEL, S2CNetworking::handleServerConfigUpdateResponse);
    }

    public static void sendServerConfigUpdateResponse(ServerPlayerEntity target) {
        PacketByteBuf buf = PacketByteBufs.create();

        WarningData.toPacketByteBuf(buf, ConfigInit.CONFIG.displayWarning);
        FeatureToggleData.toPacketByteBuf(buf, ConfigInit.CONFIG.toggleFeatures);
        FlyingBlockHeightData.toPacketByteBuf(buf, ConfigInit.CONFIG.restrictionValues);

        ServerPlayNetworking.send(target, ChannelIdentifiers.CONFIG_UPDATE_RESPONSE_CHANNEL, buf);
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
