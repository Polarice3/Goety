package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.network.EntityUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.UUID;

public class EntityFinder {
    public static Optional<ServerPlayerEntity> getPlayerByUuiDGlobal(UUID uuid) {
        for (ServerWorld world : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUUID(uuid);
            if (player != null)
                return Optional.of(player);
        }
        return Optional.empty();
    }

    public static Optional<? extends Entity> getEntityByUuiDGlobal(UUID uuid) {
        return getEntityByUuiDGlobal(ServerLifecycleHooks.getCurrentServer(), uuid);
    }

    public static Optional<? extends Entity> getEntityByUuiDGlobal(MinecraftServer server, UUID uuid) {
        if (uuid != null && server != null) {
            for (ServerWorld world : server.getAllLevels()) {
                Entity entity = world.getEntity(uuid);
                if (entity != null)
                    return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public static PlayerEntity getServerPlayersByUuiD(UUID uuid) {
        return getServerPlayersByUuiD(ServerLifecycleHooks.getCurrentServer(), uuid);
    }

    public static PlayerEntity getServerPlayersByUuiD(MinecraftServer server, UUID uuid){
        if (uuid != null && server != null) {
            for (ServerWorld world : server.getAllLevels()) {
                return world.getPlayerByUUID(uuid);
            }
        }
        return null;
    }

    public static LivingEntity getLivingEntityByUuiD(UUID uuid) {
        return getLivingEntityByUuiD(ServerLifecycleHooks.getCurrentServer(), uuid);
    }

    public static LivingEntity getLivingEntityByUuiD(MinecraftServer server, UUID uuid){
        if (uuid != null && server != null) {
            for (ServerWorld world : server.getAllLevels()) {
                Entity entity = world.getEntity(uuid);
                if (entity instanceof LivingEntity){
                    return (LivingEntity) entity;
                }
            }
        }
        return null;
    }

    public static Entity entityFromNBT(World world, CompoundNBT nbtTagCompound) {
        ResourceLocation typeId = new ResourceLocation(nbtTagCompound.getString("id"));

        Entity entity = ForgeRegistries.ENTITIES.getValue(typeId).create(world);
        entity.deserializeNBT(nbtTagCompound);
        return entity;
    }

    public static EntityType<?> entityTypeFromNbt(CompoundNBT nbtTagCompound) {
        ResourceLocation typeId = new ResourceLocation(nbtTagCompound.getString("id"));
        return ForgeRegistries.ENTITIES.getValue(typeId);
    }

    public static PlayerEntity getNearbyPlayer(World world, BlockPos blockPos){
        for (PlayerEntity player : world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(blockPos).inflate(256))){
            return player;
        }
        return null;
    }

    public static void sendEntityUpdatePacket(PlayerEntity player, LivingEntity livingEntity) {
        ModNetwork.sendTo(player, new EntityUpdatePacket(livingEntity.getUUID(), livingEntity.getPersistentData()));
    }
}
