package net.shirojr.fallflyingrestrictions.network;

import net.minecraft.util.Identifier;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictions;

public class ChannelIdentifiers {
    public static final Identifier CONFIG_UPDATE_REQUEST_C2S = new Identifier(FallFlyingRestrictions.MOD_ID, "c2s_config_request");
    public static final Identifier CONFIG_UPDATE_RESPONSE_S2C = new Identifier(FallFlyingRestrictions.MOD_ID, "s2c_config_response");
    public static final Identifier UPDATE_ZONE_CACHE_S2C = new Identifier(FallFlyingRestrictions.MOD_ID, "s2c_zone_cache_update");
    public static final Identifier CLEAR_ZONE_CACHE_S2C = new Identifier(FallFlyingRestrictions.MOD_ID, "s2c_zone_cache_clear");

}
