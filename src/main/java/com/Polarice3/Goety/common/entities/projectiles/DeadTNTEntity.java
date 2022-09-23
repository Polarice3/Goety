package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import com.Polarice3.Goety.utils.ExplosionUtil;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class DeadTNTEntity extends Entity {
    private static final DataParameter<Integer> DATA_FUSE_ID = EntityDataManager.defineId(DeadTNTEntity.class, DataSerializers.INT);
    @Nullable
    private LivingEntity owner;
    private int life = 80;

    public DeadTNTEntity(EntityType<? extends DeadTNTEntity> p_i50216_1_, World p_i50216_2_) {
        super(p_i50216_1_, p_i50216_2_);
        this.blocksBuilding = true;
    }

    public DeadTNTEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(ModEntityType.DEAD_TNT.get(), worldIn);
        this.setPos(x, y, z);
        double d0 = worldIn.random.nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE_ID, 80);
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    public boolean isPickable() {
        return !this.removed;
    }

    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.life;
        if (this.life <= 0) {
            this.remove();
            if (!this.level.isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void explode() {
        float f = 4.0F;
        ExplosionUtil.deadSandExplode(this.level, this, this.getX(), this.getY(0.0625D), this.getZ(), f, DeadSandExplosion.Mode.SPREAD);
    }

    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putShort("Fuse", (short)this.getLife());
    }

    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        this.setFuse(pCompound.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    protected float getEyeHeight(Pose pPose, EntitySize pSize) {
        return 0.15F;
    }

    public void setFuse(int pFuse) {
        this.entityData.set(DATA_FUSE_ID, pFuse);
        this.life = pFuse;
    }

    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        if (DATA_FUSE_ID.equals(pKey)) {
            this.life = this.getFuse();
        }

    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public int getLife() {
        return this.life;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
