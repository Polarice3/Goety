package com.Polarice3.Goety.api.magic;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

public interface ISummonSpell extends ISpell{
    int SummonDownDuration();

    void commonResult(ServerWorld worldIn, LivingEntity entityLiving);

    default void SummonSap(LivingEntity owner, LivingEntity summonedEntity){
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

    default void SummonDown(LivingEntity entityLiving){
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
        EffectInstance effectinstance = new EffectInstance(ModEffects.SUMMONDOWN.get(), s, i, false, false, true);
        entityLiving.addEffect(effectinstance);
    }

    default void setTarget(ServerWorld serverLevel, LivingEntity source, MobEntity summoned){
        RayTraceResult rayTraceResult = this.rayTrace(serverLevel, source, 16, 3);
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (target instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) target;
                if (!MobUtil.areFullAllies(source, living)) {
                    summoned.setTarget(living);
                }
            }
        }
    }
}
