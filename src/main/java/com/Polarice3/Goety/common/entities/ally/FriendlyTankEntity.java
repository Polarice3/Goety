package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.common.entities.neutral.AbstractTankEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class FriendlyTankEntity extends AbstractTankEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(FriendlyTankEntity.class, DataSerializers.OPTIONAL_UUID);
    public int attackTimer;
    public int attackTimes = 0;
    public int attackStep = 0;
    public boolean FireCharging;
    private boolean sitting;

    public FriendlyTankEntity(EntityType<? extends AbstractTankEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.maxUpStep = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 256.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new RangeAttackGoal());
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 20.0F, 4.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob;
        }));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        compound.putBoolean("Sitting", this.sitting);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);

            } catch (Throwable ignored) {

            }
        }

        this.sitting = compound.getBoolean("Sitting");
        this.setSitting(this.sitting);
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void setOwner(PlayerEntity player) {
        this.setOwnerId(player.getUUID());

    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public Team getTeam() {
        if (this.getOwner() != null) {
            LivingEntity livingentity = this.getOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.getOwner() != null) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }
        if (entityIn instanceof FriendlyTankEntity && ((FriendlyTankEntity) entityIn).getOwner() == this.getOwner()){
            return true;
        }
        if (entityIn instanceof SummonedEntity && ((SummonedEntity) entityIn).getTrueOwner() == this.getOwner()){
            return true;
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isQueuedToSit()) {
            return SoundEvents.FIRE_AMBIENT;
        } else {
            return null;
        }
    }

    public boolean isOwner(LivingEntity entityIn) {
        return entityIn == this.getOwner();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ANVIL_LAND;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.25F, 1.0F);
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return super.isControlledByLocalInstance() && this.canBeControlledByRider();
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractArrowEntity) {
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public boolean isQueuedToSit() {
        return this.sitting;
    }

    public void setSitting(boolean p_233687_1_) {
        this.sitting = p_233687_1_;
    }

    public void aiStep() {
            if (this.level.isClientSide) {
                if (!this.isQueuedToSit()) {
                    for (int i = 0; i < 2; ++i) {
                        this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            if (this.FireCharging){
                this.FireCharge();
            }

        super.aiStep();
    }

    public Cracks func_226512_l_() {
        return Cracks.func_226515_a_(this.getHealth() / this.getMaxHealth());
    }

    protected void mountTo(PlayerEntity player) {
        if (!this.level.isClientSide) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);
        }
    }

    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                if (this.attackTimer < 20) {
                    ++this.attackTimer;
                }
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                this.yRot = livingentity.yRot;
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vector3d((double) f, travelVector.y, (double) f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setDeltaMovement(Vector3d.ZERO);
                }

                if (livingentity.swingTime == -1) {
                    if (this.attackTimes < 3) {
                        if (this.attackTimer >= 20) {
                            World world = FriendlyTankEntity.this.level;
                            Vector3d vector3d = FriendlyTankEntity.this.getViewVector( 1.0F);
                            Random random = level.random;
                            double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
                            double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
                            double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
                            FireballEntity fireballentity = new FireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                            fireballentity.setPos(FriendlyTankEntity.this.getX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getY(0.5D) + 0.25D, fireballentity.getZ() + vector3d.z * 2.0D);
                            level.addFreshEntity(fireballentity);
                            if (!FriendlyTankEntity.this.isSilent()) {
                                FriendlyTankEntity.this.level.levelEvent(null, 1016, FriendlyTankEntity.this.blockPosition(), 0);
                            }
                            this.attackTimer = 0;
                            this.attackTimes = this.attackTimes + 1;
                        }
                    } else {
                        this.FireCharging = true;
                    }
                }
                for (Entity entity : FriendlyTankEntity.this.level.getEntitiesOfClass(LivingEntity.class, FriendlyTankEntity.this.getBoundingBox().inflate(1.5D), field_213690_b)) {
                    if (!(entity instanceof PlayerEntity)) {
                        FriendlyTankEntity.this.doHurtTarget(entity);
                        this.launch(entity);
                    }
                }
            }

            this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(travelVector);
            }
    }

    public void FireCharge(){
        if (this.attackTimer >= 20) {
            ++this.attackStep;
            World world = FriendlyTankEntity.this.level;
            Vector3d vector3d = FriendlyTankEntity.this.getViewVector( 1.0F);
            Random random = level.random;
            double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
            double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
            double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
            SmallFireballEntity fireballentity = new SmallFireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
            fireballentity.setPos(FriendlyTankEntity.this.getX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getY(0.5D) + 0.25D, fireballentity.getZ() + vector3d.z * 2.0D);
            level.addFreshEntity(fireballentity);
            if (!FriendlyTankEntity.this.isSilent()) {
                FriendlyTankEntity.this.level.levelEvent(null, 1016, FriendlyTankEntity.this.blockPosition(), 0);
            }
            if (this.attackStep >= 20) {
                this.attackTimes = 0;
                this.attackTimer = 0;
                this.attackStep = 0;
                this.FireCharging = false;
            }
        }
    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - FriendlyTankEntity.this.getX();
        double d1 = p_213688_1_.getZ() - FriendlyTankEntity.this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
    }


    public int speed = 0;

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        if (this.getOwner() != null && !p_230254_1_.isCrouching() && item != Items.REDSTONE_BLOCK && item != Items.IRON_INGOT  && !this.isQueuedToSit()) {
            this.mountTo(p_230254_1_);
            return p_230254_1_.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
        }
        if (item != Items.IRON_INGOT) {
            if (item == Items.COMPARATOR){
                if (this.getOwner() != null) {return ActionResultType.PASS;}
                else {
                    if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                    }
                    this.setOwner(p_230254_1_);
                    this.navigation.stop();
                    this.setTarget((LivingEntity)null);
                    this.setSitting(true);
                    this.level.broadcastEntityEvent(this, (byte)7);
                    return ActionResultType.SUCCESS;
                }
            } else {
                if (item == Items.REDSTONE_BLOCK && speed <= 8){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    speed = speed + 1;
                    for(int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    this.playSound(SoundEvents.ANVIL_USE,1.0f,1.0f);
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + 0.025D);
                    return ActionResultType.SUCCESS;
                } else {
                    if (p_230254_1_.isCrouching() && this.getOwner() != null){
                        ActionResultType actionresulttype = super.mobInteract(p_230254_1_, p_230254_2_);
                        if (!actionresulttype.consumesAction() || this.isOwner(p_230254_1_)) {
                            for(int i = 0; i < 7; ++i) {
                                double d0 = this.random.nextGaussian() * 0.02D;
                                double d1 = this.random.nextGaussian() * 0.02D;
                                double d2 = this.random.nextGaussian() * 0.02D;
                                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                            }
                            this.setSitting(!this.isQueuedToSit());
                            this.jumping = false;
                            this.navigation.stop();
                            this.setTarget((LivingEntity)null);
                            return ActionResultType.SUCCESS;
                        }
                        return actionresulttype;
                    } else {
                        return ActionResultType.PASS;
                    }
                }
            }
        } else {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() == f) {
                return ActionResultType.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.yRot);
        float f = MathHelper.wrapDegrees(entityToUpdate.yRot - this.yRot);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.yRot += f1 - f;
        entityToUpdate.setYHeadRot(entityToUpdate.yRot);
    }

    @OnlyIn(Dist.CLIENT)
    public void onPassengerTurned(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    class RangeAttackGoal extends Goal{
        public int attackTimer;
        public int attackTimes = 0;

        @Override
        public boolean canUse() {
            if (FriendlyTankEntity.this.getTarget() == null) {
                return false;
            } else if (FriendlyTankEntity.this.isQueuedToSit()){
                return false;
            } else if (FriendlyTankEntity.this.isVehicle()){
                return false;
            }
            else {
                return true;
            }
        }

        public void start() {
            this.attackTimer = 0;
        }

        public void tick() {
            LivingEntity livingentity = FriendlyTankEntity.this.getTarget();
            assert livingentity != null;
            if (livingentity.distanceToSqr(FriendlyTankEntity.this) < 4096.0D && FriendlyTankEntity.this.canSee(livingentity)) {
                FriendlyTankEntity.this.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                ++this.attackTimer;
                World world = FriendlyTankEntity.this.level;
                if (this.attackTimes < 3) {
                    if (this.attackTimer >= 20) {
                        this.attackTimes = this.attackTimes + 1;
                        Vector3d vector3d = FriendlyTankEntity.this.getViewVector( 1.0F);
                        double d2 = livingentity.getX() - (FriendlyTankEntity.this.getX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getY(0.5D) - (0.5D + FriendlyTankEntity.this.getY(0.5D));
                        double d4 = livingentity.getZ() - (FriendlyTankEntity.this.getZ() + vector3d.z * 2.0D);
                        FireballEntity fireballentity = new FireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                        fireballentity.setPos(FriendlyTankEntity.this.getX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getY(0.5D) + 0.25D, fireballentity.getZ() + vector3d.z * 2.0D);
                        level.addFreshEntity(fireballentity);
                        if (!FriendlyTankEntity.this.isSilent()) {
                            FriendlyTankEntity.this.level.levelEvent(null, 1016, FriendlyTankEntity.this.blockPosition(), 0);
                        }
                        this.attackTimer = -40;
                    }
                } else {
                    if (this.attackTimer >= 20) {
                        Vector3d vector3d = FriendlyTankEntity.this.getViewVector( 1.0F);
                        double d2 = livingentity.getX() - (FriendlyTankEntity.this.getX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getY(0.5D) - (0.5D + FriendlyTankEntity.this.getY(0.5D));
                        double d4 = livingentity.getZ() - (FriendlyTankEntity.this.getZ() + vector3d.z * 2.0D);
                        SmallFireballEntity smallfireballentity = new SmallFireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                        smallfireballentity.setPos(FriendlyTankEntity.this.getX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getY(0.5D) + 0.25D, smallfireballentity.getZ() + vector3d.z * 2.0D);
                        level.addFreshEntity(smallfireballentity);
                        if (!FriendlyTankEntity.this.isSilent()) {
                            FriendlyTankEntity.this.level.levelEvent(null, 1016, FriendlyTankEntity.this.blockPosition(), 0);
                        }
                        if (this.attackTimer >= 40) {
                            this.attackTimes = 0;
                            this.attackTimer = -80;
                        }
                    }
                }
                double d0 = FriendlyTankEntity.this.distanceToSqr(livingentity);
                if (d0 > 16.0D) {
                    FriendlyTankEntity.this.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }
                for (Entity entity : FriendlyTankEntity.this.level.getEntitiesOfClass(LivingEntity.class, FriendlyTankEntity.this.getBoundingBox().inflate(1.5D), field_213690_b)) {
                    if (!(entity instanceof PlayerEntity)) {
                        FriendlyTankEntity.this.doHurtTarget(entity);
                        this.launch(entity);
                    }
                }
            }
        }
            private void launch(Entity p_213688_1_) {
                double d0 = p_213688_1_.getX() - FriendlyTankEntity.this.getX();
                double d1 = p_213688_1_.getZ() - FriendlyTankEntity.this.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                p_213688_1_.push(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
            }

    }

    public class SitGoal extends Goal {
        private final FriendlyTankEntity friendlyTankEntity;

        public SitGoal(FriendlyTankEntity entityIn) {
            this.friendlyTankEntity = entityIn;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.friendlyTankEntity.isQueuedToSit();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.friendlyTankEntity.getOwner() == null) {
                return false;
            } else if (this.friendlyTankEntity.isInWaterOrBubble()) {
                return false;
            } else if (!this.friendlyTankEntity.isOnGround()) {
                return false;
            } else {
                LivingEntity livingentity = this.friendlyTankEntity.getOwner();
                if (livingentity == null) {
                    return true;
                } else {
                    return (!(this.friendlyTankEntity.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null) && this.friendlyTankEntity.isQueuedToSit();
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.friendlyTankEntity.getNavigation().stop();
            this.friendlyTankEntity.setSitting(true);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.friendlyTankEntity.setSitting(false);
        }
    }

    public class FollowOwnerGoal extends Goal {
        private final FriendlyTankEntity friendlyTankEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(FriendlyTankEntity hireable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.friendlyTankEntity = hireable;
            this.level = hireable.level;
            this.followSpeed = speed;
            this.navigation = hireable.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(hireable.getNavigation() instanceof GroundPathNavigator) && !(hireable.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.friendlyTankEntity.getOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.friendlyTankEntity.isQueuedToSit()) {
                return false;
            } else if (this.friendlyTankEntity.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.friendlyTankEntity.isQueuedToSit()) {
                return false;
            } else {
                return !(this.friendlyTankEntity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.friendlyTankEntity.getPathfindingMalus(PathNodeType.WATER);
            this.friendlyTankEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.friendlyTankEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.friendlyTankEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.friendlyTankEntity.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.friendlyTankEntity.isLeashed() && !this.friendlyTankEntity.isPassenger()) {
                    if (this.friendlyTankEntity.distanceToSqr(this.owner) >= 144.0D) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.navigation.moveTo(this.owner, this.followSpeed);
                    }

                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.blockPosition();

            for(int i = 0; i < 10; ++i) {
                int j = this.getRandomNumber(-3, 3);
                int k = this.getRandomNumber(-1, 1);
                int l = this.getRandomNumber(-3, 3);
                boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
                if (flag) {
                    return;
                }
            }

        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {
            if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.friendlyTankEntity.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.friendlyTankEntity.yRot, this.friendlyTankEntity.xRot);
                this.navigation.stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.level, pos.mutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.level.getBlockState(pos.below());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.friendlyTankEntity.blockPosition());
                    return this.level.noCollision(this.friendlyTankEntity, this.friendlyTankEntity.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.friendlyTankEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }

    class OwnerHurtTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(FriendlyTankEntity friendlyTankEntity) {
            super(friendlyTankEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = FriendlyTankEntity.this.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != FriendlyTankEntity.this.getOwner();
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = FriendlyTankEntity.this.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtMobTimestamp();
            }

            super.start();
        }
    }

    class OwnerHurtByTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(FriendlyTankEntity friendlyTankEntity) {
            super(friendlyTankEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = FriendlyTankEntity.this.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != FriendlyTankEntity.this.getOwner();
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = FriendlyTankEntity.this.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }

    public static enum Cracks {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<Cracks> field_226513_e_ = Stream.of(values()).sorted(Comparator.comparingDouble((p_226516_0_) -> {
            return (double)p_226516_0_.field_226514_f_;
        })).collect(ImmutableList.toImmutableList());
        private final float field_226514_f_;

        private Cracks(float p_i225732_3_) {
            this.field_226514_f_ = p_i225732_3_;
        }

        public static Cracks func_226515_a_(float p_226515_0_) {
            for(Cracks tankentity$cracks : field_226513_e_) {
                if (p_226515_0_ < tankentity$cracks.field_226514_f_) {
                    return tankentity$cracks;
                }
            }

            return NONE;
        }
    }

}
