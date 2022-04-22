package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.IllusionCloneEntity;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class IllusionSpell extends Spells{
    public int SoulCost() {
        return MainConfig.IllusionCost.get();
    }

    public int CastDuration() {
        return MainConfig.IllusionDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        List<IllusionCloneEntity> number = new ArrayList<>();
        if (!worldIn.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            for (Entity entity : serverWorld.getAllEntities()) {
                if (entity instanceof IllusionCloneEntity) {
                    if (((IllusionCloneEntity) entity).getTrueOwner() == entityLiving) {
                        number.add((IllusionCloneEntity) entity);
                        if (number.size() > 4){
                            ((IllusionCloneEntity) entity).die(DamageSource.STARVE);
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 4; ++i1) {
            IllusionCloneEntity summonedentity = new IllusionCloneEntity(ModEntityType.ILLUSION_CLONE.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(1200);
            summonedentity.setPersistenceRequired();
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                new ParticleUtil(ParticleTypes.CLOUD, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.IncreaseInfamy(MainConfig.IllusionInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        List<IllusionCloneEntity> number = new ArrayList<>();
        if (!worldIn.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            for (Entity entity : serverWorld.getAllEntities()) {
                if (entity instanceof IllusionCloneEntity) {
                    if (((IllusionCloneEntity) entity).getTrueOwner() == entityLiving) {
                        number.add((IllusionCloneEntity) entity);
                        if (number.size() > 4){
                            ((IllusionCloneEntity) entity).die(DamageSource.STARVE);
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 4 + entityLiving.level.random.nextInt(4); ++i1) {
            IllusionCloneEntity summonedentity = new IllusionCloneEntity(ModEntityType.ILLUSION_CLONE.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(1200);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(true);
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                new ParticleUtil(ParticleTypes.CLOUD, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.IncreaseInfamy(MainConfig.IllusionInfamyChance.get(), (PlayerEntity) entityLiving);
    }

}
