package com.Polarice3.Goety.common.magic.spells.frost;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.items.magic.SoulStaff;
import com.Polarice3.Goety.common.magic.SpewingSpell;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class FrostBreathSpell extends SpewingSpell {
    public float damage = SpellConfig.FrostBreathDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();

    @Override
    public int defaultSoulCost() {
        return SpellConfig.FrostBreathCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.FROST;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        if (staff){
            StaffResult(worldIn, entityLiving);
        } else {
            WandResult(worldIn, entityLiving);
        }
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int duration = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        if (!worldIn.isClientSide) {
            for (Entity target : getTarget(entityLiving, range + 15)) {
                if (target != null) {
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        if (livingTarget.hurt(ModDamageSource.frostBreath(entityLiving), damage + enchantment)) {
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100 * duration));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PLAYER_BREATH, SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int duration = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        if (!worldIn.isClientSide) {
            for (Entity target : getTarget(entityLiving, range + 18)) {
                if (target != null) {
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        if (livingTarget.hurt(ModDamageSource.frostBreath(entityLiving), (damage * 2) + enchantment)) {
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100 * duration));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PLAYER_BREATH, SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    @Override
    public void showWandBreath(LivingEntity entityLiving) {
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        int spread = entityLiving.getUseItem().getItem() instanceof SoulStaff ? 10 : 5;
        this.breathAttack(ParticleTypes.POOF, entityLiving, 0.3F + ((double) range / 10), spread);
    }
}
