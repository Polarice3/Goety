package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpikeEntity extends GroundProjectileEntity {

    public SpikeEntity(EntityType<? extends SpikeEntity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
        this.lifeTicks = 600;
    }

    public SpikeEntity(World world, double pPosX, double pPosY, double pPosZ, float pYRot, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.SPIKE.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.yRot = pYRot * (180F / (float)Math.PI);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
    }

    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
    }

    public boolean canCollideWith(Entity p_241849_1_) {
        return (p_241849_1_.canBeCollidedWith() || p_241849_1_.isPushable()) && !this.isPassengerOfSameVehicle(p_241849_1_);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isAttackable() {
        return false;
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.sentTrapEvent) {
                --this.lifeTicks;
                if (this.animationTicks > 9){
                    --this.animationTicks;
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().move(0.0F, 0.2F, 0.0F).inflate(0.1F, 0.0F, 0.1F))) {
                this.dealDamageTo(livingentity);
            }

            if (!this.playSound){
                this.level.broadcastEntityEvent(this, (byte)5);
                this.playSound = true;
            }

            if (!this.sentTrapEvent) {
                this.level.broadcastEntityEvent(this, (byte)4);
                this.sentTrapEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.remove();
            }
        }

    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != livingentity) {
            if (livingentity == null) {
                if (target.hurt(DamageSource.MAGIC, 1.0F)){
                    this.level.broadcastEntityEvent(target, (byte) 44);
                }
            } else {
                if (target.isAlliedTo(livingentity)){
                    return;
                }
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                if (target.hurt(DamageSource.indirectMagic(this, livingentity), 1.0F)){
                    this.level.broadcastEntityEvent(target, (byte) 44);
                }
            }
        }
    }

    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 5) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GRINDSTONE_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
