package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.items.magic.SoulStaff;
import com.Polarice3.Goety.common.magic.SpewingSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class FireBreathSpell extends SpewingSpell {
    public float damage = SpellConfig.FireBreathDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();

    @Override
    public int defaultSoulCost() {
        return SpellConfig.FireBreathCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FIRE_BREATH_START.get();
    }

    public SoundEvent loopSound(LivingEntity livingEntity){
        return ModSounds.FIRE_BREATH.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.BURNING.get());
        return list;
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff){
        float enchantment = 0;
        int burning = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity) {
            if (WandUtil.enchantedFocus(entityLiving)) {
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
                burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), entityLiving);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), entityLiving);
            }
        }
        float damage = this.damage + enchantment;
        if (!worldIn.isClientSide) {
            for (Entity target : getTarget(entityLiving, range + 15)) {
                if (target != null) {
                    if (target.hurt(ModDamageSource.fireBreath(entityLiving), damage)){
                        target.setSecondsOnFire(5 * burning);
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
        this.breathAttack(ParticleTypes.FLAME, entityLiving, 0.3F + ((double) range / 10), spread);
    }
}
