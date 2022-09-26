package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class MutatedFoodItem extends FakeFoodItem {

    public MutatedFoodItem(int foodLevel, float saturation) {
        super(foodLevel, saturation);
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int random = worldIn.random.nextInt(16);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addEffect(effectinstance);
            } else {
                EffectsUtil.amplifyEffect(entityLiving, Effects.HUNGER, 600);
            }
        }
        if (stack.isEnchanted()){
            if (worldIn.random.nextFloat() <= 0.25F){
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
