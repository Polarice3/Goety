package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FelFlyEntity extends SummonedEntity implements IFlyingAnimal {
    private int underWaterTicks;

    public FelFlyEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FelFlyMovementController(this, 20, true);
        this.lookControl = new LookController(this);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
        this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
    }

    public float getWalkTargetValue(BlockPos pPos, IWorldReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new AttackGoal());
        this.goalSelector.addGoal(1, new FeedGoal());
        this.goalSelector.addGoal(8, new WanderGoal());
        this.goalSelector.addGoal(9, new SwimGoal(this));
    }

    private void pathfindRandomlyTowards(BlockPos pPos) {
        Vector3d vector3d = Vector3d.atBottomCenterOf(pPos);
        int i = 0;
        BlockPos blockpos = this.blockPosition();
        int j = (int)vector3d.y - blockpos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int i1 = blockpos.distManhattan(pPos);
        if (i1 < 15) {
            k = i1 / 2;
            l = i1 / 2;
        }

        Vector3d vector3d1 = RandomPositionGenerator.getAirPosTowards(this, k, l, i, vector3d, (double)((float)Math.PI / 10F));
        if (vector3d1 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vector3d1.x, vector3d1.y, vector3d1.z, 1.0D);
        }
    }

    protected void customServerAiStep() {
        if (this.isInWaterOrBubble()) {
            ++this.underWaterTicks;
        } else {
            this.underWaterTicks = 0;
        }

        if (this.underWaterTicks > 20) {
            this.hurt(DamageSource.DROWN, 1.0F);
        }

    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.FLYING_SPEED, (double)0.6F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.6F)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    protected PathNavigator createNavigation(World pLevel) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, pLevel);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.FEL_FLY_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FEL_FLY_DEATH.get();
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return pSize.height * 0.5F;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (pSource.getEntity() != null && pSource.getEntity() instanceof MobEntity) {
                if (this.level.random.nextFloat() > 0.33F) {
                    return !pSource.isExplosion() && !pSource.isBypassArmor() && !pSource.isBypassInvul();
                }
            }

            return super.hurt(pSource, pAmount);
        }
    }

    protected void jumpInLiquid(ITag<Fluid> pFluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    public void lifeSpanDamage(){
        if (this.getTrueOwner() != null && this.getTrueOwner() instanceof RottreantEntity && this.getTrueOwner().isAlive() && !this.getTrueOwner().isDeadOrDying()){
            this.getNavigation().moveTo(this.getTrueOwner(), 1.25F);
            this.setTarget(null);
            if (this.getBoundingBox().inflate(0.25F).intersects(this.getTrueOwner().getBoundingBox())){
                this.playSound(ModSounds.ROT_TREE_ENTER.get(), 1.0F, 1.0F);
                this.getTrueOwner().heal(this.getHealth() / 2);
                this.remove();
            }
        } else {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0F);
        }
    }

    public class AttackGoal extends Goal {
        public int attackTime;

        public AttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return FelFlyEntity.this.getTarget() != null
                    && !FelFlyEntity.this.getTarget().isAlliedTo(FelFlyEntity.this);
        }

        public boolean canContinueToUse() {
            return FelFlyEntity.this.getTarget() != null
                    && FelFlyEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = FelFlyEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            FelFlyEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 2.0D);
            this.attackTime = 0;
        }

        public void stop() {
            LivingEntity livingentity = FelFlyEntity.this.getTarget();
            if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                FelFlyEntity.this.setTarget((LivingEntity)null);
            }
            FelFlyEntity.this.getNavigation().stop();
        }

        public void tick() {
            LivingEntity livingentity = FelFlyEntity.this.getTarget();
            assert livingentity != null;
            if (this.attackTime < this.maxAttackTime()) {
                ++this.attackTime;
            }
            if (FelFlyEntity.this.getBoundingBox().inflate(0.25).intersects(livingentity.getBoundingBox())) {
                if (this.isTimeToAttack()) {
                    if (FelFlyEntity.this.doHurtTarget(livingentity)) {
                        this.resetAttackCooldown();
                    }
                }
            } else {
                Vector3d vector3d = livingentity.getEyePosition(1.0F);
                FelFlyEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 2.0D);
            }

        }

        protected void resetAttackCooldown() {
            this.attackTime = 0;
        }

        protected int maxAttackTime(){
            return 40;
        }

        protected boolean isTimeToAttack() {
            return this.attackTime >= this.maxAttackTime();
        }
    }

    class FeedGoal extends Goal {
        public ItemEntity food;
        public int feedingTime;
        private Vector3d hoverPos;

        FeedGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            for (ItemEntity itemEntity : FelFlyEntity.this.level.getEntitiesOfClass(ItemEntity.class, FelFlyEntity.this.getBoundingBox().inflate(32))){
                if (itemEntity.getItem().isEdible()){
                    this.food = itemEntity;
                }
            }
            return this.food != null && !this.food.removed && FelFlyEntity.this.getTarget() == null;
        }

        public boolean canContinueToUse() {
            return FelFlyEntity.this.getTarget() == null
                    && this.food != null
                    && !this.food.removed;
        }

        private boolean hasEatenLongEnough() {
            return this.feedingTime >= 400;
        }

        public void start() {
            this.feedingTime = 0;
        }

        public void stop() {
            if (this.hasEatenLongEnough()) {
                if (!FelFlyEntity.this.level.isClientSide) {
                    FelFlyEntity.this.addEffect(new EffectInstance(Effects.REGENERATION, 100));
                }
            }
            this.feedingTime = 0;
            FelFlyEntity.this.navigation.stop();
        }

        public void tick() {
            Vector3d vector3d = Vector3d.atBottomCenterOf(this.food.blockPosition());
            if (vector3d.distanceTo(FelFlyEntity.this.position()) > 1.0D) {
                this.hoverPos = vector3d;
                this.setWantedPos();
            } else {
                ++this.feedingTime;
                if (this.hoverPos == null) {
                    this.hoverPos = vector3d;
                }

                boolean flag = FelFlyEntity.this.position().distanceTo(this.hoverPos) <= 0.1D;
                boolean flag1 = true;
                if (flag) {
                    boolean flag2 = FelFlyEntity.this.random.nextInt(25) == 0;
                    if (flag2) {
                        this.hoverPos = new Vector3d(vector3d.x() + (double)this.getOffset(), vector3d.y(), vector3d.z() + (double)this.getOffset());
                        FelFlyEntity.this.navigation.stop();
                    } else {
                        flag1 = false;
                    }

                    FelFlyEntity.this.getLookControl().setLookAt(vector3d.x(), vector3d.y(), vector3d.z());
                }

                if (flag1) {
                    this.setWantedPos();
                }

                if (FelFlyEntity.this.tickCount % 20 == 0) {
                    FelFlyEntity.this.playSound(ModSounds.FEL_FLY_LOOP.get(), FelFlyEntity.this.getSoundVolume(), FelFlyEntity.this.getVoicePitch());
                    FelFlyEntity.this.playSound(SoundEvents.GENERIC_EAT, FelFlyEntity.this.getSoundVolume(), 2.0F);
                    if (!FelFlyEntity.this.level.isClientSide){
                        ServerWorld serverWorld = (ServerWorld) FelFlyEntity.this.level;
                        BlockPos blockPos = FelFlyEntity.this.blockPosition();
                        if (this.food != null && !this.food.removed) {
                            serverWorld.sendParticles(new ItemParticleData(ParticleTypes.ITEM, this.food.getItem()), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 0, 0, 0, 0);
                        }
                    }
                }
            }
            if (this.feedingTime > 400 && this.food != null) {
                this.food.remove();
            }
        }

        private void setWantedPos() {
            FelFlyEntity.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), (double)2.0F);
        }

        private float getOffset() {
            return (FelFlyEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

    }

    class WanderGoal extends Goal {
        WanderGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return FelFlyEntity.this.navigation.isDone() && FelFlyEntity.this.getTarget() == null && FelFlyEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return FelFlyEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vector3d vector3d = this.findPos();
            if (vector3d != null) {
                FelFlyEntity.this.navigation.moveTo(FelFlyEntity.this.navigation.createPath(new BlockPos(vector3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d findPos() {
            Vector3d vector3d = FelFlyEntity.this.getViewVector(0.0F);

            Vector3d vector3d2 = RandomPositionGenerator.getAboveLandPos(FelFlyEntity.this, 8, 7, vector3d, ((float)Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.getAirPos(FelFlyEntity.this, 8, 4, -2, vector3d, (double)((float)Math.PI / 2F));
        }
    }

    static class FelFlyMovementController extends MovementController {
        private final int maxTurn;
        private final boolean hoversInPlace;

        public FelFlyMovementController(MobEntity p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
            super(p_i225710_1_);
            this.maxTurn = p_i225710_2_;
            this.hoversInPlace = p_i225710_3_;
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                this.operation = MovementController.Action.WAIT;
                this.mob.setNoGravity(true);
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedY - this.mob.getY();
                double d2 = this.wantedZ - this.mob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.yRot = this.rotlerp(this.mob.yRot, f, 90.0F);
                float f1;
                if (this.mob.isOnGround()) {
                    f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }

                this.mob.setSpeed(f1);
                double d4 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float) (-(MathHelper.atan2(d1, d4) * (double) (180F / (float) Math.PI)));
                this.mob.xRot = this.rotlerp(this.mob.xRot, f2, (float) this.maxTurn);
                this.mob.setYya(d1 > 0.0D ? f1/4 : -f1/4);
            } else {
                if (!this.hoversInPlace) {
                    this.mob.setNoGravity(false);
                }

                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }

        }
    }

}
