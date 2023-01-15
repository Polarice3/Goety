package com.Polarice3.Goety.common.entities.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class GroundProjectileEntity extends Entity {
    public int warmupDelayTicks;
    public boolean sentTrapEvent;
    public boolean playSound;
    public int lifeTicks = 200;
    public int animationTicks = 22;
    public LivingEntity owner;
    public UUID ownerUUID;

    public GroundProjectileEntity(EntityType<? extends Entity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public void setOwner(@Nullable LivingEntity p_190549_1_) {
        this.owner = p_190549_1_;
        this.ownerUUID = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        if (pCompound.contains("LifeTicks")) {
            this.lifeTicks = pCompound.getInt("LifeTicks");
        }
        this.warmupDelayTicks = pCompound.getInt("Warmup");
        this.playSound = pCompound.getBoolean("PlaySound");
        if (pCompound.contains("sentEvent")){
            this.sentTrapEvent = pCompound.getBoolean("sentEvent");
        }
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

    }

    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putInt("LifeTicks", this.lifeTicks);
        pCompound.putInt("Warmup", this.warmupDelayTicks);
        pCompound.putBoolean("PlaySound", this.playSound);
        pCompound.putBoolean("sentEvent", this.sentTrapEvent);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide){
            if (this.sentTrapEvent){
                this.level.broadcastEntityEvent(this, (byte)4);
            }
        }
    }

    public float getAnimationProgress(float pPartialTicks) {
        if (!this.sentTrapEvent) {
            return 0.0F;
        } else {
            if (this.lifeTicks <= 12) {
                int i = this.lifeTicks - 2;
                return i <= 0 ? 1.0F : 1.0F - ((float) i - pPartialTicks) / 20.0F;
            } else {
                return 1.0F - this.animationTicks / 20.0F;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.sentTrapEvent = true;
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
