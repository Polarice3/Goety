package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SoundUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class PoisonBallEntity extends ThrowableEntity {
    private static final DataParameter<Boolean> DATA_UPGRADED = EntityDataManager.defineId(PoisonBallEntity.class, DataSerializers.BOOLEAN);

    public PoisonBallEntity(EntityType<? extends PoisonBallEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public PoisonBallEntity(EntityType<? extends ThrowableEntity> p_i48541_1_, double p_i50156_2_, double p_i50156_4_, double p_i50156_6_, World p_i50156_8_) {
        super(ModEntityType.POISON_BALL.get(), p_i50156_2_, p_i50156_4_, p_i50156_6_, p_i50156_8_);
    }

    public PoisonBallEntity(EntityType<? extends ThrowableEntity> p_i50157_1_, LivingEntity p_i50157_2_, World p_i50157_3_) {
        super(ModEntityType.POISON_BALL.get(), p_i50157_2_, p_i50157_3_);
    }

    @Override
    public void tick() {
        super.tick();
        for(int i = 0; i < 8; ++i) {
            new ParticleUtil(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();
            if (owner instanceof LivingEntity) {
                LivingEntity LivingOwner = (LivingEntity)owner;
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        if (this.isUpgraded()){
                            livingTarget.addEffect(new EffectInstance(Effects.POISON, 432, 1));
                        } else {
                            livingTarget.addEffect(new EffectInstance(Effects.POISON, 900));
                        }
                        if (RobeArmorFinder.FindArachnoSet(LivingOwner)){
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1800));
                        }
                    }
                }
            } else {
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        if (this.isUpgraded()){
                            livingTarget.addEffect(new EffectInstance(Effects.POISON, 432, 1));
                        } else {
                            livingTarget.addEffect(new EffectInstance(Effects.POISON, 900));
                        }
                    }
                }
            }
            new SoundUtil(pResult.getLocation(), SoundEvents.SLIME_ATTACK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            new SoundUtil(pResult.getLocation(), SoundEvents.SLIME_ATTACK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }
    }


    public boolean isOnFire() {
        return false;
    }

    public boolean ignoreExplosion(){return false;}

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean isUpgraded() {
        return this.entityData.get(DATA_UPGRADED);
    }

    public void setUpgraded(boolean pInvulnerable) {
        this.entityData.set(DATA_UPGRADED, pInvulnerable);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_UPGRADED, false);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
