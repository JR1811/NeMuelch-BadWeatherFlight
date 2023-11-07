package net.shirojr.fallflyingrestrictions;

import net.fabricmc.api.ModInitializer;

import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallFlyingRestrictions implements ModInitializer {
	public static final String MOD_ID = "fallflyingrestrictions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ConfigInit.init();

		LOGGER.info("Who likes flying anyways?");
		LoggerUtil.devLogger("Launched Instance in Developer Environment");
	}
}