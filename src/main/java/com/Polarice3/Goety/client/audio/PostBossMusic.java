package com.Polarice3.Goety.client.audio;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class PostBossMusic extends TickableSound {
    protected final MobEntity mobEntity;

    public PostBossMusic(SoundEvent soundEvent, MobEntity mobEntity) {
        super(soundEvent, SoundCategory.RECORDS);
        this.mobEntity = mobEntity;
        this.x = (double)((float)mobEntity.getX());
        this.y = (double)((float)mobEntity.getY());
        this.z = (double)((float)mobEntity.getZ());
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override
    public void tick() {
    }
}
