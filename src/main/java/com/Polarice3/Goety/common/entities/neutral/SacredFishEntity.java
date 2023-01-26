package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SacredFishEntity extends AbstractFishEntity {
    public int spawnTimes;

    public SacredFishEntity(EntityType<? extends AbstractFishEntity> p_i48565_1_, World p_i48565_2_) {
        super(p_i48565_1_, p_i48565_2_);
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        if (this.isUnderWater()) {
            this.spawnAtLocation(ModItems.SACRED_FISH.get());
        } else {
            for (int i = 0; i < 2 + this.level.random.nextInt(2); ++i) {
                this.spawnAtLocation(Items.GOLD_NUGGET);
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0F);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("spawnTimes", this.spawnTimes);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.spawnTimes = pCompound.getInt("spawnTimes");
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof LivingEntity && this.canAttack((LivingEntity) pSource.getEntity())){
            if (this.spawnTimes < 20 && this.isUnderWater()) {
                if (this.level.random.nextFloat() <= 0.75F) {
                    PufferfishEntity pufferfishEntity = EntityType.PUFFERFISH.create(this.level);
                    pufferfishEntity.setPos(this.getX(), this.getY(), this.getZ());
                    pufferfishEntity.setPuffState(2);
                    double d0 = pSource.getEntity().getX() - pufferfishEntity.getX();
                    double d1 = pSource.getEntity().getY() - pufferfishEntity.getY();
                    double d2 = pSource.getEntity().getZ() - pufferfishEntity.getZ();
                    pufferfishEntity.setDeltaMovement(d0, d1, d2);
                    this.level.addFreshEntity(pufferfishEntity);
                } else {
                    GuardianEntity guardian = EntityType.GUARDIAN.create(this.level);
                    guardian.moveTo(this.blockPosition(), 0.0F, 0.0F);
                    guardian.setTarget((LivingEntity) pSource.getEntity());
                    this.level.addFreshEntity(guardian);
                }
                ++this.spawnTimes;
            }
            double d1 = pSource.getEntity().getX() - this.getX();

            double d0;
            for(d0 = pSource.getEntity().getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }
            this.knockback(2.0F, d1, d0);
        }
        return super.hurt(pSource, pAmount);
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    protected ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SACRED_FISH_BUCKET.get());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SALMON_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

}
