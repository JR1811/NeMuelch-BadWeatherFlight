package net.shirojr.fallflyingrestrictions.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.util.LoggerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(method = "travel",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V")
    )
    private void badweatherflight$badWeatherFlying(LivingEntity instance, Vec3d vec3d) {
        if (shouldApplyDefaultValue(instance) || isOptimalFlyingCondition(instance)) {
            LoggerUtil.devLogger("applying normal flight | " + instance.getWorld());
            instance.setVelocity(vec3d);
            return;
        }
        LoggerUtil.devLogger(String.format("applying bad condition flight | %s", instance.getWorld()));
        if (ConfigInit.CONFIG.displayWarning.enabledMovementWanring() && instance instanceof ServerPlayerEntity player) {
            player.sendMessage(new TranslatableText("notification.fallflyingrestrictions.bad_flying_condition"), true);
        }
        Vec3d downForce = new Vec3d(0.0, -(ConfigInit.CONFIG.downForce), 0.0);
        instance.setVelocity(vec3d.add(downForce));
    }

    /**
     * Utility method which considers config entries, type of entity and game mode.
     *
     * @return true, if <b><i>no</i></b> changes to the fall-flying movement should be done.
     */
    @Unique
    private static boolean shouldApplyDefaultValue(LivingEntity instance) {
        if (!ConfigInit.CONFIG.toggleFeatures.enabledMovementChanges()) return true;
        if (!(instance instanceof PlayerEntity player)) return true;
        if (player.getAbilities().creativeMode || player.isSpectator()) return true;
        return false;
    }

    /**
     * Utility method which considers weather, safe flight height and a clear view into the sky.
     *
     * @return true, if flying condition are optimal.
     */
    @Unique
    private static boolean isOptimalFlyingCondition(LivingEntity livingEntity) {
        World world = livingEntity.getWorld();
        BlockPos livingEntityPos = livingEntity.getBlockPos();

        boolean doRoofSafetyCheck = ConfigInit.CONFIG.toggleFeatures.enabledRoofAboveHeadSafety();
        boolean doHeightSafetyCheck = ConfigInit.CONFIG.toggleFeatures.enabledSafeFlightHeight();
        int safeBlockHeight = Math.max(ConfigInit.CONFIG.safeBlockHeight, 1);

        if (!world.isThundering() && !world.isRaining()) return true;
        if (!world.isSkyVisible(livingEntityPos) && doRoofSafetyCheck) return true;
        if (!doHeightSafetyCheck) return false;

        boolean isSafeHeight = false;
        for (int i = 0; i < safeBlockHeight; i++) {
            if (world.getBlockState(livingEntityPos).getBlock() != Blocks.AIR) {
                isSafeHeight = true;
                break;
            }
            livingEntityPos = livingEntityPos.down();
        }
        return isSafeHeight;
    }
}