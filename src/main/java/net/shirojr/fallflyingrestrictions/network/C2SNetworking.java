package net.shirojr.fallflyingrestrictions.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.config.structure.FeatureToggleData;
import net.shirojr.fallflyingrestrictions.config.structure.FlyingBlockHeightData;
import net.shirojr.fallflyingrestrictions.config.structure.WarningData;

@SuppressWarnings("unused")
public class C2SNetworking {

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ChannelIdentifiers.CONFIG_UPDATE_REQUEST_C2S, C2SNetworking::handleServerConfigUpdateRequest);
    }

    public static void sendServerConfigUpdateRequest() {
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(ChannelIdentifiers.CONFIG_UPDATE_REQUEST_C2S, buf);
    }

    private static void handleServerConfigUpdateRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            PacketByteBuf requestedBuf = PacketByteBufs.create();

            WarningData.toPacketByteBuf(requestedBuf, ConfigInit.CONFIG.displayWarning);
            FeatureToggleData.toPacketByteBuf(requestedBuf, ConfigInit.CONFIG.toggleFeatures);
            FlyingBlockHeightData.toPacketByteBuf(requestedBuf, ConfigInit.CONFIG.restrictionValues);

            ServerPlayNetworking.send(player, ChannelIdentifiers.CONFIG_UPDATE_RESPONSE_S2C, requestedBuf);
        });
    }
}
