package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ai.AllyTargetGoal;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.function.Predicate;

public class SummonedEntity extends OwnedEntity {
    private static final DataParameter<Boolean> WANDERING = EntityDataManager.defineId(SummonedEntity.class, DataSerializers.BOOLEAN);
    public final EntityPredicate summonCountTargeting = (new EntityPredicate()).range(64.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
    public boolean limitedLifespan;
    public int limitedLifeTicks;
    public boolean upgraded;

    protected SummonedEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F, false));
        this.targetSelector.addGoal(1, new AllyTargetGoal<>(this, MobEntity.class));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    public void checkDespawn() {
    }

    protected boolean isSunBurnTick() {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ())).above() : new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ());
            return f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.level.canSeeSky(blockpos);
        }

        return false;
    }

    public ItemStack getProjectile(ItemStack pShootable) {
        if (pShootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void tick(){
        super.tick();
        if (this.getTrueOwner() != null){
            if (RobeArmorFinder.FindNecroHelm(this.getTrueOwner())){
                if (this.getMobType() == CreatureAttribute.UNDEAD){
                    this.limitedLifespan = false;
                }
            } else if (this.limitedLifeTicks > 0){
                this.limitedLifespan = true;
            }
            if (this.getTrueOwner().getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.NECROBOOTSOFWANDER.get()){
                if (this.getMobType() == CreatureAttribute.UNDEAD){
                    this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1, false, false, false));
                }
            }
            if (this.getMobType() == CreatureAttribute.UNDEAD) {
                if (!this.isOnFire()) {
                    if (MainConfig.UndeadMinionHeal.get() && this.getHealth() < this.getMaxHealth()) {
                        if (this.getTrueOwner() instanceof PlayerEntity) {
                            if (RobeArmorFinder.FindNecroSet(this.getTrueOwner())) {
                                PlayerEntity owner = (PlayerEntity) this.getTrueOwner();
                                ItemStack foundStack = GoldTotemFinder.FindTotem(owner);
                                int SoulCost = MainConfig.UndeadMinionHealCost.get();
                                if (RobeArmorFinder.FindLeggings(owner)){
                                    if (this.random.nextBoolean()){
                                        SoulCost = 0;
                                    }
                                }
                                if (SEHelper.getSEActive(owner) && SEHelper.getSESouls(owner) > MainConfig.UndeadMinionHealCost.get()){
                                    if (this.tickCount % 20 == 0) {
                                        this.heal(1.0F);
                                        Vector3d vector3d = this.getDeltaMovement();
                                        new ParticleUtil(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D);
                                        SEHelper.decreaseSESouls(owner, SoulCost);
                                        if (!this.level.isClientSide){
                                            SEHelper.sendSEUpdatePacket(owner);
                                        }
                                    }
                                } else if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > MainConfig.UndeadMinionHealCost.get()) {
                                    if (this.tickCount % 20 == 0) {
                                        this.heal(1.0F);
                                        Vector3d vector3d = this.getDeltaMovement();
                                        new ParticleUtil(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D);
                                        GoldTotemItem.decreaseSouls(foundStack, SoulCost);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean flag = this.isSunSensitive() && this.isSunBurnTick();
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
        if (this.getTarget() instanceof SummonedEntity){
            SummonedEntity summonedEntity = (SummonedEntity) this.getTarget();
            if (summonedEntity.getTrueOwner() == this.getTrueOwner()){
                this.setTarget(null);
            }
        }
    }

    protected boolean isSunSensitive() {
        return false;
    }

    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (MainConfig.MinionsMasterImmune.get()) {
            if (source.getEntity() instanceof SummonedEntity) {
                SummonedEntity summoned = (SummonedEntity) source.getEntity();
                if (summoned.getTrueOwner() == this.getTrueOwner()) {
                    return false;
                } else {
                    return super.hurt(source, amount);
                }
            } else {
                return super.hurt(source, amount);
            }
        } else {
            return super.hurt(source, amount);
        }
    }

    public boolean isWandering() {
        return this.entityData.get(WANDERING);
    }

    public void setWandering(boolean wandering) {
        this.entityData.set(WANDERING, wandering);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WANDERING, false);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.upgraded = compound.getBoolean("Upgraded");
        this.entityData.set(WANDERING, compound.getBoolean("wandering"));

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Upgraded", this.upgraded);

        if (this.entityData.get(WANDERING)) {
            compound.putBoolean("wandering", true);
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    public boolean isUpgraded() {
        return this.upgraded;
    }

    public void setUpgraded(boolean attackAll) {
        this.upgraded = attackAll;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != ModEffects.GOLDTOUCHED.get() && super.canBeAffected(pPotioneffect);
    }

    public static class FollowOwnerGoal extends Goal {
        private final SummonedEntity summonedEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(SummonedEntity summonedEntity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.summonedEntity = summonedEntity;
            this.level = summonedEntity.level;
            this.followSpeed = speed;
            this.navigation = summonedEntity.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(summonedEntity.getNavigation() instanceof GroundPathNavigator) && !(summonedEntity.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.summonedEntity.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.summonedEntity.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else if (this.summonedEntity.isWandering()) {
                return false;
            } else if (this.summonedEntity.isAggressive()) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.summonedEntity.isAggressive()){
                return false;
            } else {
                return !(this.summonedEntity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.summonedEntity.getPathfindingMalus(PathNodeType.WATER);
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.summonedEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.summonedEntity.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.summonedEntity.isLeashed() && !this.summonedEntity.isPassenger()) {
                    if (this.summonedEntity.distanceToSqr(this.owner) >= 144.0D && MainConfig.UndeadTeleport.get()) {
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
                this.summonedEntity.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.summonedEntity.yRot, this.summonedEntity.xRot);
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
                    BlockPos blockpos = pos.subtract(this.summonedEntity.blockPosition());
                    return this.level.noCollision(this.summonedEntity, this.summonedEntity.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.summonedEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }
}
