package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class HogLordEntity extends MonsterEntity implements ICultistMinion, IFlinging {
    private int attackAnimationRemainingTicks;
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(HogLordEntity.class, DataSerializers.OPTIONAL_UUID);

    public HogLordEntity(EntityType<? extends HogLordEntity> p_i231569_1_, World p_i231569_2_) {
        super(p_i231569_1_, p_i231569_2_);
        this.xpReward = 5;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this).setAlertOthers()));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (!(pEntity instanceof LivingEntity)) {
            return false;
        } else {
            this.attackAnimationRemainingTicks = 10;
            this.level.broadcastEntityEvent(this, (byte)4);
            this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, this.getVoicePitch());
            pEntity.setSecondsOnFire(5);
            return IFlinging.hurtAndThrowTarget(this, (LivingEntity)pEntity);
        }
    }

    protected void blockedByShield(LivingEntity pEntity) {
        IFlinging.throwTarget(this, pEntity);
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

    private void teleport(double pX, double pY, double pZ) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pX, pY, pZ);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }

    private void teleportTowards(Entity entity) {
        Vector3d vector3d = new Vector3d(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
        vector3d = vector3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * d0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
        this.teleport(d1, d2, d3);
    }

    public void aiStep() {
        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }

        if (this.getTrueOwner() != null){
            if (!this.getTrueOwner().isDeadOrDying()) {
                if (this.distanceTo(this.getTrueOwner()) >= 32) {
                    this.teleportTowards(this.getTrueOwner());
                }
            }
        }

        if (this.isInWater()){
            this.hurt(DamageSource.GENERIC, 2.0F);
        }

        for (ZoglinEntity zoglinEntity : this.level.getEntitiesOfClass(ZoglinEntity.class, this.getBoundingBox().inflate(32.0D))){
            Brain<ZoglinEntity> brain = zoglinEntity.getBrain();
            if (brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && brain.getMemory(MemoryModuleType.ATTACK_TARGET).get() == this){
                brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            }
            if (this.getTarget() != null){
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.getTarget(), 200L);
            }
        }

        for (HoglinEntity hoglinEntity : this.level.getEntitiesOfClass(HoglinEntity.class, this.getBoundingBox().inflate(32.0D))){
            Brain<HoglinEntity> brain = hoglinEntity.getBrain();
            if (this.getTarget() != null){
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.getTarget(), 200L);
            }

        }

        super.aiStep();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            this.attackAnimationRemainingTicks = 10;
            this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, this.getVoicePitch());
        } else {
            super.handleEntityEvent(pId);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationRemainingTicks;
    }

    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null){
            return SoundEvents.HOGLIN_ANGRY;
        } else {
            return SoundEvents.HOGLIN_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.HOGLIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HOGLIN_DEATH;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
    }

}
