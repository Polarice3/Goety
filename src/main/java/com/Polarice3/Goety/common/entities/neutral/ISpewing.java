package com.Polarice3.Goety.common.entities.neutral;

import net.minecraft.entity.Entity;

public interface ISpewing {

    boolean isSpewing();

    void setSpewing(boolean flag);

    void doSpewing(Entity target);
}
