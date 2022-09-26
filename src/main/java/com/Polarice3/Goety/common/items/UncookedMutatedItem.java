package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UncookedMutatedItem extends FakeFoodItem {
    public UncookedMutatedItem() {
        super(4, 0.8F);
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int random = worldIn.random.nextInt(4);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addEffect(effectinstance);
                EffectInstance effectinstance2 = new EffectInstance(ModEffects.HOSTED.get(), 600, 0);
                entityLiving.addEffect(effectinstance2);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeEffectNoUpdate(Effects.HUNGER);
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, i);
                entityLiving.addEffect(effectinstance);
                EffectInstance effectinstance2 = new EffectInstance(ModEffects.HOSTED.get(), 600, i);
                entityLiving.addEffect(effectinstance2);
            }
        }
        if (stack.isEnchanted()){
            if (worldIn.random.nextFloat() <= 0.5F){
                EffectInstance effectinstance2 = entityLiving.getEffect(ModEffects.COSMIC.get());
                if (effectinstance2 == null) {
                    EffectInstance effectinstance = new EffectInstance(ModEffects.COSMIC.get(), 3000, 0, false, false);
                    entityLiving.addEffect(effectinstance);
                } else {
                    EffectsUtil.amplifyEffect(entityLiving, ModEffects.COSMIC.get(), 3000, false, false);
                }
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
