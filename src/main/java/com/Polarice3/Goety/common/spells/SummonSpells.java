package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
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
        int s;
        if (SummonMastery(entityLiving)){
            int random = entityLiving.level.random.nextInt(2);
            if (random == 0){
                s = 0;
            } else {
                s = SummonDownDuration();
            }
        } else {
            s = SummonDownDuration();
        }
        EffectInstance effectinstance = new EffectInstance(ModEffects.SUMMONDOWN.get(), s, i, false, false, true);
        entityLiving.addEffect(effectinstance);
    }

    public int SummonLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(OwnedEntity.class, this.summonCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(64.0D)).size();
    }
}
