package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class RootTrapEntity extends Entity {
    private int warmupDelayTicks;
    private boolean sentTrapEvent;
    private boolean playSound;
    private int lifeTicks = 100;
    private int animationTicks = 22;
    private boolean clientSideAttackStarted;
    private LivingEntity owner;
    private UUID ownerUUID;

    public RootTrapEntity(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    public RootTrapEntity(World world, double pPosX, double pPosY, double pPosZ, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.ROOTS.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public RootTrapEntity(World world, BlockPos blockPos, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.ROOTS.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
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

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        this.warmupDelayTicks = pCompound.getInt("Warmup");
        this.playSound = pCompound.getBoolean("PlaySound");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putInt("Warmup", this.warmupDelayTicks);
        pCompound.putBoolean("PlaySound", this.playSound);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.animationTicks > 9){
                    --this.animationTicks;
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
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
                if (target.hurt(DamageSource.thorns(this), 1.0F)){
                    target.playSound(SoundEvents.THORNS_HIT, 1.0F, 1.0F);
                }
            } else {
                if (target.isAlliedTo(livingentity)){
                    return;
                }
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                if (target.hurt(DamageSource.thorns(livingentity), 1.0F)){
                    target.playSound(SoundEvents.THORNS_HIT, 1.0F, 1.0F);
                }
            }
        }
    }

    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.clientSideAttackStarted = true;
        }
        if (pId == 5) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GRASS_BREAK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
        }
    }

    public float getAnimationProgress(float pPartialTicks) {
        if (!this.clientSideAttackStarted) {
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

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
