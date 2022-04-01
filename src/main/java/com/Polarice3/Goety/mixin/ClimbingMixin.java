package com.Polarice3.Goety.mixin;

import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class ClimbingMixin extends LivingEntity {
    protected ClimbingMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean onClimbable() {
        if (!super.onClimbable()) {
            if (!this.level.isClientSide) {
                if (RobeArmorFinder.FindArachnoBootsofWander(this)) {
                    return this.horizontalCollision;
                }
            }
        }
        return ForgeHooks.isLivingOnLadder(getFeetBlockState(), this.level, blockPosition(), this);
    }
}
