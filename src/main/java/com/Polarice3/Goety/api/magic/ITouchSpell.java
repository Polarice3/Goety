package com.Polarice3.Goety.api.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public interface ITouchSpell extends ISpell{
    default int defaultCastDuration() {
        return 0;
    }

    default void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, ItemStack staff) {
    }

    void touchResult(ServerWorld worldIn, LivingEntity caster, LivingEntity target);
}
