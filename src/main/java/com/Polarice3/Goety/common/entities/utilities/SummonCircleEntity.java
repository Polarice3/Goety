package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SummonCircleEntity extends Entity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(SummonCircleEntity.class, DataSerializers.OPTIONAL_UUID);
    public Entity entity;
    public boolean preMade;
    public boolean noPos;
    public int lifeSpan = 20;

    public SummonCircleEntity(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    public SummonCircleEntity(World pLevel, Vector3d pPos, Entity pEntity, boolean preMade, LivingEntity pOwner){
        this(ModEntityType.SUMMON_CIRCLE.get(), pLevel);
        this.setPos(pPos.x, pPos.y, pPos.z);
        this.entity = pEntity;
        this.preMade = preMade;
        this.setTrueOwner(pOwner);
    }

    public SummonCircleEntity(World pLevel, BlockPos pPos, Entity pEntity, boolean preMade, LivingEntity pOwner){
        this(ModEntityType.SUMMON_CIRCLE.get(), pLevel);
        this.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
        this.entity = pEntity;
        this.preMade = preMade;
        this.setTrueOwner(pOwner);
    }

    public SummonCircleEntity(World pLevel, BlockPos pPos, Entity pEntity, boolean preMade, boolean noPos, LivingEntity pOwner){
        this(ModEntityType.SUMMON_CIRCLE.get(), pLevel);
        this.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
        this.entity = pEntity;
        this.preMade = preMade;
        this.noPos = noPos;
        this.setTrueOwner(pOwner);
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        UUID uuid;
        if (pCompound.hasUUID("Owner")) {
            uuid = pCompound.getUUID("Owner");
        } else {
            String s = pCompound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
        this.preMade = pCompound.getBoolean("preMade");
        this.noPos = pCompound.getBoolean("noPos");
        this.lifeSpan = pCompound.getInt("lifeSpan");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        if (this.getOwnerId() != null) {
            pCompound.putUUID("Owner", this.getOwnerId());
        }
        pCompound.putBoolean("preMade", this.preMade);
        pCompound.putBoolean("noPos", this.noPos);
        pCompound.putInt("lifeSpan", this.lifeSpan);
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

    public void setTrueOwner(LivingEntity livingEntity){
        this.setOwnerId(livingEntity.getUUID());
    }

    public void setLifeSpan(int lifeSpan){
        this.lifeSpan = lifeSpan;
    }

    public int getLifeSpan() {
        if (this.lifeSpan == 0){
            return 20;
        }
        return this.lifeSpan;
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public void tick() {
        super.tick();
        if (!this.isNoGravity()) {
            MobUtil.moveDownToGround(this);
        }
        if (this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            float f = 1.5F;
            float f5 = (float) Math.PI * f * f;
            if (this.tickCount == this.getLifeSpan()){
                for (int j1 = 0; j1 < 16; ++j1) {
                    for (int k1 = 0; (float) k1 < f5; ++k1) {
                        float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                        float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                        float f8 = MathHelper.cos(f6) * f7;
                        float f9 = MathHelper.sin(f6) * f7;
                        serverWorld.sendParticles(ParticleTypes.REVERSE_PORTAL, this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 0, 0, 0.5D, 0, 0.5F);
                    }
                }
                if (this.entity != null){
                    if (this.noPos) {
                        this.entity.setPos(this.getX(), this.getY(), this.getZ());
                    }
                    if (this.preMade) {
                        if (this.entity instanceof TameableEntity && this.getOwnerId() != null) {
                            ((TameableEntity) this.entity).setOwnerUUID(this.getOwnerId());
                        }
                        if (this.entity instanceof IOwned && this.getTrueOwner() != null) {
                            ((IOwned) this.entity).setTrueOwner(this.getTrueOwner());
                        }
                        if (this.entity instanceof MobEntity) {
                            ((MobEntity) this.entity).finalizeSpawn(serverWorld, this.level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            if (this.getTrueOwner() != null && this.getTrueOwner() instanceof MobEntity) {
                                if (((MobEntity) this.getTrueOwner()).getTarget() != null) {
                                    ((MobEntity) this.entity).setTarget(((MobEntity) this.getTrueOwner()).getTarget());
                                }
                            }
                        }
                    }
                    serverWorld.addFreshEntity(entity);
                }
            }
        }
        if (this.tickCount % this.getLifeSpan() == 0){
            this.playSound(ModSounds.SUMMON_SPELL.get(), 1.0F, 1.0F);
            this.remove();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
