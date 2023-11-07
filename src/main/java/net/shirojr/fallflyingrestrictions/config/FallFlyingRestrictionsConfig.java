package net.shirojr.fallflyingrestrictions.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.shirojr.fallflyingrestrictions.config.structure.FeatureToggleData;
import net.shirojr.fallflyingrestrictions.config.structure.WarningData;

@Config(name = "fallflyingrestrictions")
public class FallFlyingRestrictionsConfig implements ConfigData {
    public FeatureToggleData toggleFeatures = new FeatureToggleData();
    public WarningData displayWarning = new WarningData();
    public double downForce = 0.05;
    public int safeBlockHeight = 5;
}
