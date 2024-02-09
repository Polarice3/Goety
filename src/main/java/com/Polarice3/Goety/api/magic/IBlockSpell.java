package com.Polarice3.Goety.api.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface IBlockSpell extends ISpell{
    default int defaultCastDuration() {
        return 0;
    }

    default void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, ItemStack staff) {
    }

    default boolean rightBlock(ServerWorld worldIn, LivingEntity caster, BlockPos target){
        return true;
    }

    void blockResult(ServerWorld worldIn, LivingEntity caster, BlockPos target);
}
