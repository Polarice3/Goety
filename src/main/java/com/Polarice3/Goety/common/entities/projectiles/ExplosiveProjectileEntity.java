package com.Polarice3.Goety.common.entities.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class ExplosiveProjectileEntity extends AbstractFireballEntity {
    public static final DataParameter<Boolean> DATA_DANGEROUS = EntityDataManager.defineId(ExplosiveProjectileEntity.class, DataSerializers.BOOLEAN);

    public ExplosiveProjectileEntity(EntityType<? extends ExplosiveProjectileEntity> p_i50166_1_, World p_i50166_2_) {
        super(p_i50166_1_, p_i50166_2_);
    }

    public ExplosiveProjectileEntity(EntityType<? extends ExplosiveProjectileEntity> p_i50167_1_, double p_i50167_2_, double p_i50167_4_, double p_i50167_6_, double p_i50167_8_, double p_i50167_10_, double p_i50167_12_, World p_i50167_14_) {
        super(p_i50167_1_, p_i50167_2_, p_i50167_4_, p_i50167_6_, p_i50167_8_, p_i50167_10_, p_i50167_12_, p_i50167_14_);
    }

    public ExplosiveProjectileEntity(EntityType<? extends ExplosiveProjectileEntity> p_i50168_1_, LivingEntity p_i50168_2_, double p_i50168_3_, double p_i50168_5_, double p_i50168_7_, World p_i50168_9_) {
        super(p_i50168_1_, p_i50168_2_, p_i50168_3_, p_i50168_5_, p_i50168_7_, p_i50168_9_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DANGEROUS, this.defaultDangerous());
    }

    public boolean defaultDangerous(){
        return false;
    }

    public boolean isDangerous() {
        return this.entityData.get(DATA_DANGEROUS);
    }

    public void setDangerous(boolean pDangerous) {
        this.entityData.set(DATA_DANGEROUS, pDangerous);
    }

    public abstract void setExplosionPower(float pExplosionPower);

    public abstract float getExplosionPower();

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("ExplosionPower", this.getExplosionPower());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ExplosionPower", 99)) {
            this.setExplosionPower(pCompound.getFloat("ExplosionPower"));
        }

    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null && pEntity == this.getOwner()){
            return false;
        } else {
            return super.canHitEntity(pEntity);
        }
    }

}
