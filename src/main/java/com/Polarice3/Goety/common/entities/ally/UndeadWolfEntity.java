package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class UndeadWolfEntity extends SummonedEntity {
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(UndeadWolfEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_INTERESTED_ID = EntityDataManager.defineId(UndeadWolfEntity.class, DataSerializers.BOOLEAN);
    private float interestedAngle;
    private float interestedAngleO;
    private boolean orderedToSit;

    public UndeadWolfEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void tick() {
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
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.UndeadWolfHealth.get())
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.UndeadWolfDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(ModSounds.UNDEAD_WOLF_STEP.get(), 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        if (this.isAggressive()) {
            return ModSounds.UNDEAD_WOLF_GROWL.get();
        } else if (this.random.nextInt(3) == 0) {
            return ModSounds.UNDEAD_WOLF_PANT.get();
        } else {
            return ModSounds.UNDEAD_WOLF_AMBIENT.get();
        }
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.UNDEAD_WOLF_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.UNDEAD_WOLF_DEATH.get();
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

    @Override
    public boolean canUpdateMove() {
        return false;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (this.isUpgraded()){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(AttributesConfig.NecroUndeadWolfHealth.get());
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(AttributesConfig.NecroUndeadWolfDamage.get());
            this.setHealth(AttributesConfig.NecroUndeadWolfHealth.get().floatValue());
        }
        return pSpawnData;
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
            if (zombieMinionEntity != null) {
                zombieMinionEntity.finalizeSpawn(world, level.getCurrentDifficultyAt(zombieMinionEntity.blockPosition()), SpawnReason.CONVERSION, null, (CompoundNBT) null);
                zombieMinionEntity.setLimitedLife(10 * (15 + this.level.random.nextInt(45)));
                if (this.getTrueOwner() != null){
                    zombieMinionEntity.setTrueOwner(this.getTrueOwner());
                }
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(killedEntity, zombieMinionEntity);
                if (!this.isSilent()) {
                    world.levelEvent((PlayerEntity) null, 1026, this.blockPosition(), 0);
                }
            }
        }
        if (killedEntity instanceof AbstractSkeletonEntity){
            this.heal(2.0F);
        }

    }

    public boolean canBeLeashed(PlayerEntity pPlayer) {
        return !this.isAggressive() && super.canBeLeashed(pPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.getItem() == Items.ROTTEN_FLESH || itemstack.getItem() == Items.BONE) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.heal(2.0F);
                if (!this.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        serverWorld.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                    }
                }
                return ActionResultType.SUCCESS;
            }
            if (!(itemstack.getItem() instanceof SoulWand && (pPlayer.isShiftKeyDown() || pPlayer.isCrouching()))){
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
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
