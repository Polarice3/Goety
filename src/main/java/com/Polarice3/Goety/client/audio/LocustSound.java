package com.Polarice3.Goety.client.audio;

import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class LocustSound extends TickableSound {
    protected final LocustEntity bee;

    public LocustSound(LocustEntity p_i226060_1_) {
        super(SoundEvents.BEE_LOOP_AGGRESSIVE, SoundCategory.HOSTILE);
        this.bee = p_i226060_1_;
        this.x = (double)((float)p_i226060_1_.getX());
        this.y = (double)((float)p_i226060_1_.getY());
        this.z = (double)((float)p_i226060_1_.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    public void tick() {
        if (!this.bee.removed) {
            this.x = (double)((float)this.bee.getX());
            this.y = (double)((float)this.bee.getY());
            this.z = (double)((float)this.bee.getZ());
            float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(this.bee.getDeltaMovement()));
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
        return this.bee.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.bee.isBaby() ? 1.5F : 1.1F;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.bee.isSilent();
    }
}
