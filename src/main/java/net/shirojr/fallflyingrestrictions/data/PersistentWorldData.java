package net.shirojr.fallflyingrestrictions.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.shirojr.fallflyingrestrictions.FallFlyingRestrictions;
import net.shirojr.fallflyingrestrictions.data.shape.BoxShape;
import net.shirojr.fallflyingrestrictions.data.shape.SphereShape;
import net.shirojr.fallflyingrestrictions.data.shape.Volume;
import net.shirojr.fallflyingrestrictions.network.ChannelIdentifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PersistentWorldData extends PersistentState {
    private final List<VolumeData> noFlyingZones = new ArrayList<>();

    public void modifyNoFlyingZones(Consumer<List<VolumeData>> zones, MinecraftServer server) {
        zones.accept(this.noFlyingZones);
        PlayerLookup.all(server).forEach(player -> {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeVarInt(this.noFlyingZones.size());

                    for (VolumeData data : this.noFlyingZones) {
                        Identifier identifier = data.identifier();
                        Volume volume = data.volume();

                        buf.writeIdentifier(identifier);
                        volume.toPacketByteBuf(buf);
                    }
                    ServerPlayNetworking.send(player, ChannelIdentifiers.UPDATE_ZONE_CACHE_S2C, buf);
                }
        );
    }

    public List<VolumeData> getNoFlyingZones() {
        return Collections.unmodifiableList(noFlyingZones);
    }

    public static boolean canStartFlying(BlockPos pos, List<VolumeData> list) {
        for (VolumeData data : list) {
            if (data.volume().contains(pos) && data.volume().preventStartFlying()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean interruptFlying(BlockPos pos, List<VolumeData> list) {
        for (VolumeData entry : list) {
            if (entry.volume().contains(pos) && entry.volume().interruptFlying()) {
                return true;
            }
        }
        return false;
    }

    public static PersistentWorldData fromNbt(NbtCompound nbt) {
        PersistentWorldData persistentData = new PersistentWorldData();

        NbtCompound noFlyingZonesNbt = nbt.getCompound("noFlyingZones");
        for (String key : noFlyingZonesNbt.getKeys()) {
            Identifier identifier = new Identifier(key);
            NbtCompound shapeContent = noFlyingZonesNbt.getCompound(key);

            if (identifier.equals(BoxShape.IDENTIFIER)) {
                persistentData.noFlyingZones.add(new VolumeData(identifier, BoxShape.fromNbt(shapeContent)));
            } else if (identifier.equals(SphereShape.IDENTIFIER)) {
                persistentData.noFlyingZones.add(new VolumeData(identifier, SphereShape.fromNbt(shapeContent)));
            }
        }
        persistentData.markDirty();
        return persistentData;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound noFlyingZonesNbt = new NbtCompound();
        for (var entry : this.noFlyingZones) {
            noFlyingZonesNbt.put(entry.identifier().toString(), entry.volume().toNbt());
        }
        nbt.put("noFlyingZones", noFlyingZonesNbt);
        return nbt;
    }


    public static PersistentWorldData getServerState(MinecraftServer server, RegistryKey<World> worldKey) {
        ServerWorld world = server.getWorld(worldKey);
        if (world == null) {
            throw new RuntimeException("Couldn't load [%s] for persistent state. (%s)".formatted(worldKey.getValue().toString(), FallFlyingRestrictions.MOD_ID));
        }
        PersistentStateManager manager = world.getPersistentStateManager();
        PersistentWorldData data = manager.getOrCreate(PersistentWorldData::fromNbt, PersistentWorldData::new, FallFlyingRestrictions.MOD_ID);
        data.markDirty();
        return data;
    }
}
