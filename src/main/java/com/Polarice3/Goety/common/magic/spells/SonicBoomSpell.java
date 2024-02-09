package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class SonicBoomSpell extends Spells {

    public int defaultSoulCost() {
        return SpellConfig.SonicBoomCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.SonicBoomDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.SonicBoomCoolDown.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.SONIC_CHARGE.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        return list;
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff){
        float damage = SpellConfig.SonicBoomDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();
        if (WandUtil.enchantedFocus(entityLiving)){
            damage += WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
        }
        if (MobUtil.getSingleTarget(worldIn, entityLiving, 15, 3) instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) MobUtil.getSingleTarget(worldIn, entityLiving, 15, 3);
            Vector3d vec3 = entityLiving.position().add(0.0D, (double) 1.6F, 0.0D);
            Vector3d vec31 = livingEntity.getEyePosition(1.0F).subtract(vec3);
            Vector3d vec32 = vec31.normalize();

            for (int i = 1; i < MathHelper.floor(vec31.length()) + 7; ++i) {
                Vector3d vec33 = vec3.add(vec32.scale((double) i));
                worldIn.sendParticles(ModParticleTypes.SONIC_BOOM.get(), vec33.x, vec33.y, vec33.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }

            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ROAR_SPELL.get(), this.getSoundSource(), 3.0F, 1.0F);
            livingEntity.hurt(ModDamageSource.sonicBoom(entityLiving), damage);
            double d1 = 0.5D * (1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            double d0 = 2.5D * (1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            livingEntity.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
        } else {
            Vector3d srcVec = new Vector3d(entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ());
            Vector3d lookVec = entityLiving.getViewVector(1.0F);
            Vector3d destVec = srcVec.add(lookVec.x * 15, lookVec.y * 15, lookVec.z * 15);
            for(int i = 1; i < Math.floor(destVec.length()) + 7; ++i) {
                Vector3d vector3d2 = srcVec.add(lookVec.scale((double)i));
                worldIn.sendParticles(ModParticleTypes.SONIC_BOOM.get(), vector3d2.x, vector3d2.y, vector3d2.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            if (MobUtil.getSingleTarget(worldIn, entityLiving, 15.0D, 3.0D) != null){
                if (MobUtil.getSingleTarget(worldIn, entityLiving, 15.0D, 3.0D) instanceof LivingEntity) {
                    LivingEntity target1 = (LivingEntity) MobUtil.getSingleTarget(worldIn, entityLiving, 15.0D, 3.0D);
                    target1.hurt(ModDamageSource.sonicBoom(entityLiving), damage);
                    double d0 = target1.getX() - entityLiving.getX();
                    double d1 = target1.getZ() - entityLiving.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    MobUtil.push(target1, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ROAR_SPELL.get(), this.getSoundSource(), 3.0F, 1.0F);
    }


}
