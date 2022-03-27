package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarrotBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.JumpController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MutatedRabbitEntity extends MutatedEntity {
    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(MutatedRabbitEntity.class, DataSerializers.INT);
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;
    private int moreCarrotTicks;
    private boolean spread;

    public MutatedRabbitEntity(EntityType<? extends MutatedEntity> type, World worldIn) {
        super(type, worldIn);
        this.jumpControl = new JumpHelperController(this);
        this.moveControl = new MoveHelperController(this);
        this.setSpeedModifier(0.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new EvilAttackGoal(this));
        this.goalSelector.addGoal(5, new RaidFarmGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(11, new LookAtGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D);
    }

    protected float getJumpPower() {
        if (!this.horizontalCollision && (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.5D))) {
            Path path = this.navigation.getPath();
            if (path != null && !path.isDone()) {
                Vector3d vector3d = path.getNextEntityPos(this);
                if (vector3d.y > this.getY() + 0.75D) {
                    return 0.75F;
                }
            }

            return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.75F;
        }
    }

    protected void jumpFromGround() {
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if (d0 > 0.0D) {
            double d1 = getHorizontalDistanceSqr(this.getDeltaMovement());
            if (d1 < 0.02D) {
                this.moveRelative(0.2F, new Vector3d(0.0D, 0.0D, 2.0D));
            }
        }

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)1);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public float getJumpCompletion(float p_175521_1_) {
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
    }

    public void setSpeedModifier(double pNewSpeed) {
        this.getNavigation().setSpeedModifier(pNewSpeed);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), pNewSpeed);
    }

    public void setJumping(boolean pJumping) {
        super.setJumping(pJumping);
        if (pJumping) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

    }

    public void setSpread(boolean state) {
        this.spread = state;
    }

    public boolean canSpread() {
        return spread;
    }

    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
    }

    public int getRabbitType() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setRabbitType(int pRabbitTypeId) {
        this.entityData.set(DATA_TYPE_ID, pRabbitTypeId);
    }

    public void customServerAiStep() {
        List<MutatedRabbitEntity> pop = this.level.getNearbyEntities(MutatedRabbitEntity.class, new EntityPredicate().range(32.0D), this, this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D));
        this.setSpread(pop.size() <= MainConfig.MRabbitMax.get());

        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }

        if (this.moreCarrotTicks > 0) {
            this.moreCarrotTicks -= this.random.nextInt(3);
            if (this.moreCarrotTicks < 0) {
                this.moreCarrotTicks = 0;
            }
        }

        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            if (this.jumpDelayTicks == 0) {
                LivingEntity livingentity = this.getTarget();
                if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
                    this.facePoint(livingentity.getX(), livingentity.getZ());
                    this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }

            JumpHelperController rabbitentity$jumphelpercontroller = (JumpHelperController)this.jumpControl;
            if (!rabbitentity$jumphelpercontroller.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vector3d vector3d = new Vector3d(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        vector3d = path.getNextEntityPos(this);
                    }

                    this.facePoint(vector3d.x, vector3d.z);
                    this.startJumping();
                }
            } else if (!rabbitentity$jumphelpercontroller.canJump()) {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround;
    }

    public boolean canSpawnSprintParticle() {
        return false;
    }

    private void facePoint(double pX, double pZ) {
        this.yRot = (float)(MathHelper.atan2(pZ - this.getZ(), pX - this.getX()) * (double)(180F / (float)Math.PI)) - 90.0F;
    }

    private void enableJumpControl() {
        ((JumpHelperController)this.jumpControl).setCanJump(true);
    }

    private void disableJumpControl() {
        ((JumpHelperController)this.jumpControl).setCanJump(false);
    }

    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2D) {
            this.jumpDelayTicks = 10;
        } else {
            this.jumpDelayTicks = 1;
        }

    }

    private void checkLandingDelay() {
        this.setLandingDelay();
        this.disableJumpControl();
    }

    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }

    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("RabbitType", this.getRabbitType());
        pCompound.putInt("MoreCarrotTicks", this.moreCarrotTicks);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setRabbitType(pCompound.getInt("RabbitType"));
        this.moreCarrotTicks = pCompound.getInt("MoreCarrotTicks");
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    public boolean doHurtTarget(Entity pEntity) {
        this.playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        if (pEntity instanceof CreeperEntity){
            return pEntity.hurt(DamageSource.mobAttack(this), 1000.0F);
        } else if (pEntity instanceof MutatedRabbitEntity) {
            return pEntity.hurt(DamageSource.mobAttack(this), 2.0F);
        } else {
            return pEntity.hurt(DamageSource.mobAttack(this), 8.0F);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        int random = this.level.random.nextInt(16);
        if (this.canSpread()) {
            if (random == 0) {
                MutatedRabbitEntity irk = new MutatedRabbitEntity(ModEntityType.MUTATED_RABBIT.get(), this.level);
                irk.setPos(this.getX(), this.getY(), this.getZ());
                int i = this.getRandomRabbitType(this.level);
                irk.setRabbitType(i);
                this.level.addFreshEntity(irk);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    private int getRandomRabbitType(IWorld p_213610_1_) {
        Biome biome = p_213610_1_.getBiome(this.blockPosition());
        int i = this.random.nextInt(100);
        if (biome.getPrecipitation() == Biome.RainType.SNOW) {
            return i < 80 ? 1 : 3;
        } else if (biome.getBiomeCategory() == Biome.Category.DESERT) {
            return 4;
        } else {
            return i < 50 ? 0 : (i < 90 ? 5 : 2);
        }
    }

    private boolean wantsMoreFood() {
        return this.moreCarrotTicks == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 1) {
            this.spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        if (source.getEntity() instanceof PlayerEntity) {
            int random = this.level.random.nextInt(4 - looting);
            int random2 = this.level.random.nextInt(8 - looting);
            if (random == 1) {
                this.spawnAtLocation(ModRegistry.MUTATED_RABBIT_UNCOOKED.get());
                for (int i = 0; i < 4 + this.level.random.nextInt(8); ++i) {
                    this.spawnAtLocation(Items.RABBIT_HIDE);
                }
            } else {
                for (int i = 0; i < 4 + this.level.random.nextInt(8); ++i) {
                    this.spawnAtLocation(Items.ROTTEN_FLESH);
                }
                for (int i = 0; i < 2 + this.level.random.nextInt(2); ++i) {
                    this.spawnAtLocation(Items.RABBIT_HIDE);
                }
            }
            if (random2 == 0) {
                this.spawnAtLocation(Items.RABBIT_FOOT);
            }
        }

    }


    public class JumpHelperController extends JumpController {
        private final MutatedRabbitEntity rabbit;
        private boolean canJump;

        public JumpHelperController(MutatedRabbitEntity p_i45863_2_) {
            super(p_i45863_2_);
            this.rabbit = p_i45863_2_;
        }

        public boolean wantJump() {
            return this.jump;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean pCanJump) {
            this.canJump = pCanJump;
        }

        public void tick() {
            if (this.jump) {
                this.rabbit.startJumping();
                this.jump = false;
            }

        }
    }

    static class MoveHelperController extends MovementController {
        private final MutatedRabbitEntity rabbit;
        private double nextJumpSpeed;

        public MoveHelperController(MutatedRabbitEntity p_i45862_1_) {
            super(p_i45862_1_);
            this.rabbit = p_i45862_1_;
        }

        public void tick() {
            if (this.rabbit.onGround && !this.rabbit.jumping && !((JumpHelperController)this.rabbit.jumpControl).wantJump()) {
                this.rabbit.setSpeedModifier(0.0D);
            } else if (this.hasWanted()) {
                this.rabbit.setSpeedModifier(this.nextJumpSpeed);
            }

            super.tick();
        }

        /**
         * Sets the speed and location to move to
         */
        public void setWantedPosition(double pX, double pY, double pZ, double pSpeed) {
            if (this.rabbit.isInWater()) {
                pSpeed = 1.5D;
            }

            super.setWantedPosition(pX, pY, pZ, pSpeed);
            if (pSpeed > 0.0D) {
                this.nextJumpSpeed = pSpeed;
            }

        }
    }

    static class EvilAttackGoal extends MeleeAttackGoal {
        public EvilAttackGoal(MutatedRabbitEntity p_i45867_1_) {
            super(p_i45867_1_, 1.4D, true);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(4.0F + pAttackTarget.getBbWidth());
        }
    }

    static class RaidFarmGoal extends MoveToBlockGoal {
        private final MutatedRabbitEntity rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;

        public RaidFarmGoal(MutatedRabbitEntity p_i45860_1_) {
            super(p_i45860_1_, (double)0.7F, 16);
            this.rabbit = p_i45860_1_;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.nextStartTick <= 0) {
                if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.rabbit.level, this.rabbit)) {
                    return false;
                }

                this.canRaid = false;
                this.wantsToRaid = this.rabbit.wantsMoreFood();
            }

            return super.canUse();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.canRaid && super.canContinueToUse();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            super.tick();
            this.rabbit.getLookControl().setLookAt((double)this.blockPos.getX() + 0.5D, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + 0.5D, 10.0F, (float)this.rabbit.getMaxHeadXRot());
            if (this.isReachedTarget()) {
                World world = this.rabbit.level;
                BlockPos blockpos = this.blockPos.above();
                BlockState blockstate = world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (this.canRaid && block instanceof CarrotBlock) {
                    Integer integer = blockstate.getValue(CarrotBlock.AGE);
                    if (integer == 0) {
                        world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                        world.destroyBlock(blockpos, true, this.rabbit);
                    } else {
                        world.setBlock(blockpos, blockstate.setValue(CarrotBlock.AGE, Integer.valueOf(integer - 1)), 2);
                        world.levelEvent(2001, blockpos, Block.getId(blockstate));
                    }

                    this.rabbit.moreCarrotTicks = 40;
                }

                this.canRaid = false;
                this.nextStartTick = 10;
            }

        }

        protected boolean isValidTarget(IWorldReader pLevel, BlockPos pPos) {
            Block block = pLevel.getBlockState(pPos).getBlock();
            if (block == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
                pPos = pPos.above();
                BlockState blockstate = pLevel.getBlockState(pPos);
                block = blockstate.getBlock();
                if (block instanceof CarrotBlock && ((CarrotBlock)block).isMaxAge(blockstate)) {
                    this.canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

}
