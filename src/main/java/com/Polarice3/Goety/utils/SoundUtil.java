package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundUtil {

    public SoundUtil(Entity entity, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        if (entity.level != null) {
            entity.level.playLocalSound(entity.position().x() + 0.5D, entity.position().y() + 0.5D, entity.position().z() + 0.5D, pSound, pCategory, pVolume, pPitch, false);
        }
    }

}
