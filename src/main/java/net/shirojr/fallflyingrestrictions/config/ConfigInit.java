package net.shirojr.fallflyingrestrictions.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class ConfigInit {
    public static FallFlyingRestrictionsConfig CONFIG = new FallFlyingRestrictionsConfig();

    public static void init() {
        AutoConfig.register(FallFlyingRestrictionsConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FallFlyingRestrictionsConfig.class).getConfig();
    }
}
