package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.utils.MobUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractMonolithEntity extends OwnedEntity{
    protected static final DataParameter<Integer> AGE = EntityDataManager.defineId(AbstractMonolithEntity.class, DataSerializers.INT);
    private boolean activate;

    public AbstractMonolithEntity(EntityType<? extends OwnedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AGE, 0);
    }

    public void addAdditionalSaveData(CompoundNBT p_31485_) {
        super.addAdditionalSaveData(p_31485_);
        p_31485_.putInt("Age", this.getAge());
        p_31485_.putBoolean("Activate", this.isActivate());
    }

    public void readAdditionalSaveData(CompoundNBT p_31474_) {
        super.readAdditionalSaveData(p_31474_);
        this.setAge(p_31474_.getInt("Age"));
        this.setActivate(p_31474_.getBoolean("Activate"));
    }

    public abstract BlockState getState();

    public IParticleData getParticles(){
        return new BlockParticleData(ParticleTypes.BLOCK, this.getState());
    }

    public void setAge(int age){
        this.entityData.set(AGE, age);
    }

    public int getAge(){
        return this.entityData.get(AGE);
    }

    public void setActivate(boolean activate){
        this.activate = activate;
    }

    public boolean isActivate(){
        return this.activate;
    }

    public boolean isInvulnerableTo(DamageSource p_219427_) {
        return this.isEmerging() && !p_219427_.isBypassInvul() || super.isInvulnerableTo(p_219427_);
    }

    public static float getEmergingTime(){
        return 60.0F;
    }

    public boolean isEmerging() {
        return this.getAge() < getEmergingTime();
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return false;
    }

    public boolean isPushable() {
        return false;
    }

    public void push(Entity entityIn) {
    }

    @Override
    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
    }

    public void aiStep() {
        super.aiStep();
        if (!this.isNoGravity()) {
            MobUtil.moveDownToGround(this);
        }
        if (this.isEmerging()){
            if (!this.level.isClientSide) {
                this.setAge(this.getAge() + 1);
                this.level.broadcastEntityEvent(this, (byte) 4);
            }
        }
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 4){
            this.setAge(this.getAge() + 1);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    public AbstractMonolithEntity.Crackiness getCrackiness() {
        return AbstractMonolithEntity.Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
    }

    public static enum Crackiness {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<AbstractMonolithEntity.Crackiness> BY_DAMAGE = Stream.of(values()).sorted(Comparator.comparingDouble((p_28904_) -> {
            return (double)p_28904_.fraction;
        })).collect(ImmutableList.toImmutableList());
        private final float fraction;

        private Crackiness(float p_28900_) {
            this.fraction = p_28900_;
        }

        public static AbstractMonolithEntity.Crackiness byFraction(float p_28902_) {
            for(AbstractMonolithEntity.Crackiness irongolem$crackiness : BY_DAMAGE) {
                if (p_28902_ < irongolem$crackiness.fraction) {
                    return irongolem$crackiness;
                }
            }

            return NONE;
        }
    }
}
