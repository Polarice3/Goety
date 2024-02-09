package com.Polarice3.Goety.common.magic.spells.necromancy;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class SkeletonSpell extends SummonSpells {

    public int defaultSoulCost() {
        return SpellConfig.SkeletonCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.SkeletonDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.SkeletonSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.SkeletonCoolDown.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        this.commonResult(worldIn, entityLiving);
        int i = 1;
        if (staff){
            i = 2 + entityLiving.level.random.nextInt(4);
        }
        if (!isShifting(entityLiving)) {
            for (int i1 = 0; i1 < i; ++i1) {
                BlockPos blockPos = BlockFinder.SummonRadius(entityLiving, worldIn);
                AbstractSMEntity summonedentity = new SkeletonMinionEntity(ModEntityType.SKELETON_MINION.get(), worldIn);
                if (worldIn.getBiome(blockPos).getBiomeCategory() == Biome.Category.ICY && worldIn.canSeeSky(blockPos)){
                    summonedentity = new StrayMinionEntity(ModEntityType.STRAY_MINION.get(), worldIn);
                } else if (BlockFinder.findStructure(worldIn, entityLiving, Structure.PILLAGER_OUTPOST)){
                    summonedentity = new SkeletonPillagerEntity(ModEntityType.SKELETON_PILLAGER.get(), worldIn);
                } else if (worldIn.getBiome(blockPos).getBiomeCategory() == Biome.Category.JUNGLE && worldIn.random.nextBoolean()){
                    summonedentity = new MossySkeletonMinionEntity(ModEntityType.MOSSY_SKELETON_MINION.get(), worldIn);
                } else if (entityLiving.isUnderWater() && worldIn.isWaterAt(blockPos)){
                    summonedentity = new SunkenSkeletonMinion(ModEntityType.SUNKEN_SKELETON_MINION.get(), worldIn);
                }
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                if (summonedentity.getType() != ModEntityType.SUNKEN_SKELETON_MINION.get()){
                    MobUtil.moveDownToGround(summonedentity);
                }
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setArrowPower(enchantment);
                summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                this.SummonSap(entityLiving, summonedentity);
                this.setTarget(worldIn, entityLiving, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(entityLiving, summonedentity);
            }
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
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
                if (entity instanceof SkeletonMinionEntity) {
                    if (((SkeletonMinionEntity) entity).getTrueOwner() == entityLiving) {
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
}
