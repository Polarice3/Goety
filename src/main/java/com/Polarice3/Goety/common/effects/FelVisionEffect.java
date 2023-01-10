package com.Polarice3.Goety.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FelVisionEffect extends ModEffect {
    public FelVisionEffect() {
        super(EffectType.BENEFICIAL, 2039713);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        boolean blind = false;
        World world = pLivingEntity.level;
        if (!world.isClientSide) {
            if (world.isDay()) {
                float f = pLivingEntity.getBrightness();
                BlockPos blockpos = pLivingEntity.getVehicle() instanceof BoatEntity ? (new BlockPos(pLivingEntity.getX(), (double) Math.round(pLivingEntity.getEyeY()), pLivingEntity.getZ())).above() : new BlockPos(pLivingEntity.getX(), (double) Math.round(pLivingEntity.getEyeY()), pLivingEntity.getZ());
                if (f > 0.5F && world.canSeeSky(blockpos)) {
                    blind = true;
                }
            }
            if (blind) {
                if (pLivingEntity.hasEffect(Effects.NIGHT_VISION)) {
                    pLivingEntity.removeEffect(Effects.NIGHT_VISION);
                }
            } else {
                pLivingEntity.addEffect(new EffectInstance(Effects.NIGHT_VISION, 300, 0, false, false, false));
            }
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
