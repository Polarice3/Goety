package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MagicBlastTrapEntity extends Entity {
    private static final DataParameter<Boolean> IMMEDIATE = EntityDataManager.defineId(MagicBlastTrapEntity.class, DataSerializers.BOOLEAN);
    public LivingEntity owner;
    private UUID ownerUniqueId;

    public MagicBlastTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true;
    }

    public MagicBlastTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.MAGIC_BLAST_TRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IMMEDIATE, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    public void setImmediate(boolean immediate){
        this.entityData.set(IMMEDIATE, immediate);
    }

    public boolean getImmediate(){
        return this.entityData.get(IMMEDIATE);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            float f = 1.5F;
            float f5 = (float) Math.PI * f * f;
            for (int k1 = 0; (float) k1 < f5; ++k1) {
                float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;
                serverWorld.sendParticles(ParticleTypes.DRAGON_BREATH, this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 1, 0, 0, 0, 0);
            }
            if (this.tickCount % 20 == 0 || this.getImmediate()){
                for (int j1 = 0; j1 < 16; ++j1) {
                    for (int k1 = 0; (float) k1 < f5; ++k1) {
                        float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                        float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                        float f8 = MathHelper.cos(f6) * f7;
                        float f9 = MathHelper.sin(f6) * f7;
                        serverWorld.sendParticles(ParticleTypes.WITCH, this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 0, 0, 0.5D, 0, 0.5F);
                    }
                }
                List<LivingEntity> targets = new ArrayList<>();
                for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1))){
                    if (this.owner != null) {
                        if (livingEntity != this.owner && !livingEntity.isAlliedTo(this.owner)) {
                            targets.add(livingEntity);
                        }
                    } else {
                        targets.add(livingEntity);
                    }
                }
                if (!targets.isEmpty()){
                    for (LivingEntity livingEntity : targets) {
                        livingEntity.hurt(DamageSource.indirectMagic(this, this.owner), 5.0F);
                    }
                }
            }
        }
        if (this.owner != null){
            if (this.owner.isDeadOrDying() || this.owner.removed){
                this.remove();
            }
        }
        if (this.tickCount % 20 == 0){
            this.playSound(SoundEvents.ILLUSIONER_CAST_SPELL, 1.0F, 0.5F);
            this.remove();
        }
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }
}
