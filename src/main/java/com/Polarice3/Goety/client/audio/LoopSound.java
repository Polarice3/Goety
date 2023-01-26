package com.Polarice3.Goety.client.audio;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class LoopSound extends TickableSound {
    protected final Entity entity;

    public LoopSound(SoundEvent soundEvent, Entity entity) {
        super(soundEvent, SoundCategory.NEUTRAL);
        this.entity = entity;
        this.x = (double)((float)entity.getX());
        this.y = (double)((float)entity.getY());
        this.z = (double)((float)entity.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick() {
        if (this.entity.removed){
            this.stop();
        }
    }
}
