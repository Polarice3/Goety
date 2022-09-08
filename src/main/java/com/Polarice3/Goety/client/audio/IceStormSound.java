package com.Polarice3.Goety.client.audio;

import com.Polarice3.Goety.common.entities.projectiles.IceStormEntity;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class IceStormSound extends TickableSound {
    protected final IceStormEntity iceStorm;
    private int time;

    public IceStormSound(IceStormEntity p_i226060_1_) {
        super(SoundEvents.ELYTRA_FLYING, SoundCategory.NEUTRAL);
        this.iceStorm = p_i226060_1_;
        this.x = (double)((float)p_i226060_1_.getX());
        this.y = (double)((float)p_i226060_1_.getY());
        this.z = (double)((float)p_i226060_1_.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.5F;
    }

    public void tick() {
        ++this.time;
        if (this.iceStorm.isAlive()) {
            this.x = (double)((float)this.iceStorm.getX());
            this.y = (double)((float)this.iceStorm.getY());
            this.z = (double)((float)this.iceStorm.getZ());
            this.volume = (float)((double)this.volume * ((double)(this.time - 20) / 20.0D));

            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }
        } else {
            this.stop();
        }
    }
}
