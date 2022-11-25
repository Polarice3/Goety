package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;

public abstract class SummonSpells extends Spells{
    private final EntityPredicate summonCountTargeting = (new EntityPredicate()).range(64.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public abstract int SummonDownDuration();

    public boolean NecroPower(LivingEntity entityLiving){
        return RobeArmorFinder.FindNecroArmor(entityLiving);
    }

    public boolean NecroMastery(LivingEntity entityLiving){
        return RobeArmorFinder.FindNecroHelm(entityLiving);
    }

    public boolean SummonMastery(LivingEntity entityLiving){
        return RobeArmorFinder.FindLeggings(entityLiving);
    }

    public void SummonSap(LivingEntity owner, LivingEntity summonedEntity){
        if (owner != null && summonedEntity != null) {
            if (owner.hasEffect(ModEffects.SUMMONDOWN.get())) {
                EffectInstance effectinstance = owner.getEffect(ModEffects.SUMMONDOWN.get());
                if (effectinstance != null) {
                    summonedEntity.addEffect(new EffectInstance(Effects.WEAKNESS, Integer.MAX_VALUE, effectinstance.getAmplifier()));
                    summonedEntity.addEffect(new EffectInstance(ModEffects.SAPPED.get(), Integer.MAX_VALUE, effectinstance.getAmplifier()));
                }
            }
        }
    }

    public void SummonDown(LivingEntity entityLiving){
        EffectInstance effectinstance1 = entityLiving.getEffect(ModEffects.SUMMONDOWN.get());
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            entityLiving.removeEffectNoUpdate(ModEffects.SUMMONDOWN.get());
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        int s = SummonDownDuration();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                s = (int) (SummonDownDuration() * 1.5);
            }
        }
        if (SummonMastery(entityLiving)){
            if (entityLiving.level.random.nextBoolean()){
                s = 0;
            }
        }
        EffectInstance effectinstance = new EffectInstance(ModEffects.SUMMONDOWN.get(), s, i, false, false, true);
        entityLiving.addEffect(effectinstance);
    }

    public int SummonLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(OwnedEntity.class, this.summonCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(64.0D)).size();
    }
}
