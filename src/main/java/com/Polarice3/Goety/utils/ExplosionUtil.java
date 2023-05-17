package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SLootingExplosionPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ExplosionUtil {

    public static DeadSandExplosion deadSandExplode(World world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, DeadSandExplosion.Mode pBlockInteraction) {
        DeadSandExplosion explosion = new DeadSandExplosion(world, pExploder, pX, pY, pZ, pSize, pBlockInteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
        return explosion;
    }

    public static LootingExplosion lootExplode(World world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, boolean pCausesFire, Explosion.Mode pMode, LootingExplosion.Mode pLootMode) {
        LootingExplosion explosion = new LootingExplosion(world, pExploder, pX, pY, pZ, pSize, pCausesFire, pMode, pLootMode);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.explode();
        if (world instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) world;
            explosion.finalizeExplosion(false);
            for (ServerPlayerEntity serverplayer : serverLevel.getPlayers((p_147157_) -> {
                return p_147157_.distanceToSqr(pX, pY, pZ) < 4096.0D;
            })) {
                ModNetwork.sendTo(serverplayer, new SLootingExplosionPacket(pX, pY, pZ, pSize, explosion.getHitPlayers().get(serverplayer)));
            }
        } else {
            explosion.finalizeExplosion(true);
        }
        return explosion;
    }

    public static FrostExplosion frostExplode(World world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, Explosion.Mode pMode) {
        FrostExplosion explosion = new FrostExplosion(world, pExploder, pX, pY, pZ, pSize, pMode);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.iceExplosion();
        return explosion;
    }
}
