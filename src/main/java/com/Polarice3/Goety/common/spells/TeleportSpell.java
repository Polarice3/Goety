package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class TeleportSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return MainConfig.TeleportCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.ENDERMAN_TELEPORT;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PlayerEntity player = (PlayerEntity) entityLiving;
        int enchantment = 0;
        if (WandUtil.enchantedFocus(player)){
            enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
        }
        RayTraceResult trace = player.pick(32 + enchantment, 0, true);
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) trace;
        Direction face = blockRayTraceResult.getDirection();
        BlockPos newPos = blockRayTraceResult.getBlockPos().relative(face);
        enderTeleportEvent(entityLiving, worldIn, newPos);
        worldIn.sendParticles(ParticleTypes.PORTAL, entityLiving.getX(), entityLiving.getY() + worldIn.random.nextDouble() * 2.0D, entityLiving.getZ(), 0, worldIn.random.nextGaussian(), 0.0D, worldIn.random.nextGaussian(), 0.5F);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.TeleportInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PlayerEntity player = (PlayerEntity) entityLiving;
        int enchantment = 0;
        if (WandUtil.enchantedFocus(player)){
            enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
        }
        RayTraceResult trace = player.pick(64 + enchantment, 0, true);
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) trace;
        Direction face = blockRayTraceResult.getDirection();
        BlockPos newPos = blockRayTraceResult.getBlockPos().relative(face);
        enderTeleportEvent(entityLiving, worldIn, newPos);
        worldIn.sendParticles(ParticleTypes.PORTAL, entityLiving.getX(), entityLiving.getY() + worldIn.random.nextDouble() * 2.0D, entityLiving.getZ(), 0, worldIn.random.nextGaussian(), 0.0D, worldIn.random.nextGaussian(), 0.5F);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.TeleportInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public static void enderTeleportEvent(LivingEntity player, World world, BlockPos target) {
        enderTeleportEvent(player, world, target.getX() + .5F, target.getY() + .5F, target.getZ() + .5F);
    }

    private static void enderTeleportEvent(LivingEntity player, World world, double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0);
        boolean wasCancelled = MinecraftForge.EVENT_BUS.post(event);
        if (!wasCancelled) {
            teleportWallSafe(player, world, event.getTargetX(), event.getTargetY(), event.getTargetZ());
        }
    }

    private static void teleportWallSafe(LivingEntity player, World world, double x, double y, double z) {
        BlockPos coords = new BlockPos(x, y, z);
        world.getChunk(coords).setUnsaved(true);
        player.moveTo(x, y, z);
        moveEntityWallSafe(player, world);
    }

    public static void moveEntityWallSafe(Entity entity, World world) {
        while (!world.noCollision(entity)) {
            entity.moveTo(entity.xo, entity.yo + 1.0D, entity.zo);
        }
    }

}
