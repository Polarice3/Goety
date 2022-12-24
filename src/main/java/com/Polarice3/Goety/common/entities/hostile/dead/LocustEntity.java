package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class LocustEntity extends OwnedEntity implements IDeadMob, IFlyingAnimal {
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(LocustEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_LIFE = EntityDataManager.defineId(LocustEntity.class, DataSerializers.BOOLEAN);
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;

    public LocustEntity(EntityType<? extends LocustEntity> p_i225714_1_, World p_i225714_2_) {
        super(p_i225714_1_, p_i225714_2_);
        this.moveControl = new FlyingMovementController(this, 20, true);
        this.lookControl = new LookController(this);
        this.setHostile(true);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
        this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LocustEntity.StingGoal(this, (double)3.0F, true));
        this.goalSelector.addGoal(5, new LesserFollowOwnerGoal(this, 1.25D, 10.0F, 2.0F));
        this.goalSelector.addGoal(8, new LocustEntity.WanderGoal());
        this.targetSelector.addGoal(2, new LocustTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, (double)0.6F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_LIFE, false);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("HasStung", this.hasStung());
        pCompound.putBoolean("LifeSpan", this.getLifespan());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        this.setHasStung(pCompound.getBoolean("HasStung"));
        this.setLifespan(pCompound.getBoolean("LifeSpan"));
    }

    public float getWalkTargetValue(BlockPos pPos, IWorldReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(ModDamageSource.directDesiccate(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, pEntity);
            if (pEntity instanceof LivingEntity) {
                ((LivingEntity)pEntity).setStingerCount(((LivingEntity)pEntity).getStingerCount() + 1);
                int i = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level.getDifficulty() == Difficulty.HARD) {
                    i = 18;
                }

                if (i > 0) {
                    ((LivingEntity)pEntity).addEffect(new EffectInstance(ModEffects.DESICCATE.get(), i * 80, 0));
                }
            }
            this.setHasStung(true);
            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }

        return flag;
    }

    public void desiccateTarget(LivingEntity pEntity) {
    }

    public void tick() {
        super.tick();
        if (this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                IParticleData particleData = new BlockParticleData(ParticleTypes.FALLING_DUST, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                this.spawnFluidParticle(this.level, this.getX() - (double)0.3F, this.getX() + (double)0.3F, this.getZ() - (double)0.3F, this.getZ() + (double)0.3F, this.getY(0.5D), particleData);
            }
        }
        if (MainConfig.DeadSandSpread.get() && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            if (this.random.nextInt(30) == 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockpos = this.blockPosition().below(i);
                    BlockState blockstate = ModBlocks.DEAD_PILE.get().defaultBlockState();
                    if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                        this.level.setBlockAndUpdate(blockpos, blockstate);
                    }
                }
            }
        }

        if (this.getLifespan()){
            boolean flag = true;
            if (this.getTrueOwner() != null){
                for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8))){
                    if (livingEntity == this.getTrueOwner()){
                        flag = false;
                    }
                }
            }
            if (flag){
                if (this.tickCount % 20 == 0){
                    this.hurt(DamageSource.STARVE, 1.0F);
                }
            }
        }
        this.updateRollAmount();
    }

    private void spawnFluidParticle(World pLevel, double p_226397_2_, double p_226397_4_, double p_226397_6_, double p_226397_8_, double pPosY, IParticleData pParticleData) {
        pLevel.addParticle(pParticleData, MathHelper.lerp(pLevel.random.nextDouble(), p_226397_2_, p_226397_4_), pPosY, MathHelper.lerp(pLevel.random.nextDouble(), p_226397_6_, p_226397_8_), 0.0D, 0.0D, 0.0D);
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

    @OnlyIn(Dist.CLIENT)
    public float getRollAmount(float p_226455_1_) {
        return MathHelper.lerp(p_226455_1_, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }

    }

    protected void customServerAiStep() {
        boolean flag = this.hasStung();
        if (flag) {
            ++this.timeSinceSting;
            if (this.timeSinceSting % 20 == 0) {
                this.setHasStung(false);
            }
        } else {
            this.timeSinceSting = 0;
        }

    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            boolean flag = !this.hasStung() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0D;
            this.setRolling(flag);
        }

    }

    public boolean hasStung() {
        return this.getFlag(4);
    }

    private void setHasStung(boolean p_226449_1_) {
        this.setFlag(4, p_226449_1_);
    }

    private boolean isRolling() {
        return this.getFlag(2);
    }

    private void setRolling(boolean p_226452_1_) {
        this.setFlag(2, p_226452_1_);
    }

    private void setFlag(int pFlagId, boolean p_226404_2_) {
        if (p_226404_2_) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | pFlagId));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~pFlagId));
        }

    }

    private boolean getFlag(int pFlagId) {
        return (this.entityData.get(DATA_FLAGS_ID) & pFlagId) != 0;
    }

    public void setLifespan(boolean lifespan){
        this.entityData.set(DATA_LIFE, lifespan);
    }

    public boolean getLifespan(){
        return this.entityData.get(DATA_LIFE);
    }

    protected PathNavigator createNavigation(World pLevel) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, pLevel) {
            public boolean isStableDestination(BlockPos pPos) {
                return !this.level.getBlockState(pPos.below()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
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
        return SoundEvents.BEE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected void jumpInLiquid(ITag<Fluid> pFluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    class StingGoal extends MeleeAttackGoal {
        StingGoal(CreatureEntity p_i225718_2_, double p_i225718_3_, boolean p_i225718_5_) {
            super(p_i225718_2_, p_i225718_3_, p_i225718_5_);
        }

        public boolean canUse() {
            return super.canUse() && !LocustEntity.this.hasStung();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && !LocustEntity.this.hasStung();
        }
    }

    static class LocustTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        LocustTargetGoal(LocustEntity p_i225719_1_) {
            super(p_i225719_1_, LivingEntity.class, 10, true, false, DEAD_TARGETS);
        }

        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }

        public boolean canContinueToUse() {
            boolean flag = this.beeCanTarget();
            if (flag && this.mob.getTarget() != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }

        private boolean beeCanTarget() {
            LocustEntity beeentity = (LocustEntity)this.mob;
            return !beeentity.hasStung();
        }
    }

    class WanderGoal extends Goal {
        WanderGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return LocustEntity.this.navigation.isDone() && LocustEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return LocustEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vector3d vector3d = this.findPos();
            if (vector3d != null) {
                LocustEntity.this.navigation.moveTo(LocustEntity.this.navigation.createPath(new BlockPos(vector3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d findPos() {
            Vector3d vector3d;
            vector3d = LocustEntity.this.getViewVector(0.0F);
            int i = 8;
            Vector3d vector3d2 = RandomPositionGenerator.getAboveLandPos(LocustEntity.this, 8, 7, vector3d, ((float)Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.getAirPos(LocustEntity.this, 8, 4, -2, vector3d, (double)((float)Math.PI / 2F));
        }
    }

}
