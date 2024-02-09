package com.Polarice3.Goety.common.magic.spells.ender;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.items.magic.SoulStaff;
import com.Polarice3.Goety.common.magic.SpewingSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;

public class AcidBreathSpell extends SpewingSpell {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.EnderAcidCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FIRE_BREATH_START.get();
    }

    public SoundEvent loopSound(LivingEntity livingEntity){
        return ModSounds.FIRE_BREATH.get();
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.ENDER;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        int enchantment = 0;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        if (!worldIn.isClientSide) {
            int reach = staff ? 18 : 15;
            for (Entity target : getTarget(entityLiving, range + reach)) {
                if (target != null) {
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        if (livingTarget.canBeAffected(new EffectInstance(Effects.HARM))) {
                            if (livingTarget.isInvertedHealAndHarm()) {
                                livingTarget.heal((float) (3 << enchantment));
                            } else {
                                livingTarget.hurt(DamageSource.indirectMagic(entityLiving, entityLiving), (float) (3 << enchantment));
                            }
                        }
                    }
                }
            }
        }
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
        this.breathAttack(ParticleTypes.DRAGON_BREATH, entityLiving, 0.3F + ((double) range / 10), spread);
    }
}
