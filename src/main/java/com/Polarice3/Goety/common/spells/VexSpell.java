package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class VexSpell extends SummonSpells {
    private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public int SoulCost() {
        return SpellConfig.VexCost.get();
    }

    public int CastDuration() {
        return SpellConfig.VexDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.VexCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
            this.IncreaseInfamy(SpellConfig.VexInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof FriendlyVexEntity) {
                    if (((FriendlyVexEntity) entity).getTrueOwner() == entityLiving) {
                        entity.kill();
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving){
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
                for (int i1 = 0; i1 < 3; ++i1) {
                    BlockPos blockpos = entityLiving.blockPosition();
                    FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
                    vexentity.setOwnerId(entityLiving.getUUID());
                    vexentity.moveTo(blockpos, 0.0F, 0.0F);
                    vexentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                    vexentity.setBoundOrigin(blockpos);
                    if (SpellConfig.WandVexLimit.get() > VexLimit(entityLiving)) {
                        vexentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    } else {
                        vexentity.setLimitedLife(1);
                        vexentity.addEffect(new EffectInstance(Effects.WITHER, 800, 1));
                    }
                    if (enchantment > 0) {
                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(vexentity.getMainHandItem());
                        map.putIfAbsent(Enchantments.SHARPNESS, enchantment);
                        EnchantmentHelper.setEnchantments(map, vexentity.getMainHandItem());
                        vexentity.setItemSlot(EquipmentSlotType.MAINHAND, vexentity.getMainHandItem());
                    }
                    this.SummonSap(entityLiving, vexentity);
                    worldIn.addFreshEntity(vexentity);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                this.SummonDown(entityLiving);
            }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
                for (int i1 = 0; i1 < 3 + worldIn.random.nextInt(3); ++i1) {
                    BlockPos blockpos = entityLiving.blockPosition();
                    FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
                    vexentity.setOwnerId(entityLiving.getUUID());
                    vexentity.moveTo(blockpos, 0.0F, 0.0F);
                    vexentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                    vexentity.setBoundOrigin(blockpos);
                    if (SpellConfig.StaffVexLimit.get() > VexLimit(entityLiving)) {
                        vexentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    } else {
                        vexentity.setLimitedLife(1);
                        vexentity.addEffect(new EffectInstance(Effects.WITHER, 800, 1));
                    }
                    if (enchantment > 0){
                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(vexentity.getMainHandItem());
                        map.putIfAbsent(Enchantments.SHARPNESS, enchantment);
                        EnchantmentHelper.setEnchantments(map, vexentity.getMainHandItem());
                        vexentity.setItemSlot(EquipmentSlotType.MAINHAND, vexentity.getMainHandItem());
                    }
                    this.SummonSap(entityLiving, vexentity);
                    worldIn.addFreshEntity(vexentity);
                }
                this.SummonDown(entityLiving);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
    }

    public int VexLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(FriendlyVexEntity.class, this.vexCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(16.0D)).size();
    }
}
