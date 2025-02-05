package net.shirojr.fallflyingrestrictions;

import net.fabricmc.api.ClientModInitializer;
import net.shirojr.fallflyingrestrictions.data.VolumeData;
import net.shirojr.fallflyingrestrictions.network.S2CNetworking;

import java.util.ArrayList;
import java.util.List;

public class FallFlyingRestrictionsClient implements ClientModInitializer {
    public static List<VolumeData> CACHED_ZONES = new ArrayList<>();


    @Override
    public void onInitializeClient() {
        S2CNetworking.registerS2CPackets();
    }
}
