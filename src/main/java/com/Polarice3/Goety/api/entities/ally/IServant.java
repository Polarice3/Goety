package com.Polarice3.Goety.api.entities.ally;

import com.Polarice3.Goety.api.entities.IOwned;
import net.minecraft.entity.player.PlayerEntity;

public interface IServant extends IOwned {
    boolean isWandering();

    void setWandering(boolean wandering);

    boolean isStaying();

    void setStaying(boolean staying);

    default boolean isFollowing(){
        return !this.isWandering() && !this.isStaying();
    }

    boolean canUpdateMove();

    void updateMoveMode(PlayerEntity player);
}
