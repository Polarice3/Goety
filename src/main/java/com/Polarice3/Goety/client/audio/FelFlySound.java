package com.Polarice3.Goety.client.audio;

import com.Polarice3.Goety.common.entities.ally.FelFlyEntity;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class FelFlySound extends TickableSound {
    protected final FelFlyEntity fly;

    public FelFlySound(FelFlyEntity p_i226060_1_) {
        super(ModSounds.FEL_FLY_LOOP.get(), SoundCategory.NEUTRAL);
        this.fly = p_i226060_1_;
        this.x = (double)((float)p_i226060_1_.getX());
        this.y = (double)((float)p_i226060_1_.getY());
        this.z = (double)((float)p_i226060_1_.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    public void tick() {
        if (!this.fly.removed) {
            this.x = (double)((float)this.fly.getX());
            this.y = (double)((float)this.fly.getY());
            this.z = (double)((float)this.fly.getZ());
            float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(this.fly.getDeltaMovement()));
            if ((double)f >= 0.01D) {
                this.pitch = MathHelper.lerp(MathHelper.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }
        } else {
            this.stop();
        }
    }

    private float getMinPitch() {
        return 1.5F;
    }

    private float getMaxPitch() {
        return 2.0F;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.fly.isSilent();
    }
}
