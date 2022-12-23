package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.common.entities.ai.SpewingAttackGoal;
import com.Polarice3.Goety.common.entities.neutral.ISpewing;
import com.Polarice3.Goety.common.entities.projectiles.FrostBallEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;

public class DredenMinionEntity extends SummonedEntity implements ISpewing, IRangedAttackMob {
    protected static final DataParameter<Boolean> SPEWING = EntityDataManager.defineId(DredenMinionEntity.class, DataSerializers.BOOLEAN);

    public DredenMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new MoveHelperController(this);
        this.maxUpStep = 1.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new DredenMinionEntity.MoveRandomGoal());
        this.goalSelector.addGoal(4, new DredenMinionEntity.FrostBallGoal(this));
        this.goalSelector.addGoal(4, new SpewingAttackGoal<>(this, 15.0F, 20, 0.025F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPEWING, false);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.25F);
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.25F);
        }
    }

    public void die(DamageSource pCause) {
        super.die(pCause);
        SoundEvent soundevent = SoundEvents.STRAY_DEATH;
        this.playSound(soundevent, 1.0F, 0.25F);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (pSource.getEntity() instanceof StrayEntity || pSource.getEntity() instanceof StrayMinionEntity) {
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return super.canBeAffected(potioneffectIn) && potioneffectIn.getEffect() != ModEffects.HOSTED.get() && potioneffectIn.getEffect() != Effects.MOVEMENT_SLOWDOWN;
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void tick() {
        this.setNoGravity(this.isUnderWater());
        super.tick();
    }

    public void aiStep() {
        super.aiStep();

        boolean flag = this.isSunBurnTick();
        if (flag) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                        this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                    }
                }

                flag = false;
            }

            if (flag) {
                this.setSecondsOnFire(8);
            }
        }

        Vector3d vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        if (isSpewing() && this.isAlive()) {
            if (this.level.isClientSide) {
                Vector3d lookAngle = this.getLookAngle();

                double distance = 0.9;
                double px = this.getX() + lookAngle.x * distance;
                double py = this.getY() + 1.5F + lookAngle.y * distance;
                double pz = this.getZ() + lookAngle.z * distance;

                for (int i = 0; i < 2; i++) {
                    double dx = lookAngle.x;
                    double dy = lookAngle.y;
                    double dz = lookAngle.z;

                    double spread = 5.0D + this.random.nextDouble() * 2.5D;
                    double velocity = 0.6D + this.random.nextDouble() * 0.6D;

                    dx += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dy += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dz += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dx *= velocity;
                    dy *= velocity;
                    dz *= velocity;

                    this.level.addParticle(ParticleTypes.CLOUD, px, py, pz, dx, dy, dz);
                }
            }

            this.playSound(SoundEvents.PLAYER_BREATH, random.nextFloat() * 0.5F, random.nextFloat() * 0.5F);
        }

        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            int f = MathHelper.floor(this.getX());
            int g = MathHelper.floor(this.getY());
            int h = MathHelper.floor(this.getZ());
            if (this.isAlive()) {
                serverWorld.sendParticles(ParticleTypes.CLOUD, this.getRandomX(0.5D), this.getY() + 0.5D, this.getRandomZ(0.5D), 1, (0.5D - this.random.nextDouble()) * 0.15D, (double) 0.01F, (0.5D - this.random.nextDouble()) * 0.15D, (0.5D - this.random.nextDouble()) * 0.15D);
                if (this.level.getBiome(new BlockPos(f, 0, h)).getTemperature(new BlockPos(f, g, h)) > 1.0F && this.level.isDay()) {
                    this.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
                }
                if (this.level.isRainingAt(this.blockPosition()) && this.level.getBiome(this.blockPosition()).shouldSnow(this.level, this.blockPosition())){
                    this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 20, 1, false, false));
                }
            }
        }

    }

    @Override
    public boolean isSpewing() {
        return this.entityData.get(SPEWING);
    }

    @Override
    public void setSpewing(boolean flag) {
        this.entityData.set(SPEWING, flag);
    }

    @Override
    public void doSpewing(Entity target) {
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) target;
            if (livingEntity.hurt(ModDamageSource.frostBreath(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))){
                if (!livingEntity.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100));
                } else {
                    if (this.random.nextFloat() <= 0.01F) {
                        EffectsUtil.amplifyEffect(livingEntity, Effects.MOVEMENT_SLOWDOWN, 100);
                    } else {
                        EffectsUtil.resetDuration(livingEntity, Effects.MOVEMENT_SLOWDOWN, 100);
                    }
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 0.25F);
        double d1 = pTarget.getX() - this.getX();
        double d2 = pTarget.getY(0.5D) - this.getY(0.5D);
        double d3 = pTarget.getZ() - this.getZ();
        FrostBallEntity frostBall = new FrostBallEntity(this.level, this, d1, d2, d3);
        frostBall.setPos(frostBall.getX(), this.getY(0.75F), frostBall.getZ());
        this.level.addFreshEntity(frostBall);
        this.swing(Hand.MAIN_HAND);
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !DredenMinionEntity.this.getMoveControl().hasWanted()
                    && DredenMinionEntity.this.isNoGravity()
                    && DredenMinionEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = DredenMinionEntity.this.blockPosition();

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(DredenMinionEntity.this.random.nextInt(15) - 7, DredenMinionEntity.this.random.nextInt(11) - 5, DredenMinionEntity.this.random.nextInt(15) - 7);
                if (DredenMinionEntity.this.level.getFluidState(blockpos1).isSource() || DredenMinionEntity.this.level.isEmptyBlock(blockpos1)) {
                    DredenMinionEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (DredenMinionEntity.this.getTarget() == null) {
                        DredenMinionEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    public static class MoveHelperController extends MovementController {
        public MoveHelperController(MobEntity mob) {
            super(mob);
        }

        public void tick() {
            if (this.mob.isNoGravity()) {
                if (this.operation == Action.MOVE_TO) {
                    Vector3d vector3d = new Vector3d(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                    double d0 = vector3d.length();
                    if (d0 < this.mob.getBoundingBox().getSize()) {
                        this.operation = Action.WAIT;
                        this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
                    } else {
                        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                        if (this.mob.getTarget() == null) {
                            Vector3d vector3d1 = this.mob.getDeltaMovement();
                            this.mob.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        } else {
                            double d2 = this.mob.getTarget().getX() - this.mob.getX();
                            double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                            this.mob.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        }
                        this.mob.yBodyRot = this.mob.yRot;
                    }

                }
            } else {
                super.tick();
            }
        }
    }

    static class FrostBallGoal extends RangedAttackGoal {
        public DredenMinionEntity ghost;

        public FrostBallGoal(DredenMinionEntity entity) {
            super(entity, 1.0D,  20, 40, 20.0F);
            this.ghost = entity;
        }

        public boolean canUse() {
            if (super.canUse() && this.ghost.getTarget() != null){
                return this.ghost.distanceToSqr(this.ghost.getTarget()) >= 64;
            } else {
                return false;
            }
        }
    }
}
