package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

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
        explosion.finalizeExplosion(true);
        return explosion;
    }
}
