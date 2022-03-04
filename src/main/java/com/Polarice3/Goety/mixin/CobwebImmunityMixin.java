package com.Polarice3.Goety.mixin;

import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WebBlock.class)
public class CobwebImmunityMixin extends Block implements IForgeShearable {
    protected CobwebImmunityMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity)  {
        boolean flag = RobeArmorFinder.FindArachnoArmor((LivingEntity) pEntity);
        if (!flag) {
            pEntity.makeStuckInBlock(pState, new Vector3d(0.25D, (double) 0.05F, 0.25D));
        }
    }
}
