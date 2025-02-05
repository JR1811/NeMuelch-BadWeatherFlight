package net.shirojr.fallflyingrestrictions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictionsClient;
import net.shirojr.fallflyingrestrictions.config.ConfigInit;
import net.shirojr.fallflyingrestrictions.data.PersistentWorldData;
import net.shirojr.fallflyingrestrictions.data.VolumeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityZoneRestrictionsMixin {
    @ModifyExpressionValue(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ElytraItem;isUsable(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean isInNoFlyingZone(boolean original) {
        if (!original) return false;
        LivingEntity entity = (LivingEntity) (Object) this;
        BlockPos pos = entity.getBlockPos();
        if (entity.getWorld().isClient()) {
            if (!FallFlyingRestrictionsClient.isClientPlayer(entity)) return original;
            boolean interrupt = PersistentWorldData.interruptFlying(pos, FallFlyingRestrictionsClient.CACHED_ZONES);
            if (interrupt && entity instanceof PlayerEntity player && ConfigInit.CONFIG.displayWarning.enabledZoneFlying()) {
                player.sendMessage(new TranslatableText("notification.fallflyingrestrictions.restricted_zone.interrupt_flying"), true);
            }
            return !interrupt;
        } else {
            MinecraftServer server = entity.getServer();
            if (server == null) return original;
            List<VolumeData> zones = PersistentWorldData.getServerState(server, entity.getWorld().getRegistryKey()).getNoFlyingZones();
            boolean interrupt = PersistentWorldData.interruptFlying(pos, zones);
            if (interrupt && entity instanceof PlayerEntity player && ConfigInit.CONFIG.displayWarning.enabledZoneFlying()) {
                player.sendMessage(new TranslatableText("notification.fallflyingrestrictions.restricted_zone.interrupt_flying"), true);
            }
            return !interrupt;
        }
    }
}
