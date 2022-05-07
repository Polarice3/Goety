package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WitchGaleEntity extends DamagingProjectileEntity {
    private static final DataParameter<Boolean> DATA_UPGRADED = EntityDataManager.defineId(WitchGaleEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(WitchGaleEntity.class, DataSerializers.OPTIONAL_UUID);
    private int lifespan;
    private int totallife;

    public WitchGaleEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
        this.noPhysics = false;
        this.lifespan = 0;
        this.totallife = 60;
    }

    public WitchGaleEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(ModEntityType.WITCHGALE.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public WitchGaleEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(ModEntityType.WITCHGALE.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
    }

    protected float getInertia() {
        return 0.75F;
    }

    public int getTotallife() {
        return totallife;
    }

    public void setTotallife(int totallife) {
        this.totallife = totallife;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void tick() {
        super.tick();
        if (this.lifespan < getTotallife()){
            ++this.lifespan;
        } else {
            this.remove();
        }
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(AreaofEffect()))) {
            if (entity != this.getTrueOwner() || entity != this.getOwner()) {
                if (this.isUpgraded()) {
                    entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1800, 1));
                    entity.addEffect(new EffectInstance(Effects.WEAKNESS, 1800, 1));
                    entity.addEffect(new EffectInstance(Effects.POISON, 432, 1));
                    this.upgradedlaunch(entity);
                } else {
                    entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1800));
                    entity.addEffect(new EffectInstance(Effects.WEAKNESS, 1800));
                    entity.addEffect(new EffectInstance(Effects.POISON, 900));
                    this.launch(entity);
                }
            }
        }
    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
    }

    private void upgradedlaunch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 4.0D, 0.4D, d1 / d2 * 4.0D);
    }

    public double AreaofEffect(){
        if (this.isUpgraded()){
            return 2.0D;
        } else {
            return 1.0D;
        }
    }


    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_UPGRADED, false);
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }


    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }

    }

    public boolean isUpgraded() {
        return this.entityData.get(DATA_UPGRADED);
    }

    public void setUpgraded(boolean pInvulnerable) {
        this.entityData.set(DATA_UPGRADED, pInvulnerable);
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.WITCH;
    }

    public EntitySize getDimensions(Pose pPose) {
        if (this.isUpgraded()){
            return super.getDimensions(pPose).scale(2.0F);
        } else {
            return super.getDimensions(pPose).scale(1.0F);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
