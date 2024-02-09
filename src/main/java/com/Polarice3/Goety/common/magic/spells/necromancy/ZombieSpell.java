package com.Polarice3.Goety.common.magic.spells.necromancy;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinBruteMinionEntity;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinMinionEntity;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModMathHelper;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class ZombieSpell extends SummonSpells {

    public int defaultSoulCost() {
        return SpellConfig.ZombieCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.ZombieDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.ZombieSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.ZombieCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NECROMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (WandUtil.enchantedFocus(entityLiving)){
            enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
            duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), entityLiving) + 1;
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof ZombieMinionEntity) {
                    if (((ZombieMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            int i = 1;
            if (staff){
                i = 2 + entityLiving.level.random.nextInt(4);
            }
            for (int i1 = 0; i1 < i; ++i1) {
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
                } else if (BlockFinder.findStructure(worldIn, entityLiving, Structure.WOODLAND_MANSION)){
                    summonedentity = new ZombieVindicatorEntity(ModEntityType.ZOMBIE_VINDICATOR.get(), worldIn);
                } else if (worldIn.getBiome(blockPos).getTemperature(blockPos) < 0.15F){
                    summonedentity = new FrozenZombieMinionEntity(ModEntityType.FROZEN_ZOMBIE_MINION.get(), worldIn);
                } else if (worldIn.getBiome(blockPos).getBiomeCategory() == Biome.Category.JUNGLE && worldIn.random.nextBoolean()){
                    summonedentity = new JungleZombieMinionEntity(ModEntityType.JUNGLE_ZOMBIE_MINION.get(), worldIn);
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
                this.setTarget(worldIn, entityLiving, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(entityLiving, summonedentity);
            }
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
