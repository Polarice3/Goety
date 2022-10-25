package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ai.AllyTargetGoal;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.function.Predicate;

public class SummonedEntity extends OwnedEntity {
    protected static final DataParameter<Byte> SUMMONED_FLAGS = EntityDataManager.defineId(SummonedEntity.class, DataSerializers.BYTE);
    public boolean limitedLifespan;
    public int limitedLifeTicks;
    public boolean upgraded;

    protected SummonedEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F));
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
        if (this.isStaying()){
            boolean docile = this.getLastHurtByMob() == null || this.getLastHurtByMob().isDeadOrDying();
            if (this.getTrueOwner() != null) {
                if (this.getTrueOwner().getLastHurtByMob() != null && !this.getTrueOwner().getLastHurtByMob().isDeadOrDying()) {
                    if (this.distanceTo(this.getTrueOwner()) <= 8.0F) {
                        docile = false;
                    }
                }
            }
            if (docile){
                this.setTarget(null);
                if (this.navigation.getPath() != null) {
                    this.navigation.stop();
                }
            }
            if (this.isWandering()) {
                this.setWandering(false);
            }
        }
        if (this.isWandering()){
            if (this.isStaying()) {
                this.setStaying(false);
            }
        }
        if (this.getTrueOwner() != null){
            if (RobeArmorFinder.FindNecroHelm(this.getTrueOwner()) && this.getMobType() == CreatureAttribute.UNDEAD){
                this.limitedLifespan = false;
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
                                        SEHelper.decreaseSESouls(owner, SoulCost);
                                        if (!this.level.isClientSide){
                                            ServerWorld serverWorld = (ServerWorld) this.level;
                                            SEHelper.sendSEUpdatePacket(owner);
                                            serverWorld.sendParticles(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D, 0.5F);
                                        }
                                    }
                                } else if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > MainConfig.UndeadMinionHealCost.get()) {
                                    if (this.tickCount % 20 == 0) {
                                        this.heal(1.0F);
                                        Vector3d vector3d = this.getDeltaMovement();
                                        GoldTotemItem.decreaseSouls(foundStack, SoulCost);
                                        if (!this.level.isClientSide){
                                            ServerWorld serverWorld = (ServerWorld) this.level;
                                            serverWorld.sendParticles(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D, 0.5F);
                                        }
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
                }
            }
        }
        return super.hurt(source, amount);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONED_FLAGS, (byte)0);
    }

    private boolean getFlag(int mask) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        return (i & mask) != 0;
    }

    private void setFlags(int mask, boolean value) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(SUMMONED_FLAGS, (byte)(i & 255));
    }

    public boolean isWandering() {
        return this.getFlag(1);
    }

    public void setWandering(boolean wandering) {
        this.setFlags(1, wandering);
    }

    public boolean isStaying(){
        return this.getFlag(2);
    }

    public void setStaying(boolean staying){
        this.setFlags(2, staying);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.upgraded = compound.getBoolean("Upgraded");
        this.setWandering(compound.getBoolean("wandering"));
        this.setStaying(compound.getBoolean("staying"));

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Upgraded", this.upgraded);

        if (this.isWandering()) {
            compound.putBoolean("wandering", true);
        }

        if (this.isStaying()) {
            compound.putBoolean("staying", true);
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    public void updateMoveMode(PlayerEntity player){
        if (!this.isWandering() && !this.isStaying()){
            this.setWandering(true);
            this.setStaying(false);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.wander", this.getDisplayName()), true);
        } else if (!this.isStaying()){
            this.setWandering(false);
            this.setStaying(true);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.staying", this.getDisplayName()), true);
        } else {
            this.setWandering(false);
            this.setStaying(false);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.follow", this.getDisplayName()), true);
        }
        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);

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

        public FollowOwnerGoal(SummonedEntity summonedEntity, double speed, float minDist, float maxDist) {
            this.summonedEntity = summonedEntity;
            this.level = summonedEntity.level;
            this.followSpeed = speed;
            this.navigation = summonedEntity.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
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
            } else if (this.summonedEntity.isWandering() || this.summonedEntity.isStaying()) {
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
                if (blockstate.getBlock() instanceof LeavesBlock) {
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

    class WanderGoal extends WaterAvoidingRandomWalkingGoal{

        public WanderGoal(CreatureEntity p_i47301_1_, double p_i47301_2_) {
            this(p_i47301_1_, p_i47301_2_, 0.001F);
        }

        public WanderGoal(CreatureEntity entity, double speedModifier, float probability) {
            super(entity, speedModifier, probability);
        }

        public boolean canUse() {
            if (super.canUse()){
                return !SummonedEntity.this.isStaying();
            } else {
                return false;
            }
        }
    }
}
