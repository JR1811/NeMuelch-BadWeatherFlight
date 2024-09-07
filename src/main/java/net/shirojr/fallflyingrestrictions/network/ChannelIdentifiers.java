package net.shirojr.fallflyingrestrictions.network;

import net.minecraft.util.Identifier;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictions;

public class ChannelIdentifiers {
    public static final Identifier CONFIG_UPDATE_REQUEST_CHANNEL = new Identifier(FallFlyingRestrictions.MOD_ID, "s2c_config_request");
    public static final Identifier CONFIG_UPDATE_RESPONSE_CHANNEL = new Identifier(FallFlyingRestrictions.MOD_ID, "s2c_config_response");

}
