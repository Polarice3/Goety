package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

public class UndeadWolfEntity extends SummonedEntity{
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(UndeadWolfEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_INTERESTED_ID = EntityDataManager.defineId(UndeadWolfEntity.class, DataSerializers.BOOLEAN);
    private float interestedAngle;
    private float interestedAngleO;
    private boolean orderedToSit;

    public UndeadWolfEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void tick() {
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 2.0F);
        }
        if (this.isAlive()) {
            this.interestedAngleO = this.interestedAngle;
            if (this.isInterested()) {
                this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
            } else {
                this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
            }
        }
        super.tick();
    }

    @Override
    protected boolean isSunSensitive() {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INTERESTED_ID, false);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        if (this.isAggressive()) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {
            return SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WOLF_HURT;
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.5F);
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.5F);
        }
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return pSize.height * 0.8F;
    }

    public void setIsInterested(boolean pBeg) {
        this.entityData.set(DATA_INTERESTED_ID, pBeg);
    }

    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Sitting", this.orderedToSit);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.orderedToSit = pCompound.getBoolean("Sitting");
        this.setInSittingPose(this.orderedToSit);
    }

    public boolean isInSittingPose() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean p_233686_1_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_233686_1_) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
        }

    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean p_233687_1_) {
        this.orderedToSit = p_233687_1_;
    }

    @OnlyIn(Dist.CLIENT)
    public float getHeadRollAngle(float pPartialTicks) {
        return MathHelper.lerp(pPartialTicks, this.interestedAngleO, this.interestedAngle) * 0.15F * (float)Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getBodyRollAngle(float pPartialTicks, float pOffset) {
        float f = (MathHelper.lerp(pPartialTicks, 0.0F, 0.0F) + pOffset) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getTailAngle() {
        if (this.isAggressive()) {
            return 1.5393804F;
        } else {
            return (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI;
        }
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entityIn.setSecondsOnFire(2 * (int)f);
            }
        }

        return flag;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.setOrderedToSit(false);
            return super.hurt(pSource, pAmount);
        }
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public void killed(ServerWorld world, LivingEntity killedEntity) {
        super.killed(world, killedEntity);
        int random = this.level.random.nextInt(2);
        if (this.isUpgraded() && killedEntity instanceof WolfEntity && random == 1 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(killedEntity, ModEntityType.UNDEAD_WOLF_MINION.get(), (timer) -> {})) {
            WolfEntity zombieEntity = (WolfEntity)killedEntity;
            UndeadWolfEntity zombieMinionEntity = zombieEntity.convertTo(ModEntityType.UNDEAD_WOLF_MINION.get(), false);
            zombieMinionEntity.finalizeSpawn(world, level.getCurrentDifficultyAt(zombieMinionEntity.blockPosition()), SpawnReason.CONVERSION, null, (CompoundNBT)null);
            if (this.getTrueOwner() != null){
                zombieMinionEntity.setOwnerId(this.getTrueOwner().getUUID());
            }
            zombieMinionEntity.setLimitedLife(10 * (15 + this.level.random.nextInt(45)));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(killedEntity, zombieMinionEntity);
            if (!this.isSilent()) {
                world.levelEvent((PlayerEntity)null, 1026, this.blockPosition(), 0);
            }
        }
        if (killedEntity instanceof AbstractSkeletonEntity){
            this.heal(2.0F);
        }

    }

    public void die(DamageSource pCause) {
        SoundEvent soundevent = SoundEvents.WOLF_DEATH;
        this.playSound(soundevent, 1.0F, 0.5F);
        if (!this.level.isClientSide && this.hasCustomName() && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayerEntity) {
            this.getTrueOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        super.die(pCause);
    }

    public boolean canBeLeashed(PlayerEntity pPlayer) {
        return !this.isAggressive() && super.canBeLeashed(pPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        if (!this.level.isClientSide){
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Item item = itemstack.getItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if (item == Items.ROTTEN_FLESH && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.heal(2.0F);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.CONSUME;
                } else if (RobeArmorFinder.FindNecroSet(pPlayer)){
                    ActionResultType actionresulttype = super.mobInteract(pPlayer, pHand);
                    if ((!actionresulttype.consumesAction() || this.isBaby())) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget((LivingEntity)null);
                        return ActionResultType.SUCCESS;
                    }
                    return actionresulttype;
                } else {
                    return ActionResultType.PASS;
                }
            } else {
                return ActionResultType.PASS;
            }
        } else {
            return ActionResultType.PASS;
        }
    }

    static class BegGoal extends Goal {
        private final UndeadWolfEntity wolf;
        private PlayerEntity player;
        private final World level;
        private final float lookDistance;
        private int lookTime;
        private final EntityPredicate begTargeting;

        public BegGoal(UndeadWolfEntity p_i1617_1_, float p_i1617_2_) {
            this.wolf = p_i1617_1_;
            this.level = p_i1617_1_.level;
            this.lookDistance = p_i1617_2_;
            this.begTargeting = (new EntityPredicate()).range((double) p_i1617_2_).allowInvulnerable().allowSameTeam().allowNonAttackable();
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            this.player = this.level.getNearestPlayer(this.begTargeting, this.wolf);
            return this.player != null && this.playerHoldingInteresting(this.player);
        }

        public boolean canContinueToUse() {
            if (!this.player.isAlive()) {
                return false;
            } else if (this.wolf.distanceToSqr(this.player) > (double) (this.lookDistance * this.lookDistance)) {
                return false;
            } else {
                return this.lookTime > 0 && this.playerHoldingInteresting(this.player);
            }
        }

        public void start() {
            this.wolf.setIsInterested(true);
            this.lookTime = 40 + this.wolf.getRandom().nextInt(40);
        }

        public void stop() {
            this.wolf.setIsInterested(false);
            this.player = null;
        }

        public void tick() {
            this.wolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float) this.wolf.getMaxHeadXRot());
            --this.lookTime;
        }

        private boolean playerHoldingInteresting(PlayerEntity pPlayer) {
            for (Hand hand : Hand.values()) {
                ItemStack itemstack = pPlayer.getItemInHand(hand);
                if (itemstack.getItem() == Items.BONE || itemstack.getItem() == Items.ROTTEN_FLESH) {
                    return true;
                }
            }

            return false;
        }
    }

    static class SitGoal extends Goal {
        private final UndeadWolfEntity mob;

        public SitGoal(UndeadWolfEntity p_i1654_1_) {
            this.mob = p_i1654_1_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canContinueToUse() {
            return this.mob.isOrderedToSit();
        }

        public boolean canUse() {
            if (this.mob.isInWaterOrBubble()) {
                return false;
            } else if (!this.mob.isOnGround()) {
                return false;
            } else {
                LivingEntity livingentity = this.mob.getTrueOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    return (!(this.mob.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null) && this.mob.isOrderedToSit();
                }
            }
        }

        public void start() {
            this.mob.getNavigation().stop();
            this.mob.setInSittingPose(true);
        }

        public void stop() {
            this.mob.setInSittingPose(false);
        }
    }

}
