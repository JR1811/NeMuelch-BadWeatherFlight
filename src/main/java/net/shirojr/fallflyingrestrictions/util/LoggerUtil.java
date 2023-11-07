package net.shirojr.fallflyingrestrictions.util;

import net.fabricmc.loader.api.FabricLoader;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictions;
import org.jetbrains.annotations.Nullable;

public class LoggerUtil {

    private LoggerUtil() {
        // This is just a helper class.
        // No objects for you, haha!
    }

    /**
     * Overlaod method of {@link LoggerUtil#devLogger(String, boolean, Exception) devLogger}
     *
     * @param input Text to print in the console.
     */
    public static void devLogger(String input) {
        devLogger(input, false, null);
    }

    /**
     * Prints text to the console, only if the Minecraft instance is running in a developer environment.
     *
     * @param input   Text to print in the console.
     * @param isError Enables red highlighting in the console and marks it as an error.
     * @param e       Add additional error information. This may be null if no Exception is available.
     */
    public static void devLogger(String input, boolean isError, @Nullable Exception e) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
        String message = String.format("DEV - [%s]", input);

        if (!isError) FallFlyingRestrictions.LOGGER.info(message);
        else if (e == null) FallFlyingRestrictions.LOGGER.error(message);
        else FallFlyingRestrictions.LOGGER.error(message, e);
    }
}
