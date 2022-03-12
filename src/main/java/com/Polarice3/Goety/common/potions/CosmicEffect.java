package com.Polarice3.Goety.common.potions;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class CosmicEffect extends ModEffects{
    public CosmicEffect() {
        super(EffectType.NEUTRAL, 9044223);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof PlayerEntity){
            int r = pLivingEntity.level.random.nextInt(30);
            int a = pLivingEntity.level.random.nextInt(5);
            switch (r){
                case 0:
                    pLivingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a));
                    break;
                case 1:
                    pLivingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 400, a));
                    break;
                case 2:
                    pLivingEntity.addEffect(new EffectInstance(Effects.HUNGER, 400, a));
                    break;
                case 3:
                    pLivingEntity.addEffect(new EffectInstance(Effects.CONFUSION, 400));
                    break;
                case 4:
                    pLivingEntity.addEffect(new EffectInstance(Effects.REGENERATION, 400, a));
                    break;
                case 5:
                    pLivingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a));
                    break;
                case 6:
                    pLivingEntity.addEffect(new EffectInstance(Effects.INVISIBILITY, 400));
                    break;
                case 7:
                    pLivingEntity.addEffect(new EffectInstance(Effects.GLOWING, 400));
                    break;
                case 8:
                    pLivingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 400));
                    break;
                case 9:
                    pLivingEntity.addEffect(new EffectInstance(Effects.NIGHT_VISION, 400));
                    break;
                case 10:
                    pLivingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400, a));
                    break;
                case 11:
                    pLivingEntity.addEffect(new EffectInstance(Effects.DIG_SPEED, 400, a));
                    break;
                case 12:
                    pLivingEntity.addEffect(new EffectInstance(Effects.WITHER, 400, a));
                    break;
                case 13:
                    pLivingEntity.addEffect(new EffectInstance(ModRegistry.HOSTED.get(), 400, a));
                    break;
                case 14:
                    pLivingEntity.addEffect(new EffectInstance(ModRegistry.CURSED.get(), 400, a));
                    break;
            }
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int k = 1200 >> pAmplifier;
        if (k > 0) {
            return pDuration % k == 0;
        } else {
            return true;
        }
    }

}
