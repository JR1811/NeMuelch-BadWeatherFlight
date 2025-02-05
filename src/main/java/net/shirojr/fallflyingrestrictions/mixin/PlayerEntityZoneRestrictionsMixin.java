package net.shirojr.fallflyingrestrictions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictionsClient;
import net.shirojr.fallflyingrestrictions.data.PersistentWorldData;
import net.shirojr.fallflyingrestrictions.data.VolumeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityZoneRestrictionsMixin {
    @ModifyExpressionValue(method = "checkFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ElytraItem;isUsable(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean checkNoFlyingZones(boolean original) {
        if (!original) return false;
        PlayerEntity player = (PlayerEntity) (Object) this;
        BlockPos pos = player.getBlockPos();
        if (player.getWorld().isClient()) {
            return PersistentWorldData.canStartFlying(pos, FallFlyingRestrictionsClient.CACHED_ZONES);
        } else {
            MinecraftServer server = player.getServer();
            if (server == null) return original;
            List<VolumeData> zones = PersistentWorldData.getServerState(server, player.getWorld().getRegistryKey()).getNoFlyingZones();
            return PersistentWorldData.canStartFlying(pos, zones);
        }
    }
}
