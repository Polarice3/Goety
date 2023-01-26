package com.Polarice3.Goety.client.audio;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BossLoopMusic extends TickableSound {
    protected final MobEntity mobEntity;

    public BossLoopMusic(SoundEvent soundEvent, MobEntity mobEntity) {
        super(soundEvent, SoundCategory.RECORDS);
        this.mobEntity = mobEntity;
        this.x = (double)((float)mobEntity.getX());
        this.y = (double)((float)mobEntity.getY());
        this.z = (double)((float)mobEntity.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
    }

    public void tick() {
        if (!MainConfig.BossMusic.get()){
            this.stop();
        }
        if (this.mobEntity.removed || this.mobEntity.isDeadOrDying() || !this.mobEntity.isAlive()){
            if (this.mobEntity.isDeadOrDying()){
                if (this.mobEntity.level.isClientSide){
                    Minecraft minecraft = Minecraft.getInstance();
                    SoundHandler soundHandler = minecraft.getSoundManager();
                    if (!this.isStopped()){
                        if (this.mobEntity instanceof ApostleEntity) {
                            soundHandler.queueTickingSound(new PostBossMusic(ModSounds.APOSTLE_THEME_POST.get(), mobEntity));
                        } else {
                            soundHandler.queueTickingSound(new PostBossMusic(ModSounds.BOSS_POST.get(), mobEntity));
                        }
                    }
                }
                this.stop();
            } else {
                this.stop();
            }
        }
    }
}
