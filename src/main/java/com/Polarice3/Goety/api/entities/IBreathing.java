package com.Polarice3.Goety.api.entities;

import net.minecraft.entity.Entity;

public interface IBreathing {

    boolean isBreathing();

    void setBreathing(boolean flag);

    void doBreathing(Entity target);
}
