package net.shirojr.fallflyingrestrictions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictionsClient;
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
            if (!entity.equals(MinecraftClient.getInstance().player)) return original;
            return !PersistentWorldData.interruptFlying(pos, FallFlyingRestrictionsClient.CACHED_ZONES);
        } else {
            MinecraftServer server = entity.getServer();
            if (server == null) return original;
            List<VolumeData> zones = PersistentWorldData.getServerState(server, entity.getWorld().getRegistryKey()).getNoFlyingZones();
            return !PersistentWorldData.interruptFlying(pos, zones);
        }
    }
}
