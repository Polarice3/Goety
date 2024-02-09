package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class LaunchSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.LaunchCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.LaunchDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WIND.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.LaunchCoolDown.get();
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
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            int enchantment = 0;
            int duration = 1;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
            player.hurtMarked = true;
            if (!player.level.isClientSide){
                player.setOnGround(false);
            }
            Vector3d vector3d = player.getLookAngle();
            double power = staff ? 5.0D : 2.5D;
            double d0 = power + (double) (enchantment/2);
            player.setDeltaMovement(vector3d.x * d0, vector3d.y * d0, vector3d.z * d0);
            player.hasImpulse = true;
            player.fallDistance = 0;
            player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * duration));
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), this.getSoundSource(), 2.0F, 1.0F);
    }
}
