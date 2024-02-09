package com.Polarice3.Goety.client.audio;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;

public class ItemLoopSound extends TickableSound {
    protected final LivingEntity entity;

    public ItemLoopSound(SoundEvent soundEvent, LivingEntity entity) {
        super(soundEvent, entity.getSoundSource());
        this.entity = entity;
        this.x = (double)((float)entity.getX());
        this.y = (double)((float)entity.getY());
        this.z = (double)((float)entity.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
    }

    public void tick() {
        if (!this.entity.isAlive() || !this.entity.isUsingItem()){
            this.stop();
        } else {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
        }
    }
}
