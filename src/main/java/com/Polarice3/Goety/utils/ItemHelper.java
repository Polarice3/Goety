package com.Polarice3.Goety.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemHelper {

    public static <T extends LivingEntity> void hurtAndRemove(ItemStack stack, int pAmount, T pEntity) {
        if (!pEntity.level.isClientSide && (!(pEntity instanceof PlayerEntity) || !((PlayerEntity)pEntity).abilities.instabuild)) {
            if (stack.isDamageableItem()) {
                if (stack.hurt(pAmount, pEntity.getRandom(), pEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity)pEntity : null)) {
                    stack.shrink(1);
                    stack.setDamageValue(0);
                }
            }
        }
    }
}
