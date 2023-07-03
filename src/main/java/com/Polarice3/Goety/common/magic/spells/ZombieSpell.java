package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.DrownedMinionEntity;
import com.Polarice3.Goety.common.entities.ally.HuskMinionEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinBruteMinionEntity;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinMinionEntity;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModMathHelper;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

public class ZombieSpell extends SummonSpells {

    public int SoulCost() {
        return SpellConfig.ZombieCost.get();
    }

    public int CastDuration() {
        return SpellConfig.ZombieDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.ZombieCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.NECROMANCY;
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof ZombieMinionEntity) {
                    if (((ZombieMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
                if (entity instanceof ZPiglinMinionEntity){
                    if (((ZPiglinMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            SummonedEntity summonedentity;
            BlockPos blockPos = BlockFinder.SummonRadius(entityLiving, worldIn);
            if (entityLiving.isUnderWater()){
                blockPos = BlockFinder.SummonWaterRadius(entityLiving, worldIn);
            }
            if (entityLiving.isUnderWater() && worldIn.isWaterAt(blockPos)){
                summonedentity = new DrownedMinionEntity(ModEntityType.DROWNED_MINION.get(), worldIn);
            } else if (worldIn.getBiome(blockPos).getBiomeCategory() == Biome.Category.DESERT && worldIn.canSeeSky(blockPos)){
                summonedentity = new HuskMinionEntity(ModEntityType.HUSK_MINION.get(), worldIn);
            } else if (worldIn.dimension() == World.NETHER && SpellConfig.SummonZPiglins.get()){
                SummonedEntity summoned = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), worldIn);
                if (worldIn.random.nextFloat() <= 0.25F && BlockFinder.findStructure(worldIn, entityLiving, Structure.BASTION_REMNANT)){
                    summoned = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), worldIn);
                }
                summonedentity = summoned;
                entityLiving.setSecondsOnFire(15);
                entityLiving.addEffect(new EffectInstance(ModEffects.CURSED.get(), ModMathHelper.secondsToTicks(15)));
            } else {
                summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
            }
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(blockPos, 0.0F, 0.0F);
            if (summonedentity.getType() != ModEntityType.DROWNED_MINION.get()){
                MobUtil.moveDownToGround(summonedentity);
            }
            summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            if (enchantment > 0){
                int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost, false, false));
            }
            this.SummonSap(entityLiving, summonedentity);
            worldIn.addFreshEntity(summonedentity);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            this.SummonDown(entityLiving);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            for (int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
                SummonedEntity summonedentity;
                BlockPos blockPos = BlockFinder.SummonRadius(entityLiving, worldIn);
                if (entityLiving.isUnderWater()){
                    blockPos = BlockFinder.SummonWaterRadius(entityLiving, worldIn);
                }
                if (entityLiving.isUnderWater() && worldIn.isWaterAt(blockPos)){
                    summonedentity = new DrownedMinionEntity(ModEntityType.DROWNED_MINION.get(), worldIn);
                } else if (worldIn.getBiome(blockPos).getBiomeCategory() == Biome.Category.DESERT && worldIn.canSeeSky(blockPos)){
                    summonedentity = new HuskMinionEntity(ModEntityType.HUSK_MINION.get(), worldIn);
                } else if (worldIn.dimension() == World.NETHER && SpellConfig.SummonZPiglins.get()){
                    SummonedEntity summoned = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), worldIn);
                    if (worldIn.random.nextFloat() <= 0.25F && BlockFinder.findStructure(worldIn, entityLiving, Structure.BASTION_REMNANT)){
                        summoned = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), worldIn);
                    }
                    summonedentity = summoned;
                    entityLiving.setSecondsOnFire(15);
                    entityLiving.addEffect(new EffectInstance(ModEffects.CURSED.get(), ModMathHelper.secondsToTicks(15)));
                } else {
                    summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
                }
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                if (summonedentity.getType() != ModEntityType.DROWNED_MINION.get()){
                    MobUtil.moveDownToGround(summonedentity);
                }
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                if (enchantment > 0){
                    int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                    summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost, false, false));
                }
                this.SummonSap(entityLiving, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
            }
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }
}
