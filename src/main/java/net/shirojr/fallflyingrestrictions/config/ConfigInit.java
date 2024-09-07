package net.shirojr.fallflyingrestrictions.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigInit {
    public static FallFlyingRestrictionsConfig CONFIG = new FallFlyingRestrictionsConfig();

    public static void initialize() {
        AutoConfig.register(FallFlyingRestrictionsConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FallFlyingRestrictionsConfig.class).getConfig();
    }
}
