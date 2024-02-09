package com.Polarice3.Goety.api.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IOwned {
    LivingEntity getTrueOwner();

    UUID getOwnerId();

    void setOwnerId(@Nullable UUID p_184754_1_);

    void setTrueOwner(LivingEntity livingEntity);

    void setHostile(boolean hostile);

    boolean isHostile();

    @Nullable
    default EntityType<?> getVariant(World level, BlockPos blockPos){
        return null;
    };

    default LivingEntity getMasterOwner(){
        if (this.getTrueOwner() instanceof IOwned){
            IOwned iOwned = (IOwned) this.getTrueOwner();
            return iOwned.getTrueOwner();
        } else {
            return null;
        }
    }

    default void convertNewEquipment(Entity entity) {
    }
}
