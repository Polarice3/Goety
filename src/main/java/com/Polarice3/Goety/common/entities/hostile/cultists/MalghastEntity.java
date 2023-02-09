package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.neutral.OwnedFlyingEntity;
import com.Polarice3.Goety.common.entities.projectiles.ExplosiveProjectileEntity;
import com.Polarice3.Goety.common.entities.projectiles.GrandLavaballEntity;
import com.Polarice3.Goety.common.entities.projectiles.LavaballEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class MalghastEntity extends OwnedFlyingEntity {
    private static final DataParameter<Boolean> DATA_IS_CHARGING = EntityDataManager.defineId(MalghastEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_SWELL_DIR = EntityDataManager.defineId(MalghastEntity.class, DataSerializers.INT);
    private float explosionPower = 1.0F;
    private int oldSwell;
    private int swell;
    private int maxSwell = 15;

    public MalghastEntity(EntityType<? extends OwnedFlyingEntity> type, World p_i48578_2_) {
        super(type, p_i48578_2_);
        this.moveControl = new MalghastEntity.MoveHelperController(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FlyingGoal(this));
        this.goalSelector.addGoal(7, new LookAroundGoal(this));
        this.goalSelector.addGoal(7, new FireballAttackGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isCharging()) {
                this.setSwellDir(1);
            } else {
                this.setSwellDir(-1);
            }

            int i = this.getSwellDir();

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
            }
        }
        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public float getSwelling(float pPartialTicks) {
        return MathHelper.lerp(pPartialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean pAttacking) {
        this.entityData.set(DATA_IS_CHARGING, pAttacking);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int pState) {
        this.entityData.set(DATA_SWELL_DIR, pState);
    }

    public float getExplosionPower() {
        return this.explosionPower;
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return 1.3F;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (pSource.getDirectEntity() instanceof FireballEntity && pSource.getEntity() instanceof PlayerEntity) {
            return super.hurt(pSource, 1000.0F);
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING, false);
        this.entityData.define(DATA_SWELL_DIR, -1);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, MobConfig.MalghastHealth.get())
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    protected float getSoundVolume() {
        return 2.0F;
    }

    protected float getVoicePitch() {
        return 0.75F;
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("ExplosionPower", this.explosionPower);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ExplosionPower", 99)) {
            this.explosionPower = pCompound.getFloat("ExplosionPower");
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setBoundOrigin(this.blockPosition());
        return pSpawnData;
    }

    static class FireballAttackGoal extends Goal {
        private final MalghastEntity ghast;
        public int chargeTime;
        public boolean shotTimes;

        public FireballAttackGoal(MalghastEntity p_i45837_1_) {
            this.ghast = p_i45837_1_;
        }

        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
            this.shotTimes = true;
        }

        public void stop() {
            this.ghast.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = this.ghast.getTarget();
            float d0 = 64.0F;
            if (livingentity.distanceToSqr(this.ghast) < MathHelper.square(d0) && this.ghast.canSee(livingentity)) {
                World world = this.ghast.level;
                ++this.chargeTime;
                if (this.chargeTime == 10) {
                    this.shotTimes = this.ghast.random.nextFloat() >= 0.25F;
                    if (!this.ghast.isSilent()) {
                        if (this.shotTimes) {
                            this.ghast.playSound(SoundEvents.GHAST_WARN, 5.0F, (this.ghast.random.nextFloat() - this.ghast.random.nextFloat()) * 0.2F + this.ghast.getVoicePitch());
                        } else {
                            this.ghast.playSound(SoundEvents.GHAST_SCREAM, 5.0F, (this.ghast.random.nextFloat() - this.ghast.random.nextFloat()) * 0.2F + this.ghast.getVoicePitch());
                        }
                    }
                }

                if (this.chargeTime == 20) {
                    double d1 = 2.0D;
                    Vector3d vector3d = this.ghast.getViewVector(1.0F);
                    double d2 = livingentity.getX() - (this.ghast.getX() + vector3d.x * d1);
                    double d3 = livingentity.getY(0.5D) - this.ghast.getY(0.5D);
                    double d4 = livingentity.getZ() - (this.ghast.getZ() + vector3d.z * d1);
                    if (!this.ghast.isSilent()) {
                        this.ghast.playSound(SoundEvents.GHAST_SHOOT, 5.0F, (this.ghast.random.nextFloat() - this.ghast.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    float power = this.ghast.getExplosionPower() + this.ghast.level.getCurrentDifficultyAt(this.ghast.blockPosition()).getSpecialMultiplier();

                    ExplosiveProjectileEntity fireballentity;

                    if (this.shotTimes) {
                        fireballentity = new GrandLavaballEntity(world, this.ghast, d2, d3, d4);
                    } else {
                        fireballentity = new LavaballEntity(world, this.ghast, d2, d3, d4);
                    }
                    fireballentity.setExplosionPower(power);
                    fireballentity.setDangerous(ForgeEventFactory.getMobGriefingEvent(this.ghast.level, this.ghast));
                    double y = this.ghast.getY() <= livingentity.getEyeY() ? this.ghast.getY(0.5D) : this.ghast.getY();
                    fireballentity.setPos(this.ghast.getX() + vector3d.x * d1, y, fireballentity.getZ() + vector3d.z * d1);
                    world.addFreshEntity(fireballentity);
                    this.ghast.knockback(1.0F, livingentity.getX() - this.ghast.getX(), livingentity.getZ() - this.ghast.getZ());
                    this.chargeTime = -40;
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }

            this.ghast.setCharging(this.chargeTime > 10);
        }
    }

    static class LookAroundGoal extends Goal {
        private final MalghastEntity ghast;

        public LookAroundGoal(MalghastEntity p_i45839_1_) {
            this.ghast = p_i45839_1_;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vector3d vector3d = this.ghast.getDeltaMovement();
                this.ghast.yRot = -((float)MathHelper.atan2(vector3d.x, vector3d.z)) * (180F / (float)Math.PI);
            } else {
                LivingEntity livingentity = this.ghast.getTarget();
                double d1 = livingentity.getX() - this.ghast.getX();
                double d2 = livingentity.getZ() - this.ghast.getZ();
                this.ghast.getLookControl().setLookAt(livingentity, 10.0F, this.ghast.getMaxHeadXRot());
                this.ghast.yRot = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
            }
            this.ghast.yBodyRot = this.ghast.yRot;

        }
    }

    static class MoveHelperController extends MovementController {
        private final MalghastEntity ghast;
        private int floatDuration;

        public MoveHelperController(MalghastEntity p_i45838_1_) {
            super(p_i45838_1_);
            this.ghast = p_i45838_1_;
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                    Vector3d vector3d = new Vector3d(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double d0 = vector3d.length();
                    vector3d = vector3d.normalize();
                    if (this.canReach(vector3d, MathHelper.ceil(d0))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vector3d.scale(0.1D)));
                    } else {
                        this.operation = MovementController.Action.WAIT;
                    }
                }

            }
        }

        private boolean canReach(Vector3d p_220673_1_, int p_220673_2_) {
            AxisAlignedBB axisalignedbb = this.ghast.getBoundingBox();

            for(int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.ghast.level.noCollision(this.ghast, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class FlyingGoal extends Goal {
        private final MalghastEntity ghast;

        public FlyingGoal(MalghastEntity p_i45836_1_) {
            this.ghast = p_i45836_1_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            MovementController moveControl = this.ghast.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            } else {
                double d0 = moveControl.getWantedX() - this.ghast.getX();
                double d1 = moveControl.getWantedY() - this.ghast.getY();
                double d2 = moveControl.getWantedZ() - this.ghast.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.ghast.getRandom();
            float distance = 16.0F;
            BlockPos blockPos = null;
            if (this.ghast.getTrueOwner() != null){
                blockPos = this.ghast.getTrueOwner().blockPosition().above(4);
            } else if (this.ghast.getTarget() != null){
                blockPos = this.ghast.getTarget().blockPosition().above(4);
            } else if (this.ghast.getBoundOrigin() != null){
                blockPos = this.ghast.getBoundOrigin();
            }

            if (blockPos != null) {
                if (this.ghast.distanceToSqr(Vector3d.atCenterOf(blockPos)) < MathHelper.square(distance)) {
                    Vector3d vector3d = Vector3d.atCenterOf(blockPos);
                    double X = this.ghast.getX() + vector3d.x * distance + (random.nextFloat() * 2.0F - 1.0F) * distance;
                    double Y = this.ghast.getY() + vector3d.y * distance + (random.nextFloat() * 2.0F - 1.0F) * distance;
                    double Z = this.ghast.getZ() + vector3d.z * distance + (random.nextFloat() * 2.0F - 1.0F) * distance;

                    this.ghast.getMoveControl().setWantedPosition(X, Y, Z, 0.25D);
                } else {
                    this.ghast.getMoveControl().setWantedPosition(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, 0.25D);
                }
            } else {
                double d0 = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * distance;
                double d1 = this.ghast.getY() + (random.nextFloat() * 2.0F - 1.0F) * distance;
                double d2 = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * distance;
                this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 0.25D);
            }
        }
    }
}
