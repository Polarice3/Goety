package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class OwnedEntity extends CreatureEntity implements IOwned{
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(OwnedEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Boolean> HOSTILE = EntityDataManager.defineId(OwnedEntity.class, DataSerializers.BOOLEAN);
    private final NearestAttackableTargetGoal<PlayerEntity> targetGoal = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true);
    public boolean limitedLifespan;
    public int limitedLifeTicks;

    protected OwnedEntity(EntityType<? extends OwnedEntity> type, World worldIn) {
        super(type, worldIn);
        this.reassessGoal();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    public void reassessGoal() {
        if (this.level != null && !this.level.isClientSide) {
            if (this.getTrueOwner() instanceof MonsterEntity){
                this.setHostile(true);
            }
        }
    }

    public void tick(){
        super.tick();
        if (this.getTarget() instanceof OwnedEntity){
            OwnedEntity ownedEntity = (OwnedEntity) this.getTarget();
            if (ownedEntity.getTrueOwner() == this.getTrueOwner()){
                this.setTarget(null);
                if (this.getLastHurtByMob() == ownedEntity){
                    this.setLastHurtByMob(null);
                }
            }
            if (ownedEntity.getTrueOwner() == this){
                this.setTarget(null);
                if (this.getLastHurtByMob() == ownedEntity){
                    this.setLastHurtByMob(null);
                }
            }
        }
        if (this.getTrueOwner() instanceof OwnedEntity){
            if (this.getTrueOwner().isDeadOrDying() || !this.getTrueOwner().isAlive()){
                this.kill();
            }
        }
        if (this.getLastHurtByMob() == this.getTrueOwner()){
            this.setLastHurtByMob(null);
        }
        for (OwnedEntity target : this.level.getEntitiesOfClass(OwnedEntity.class, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE)))) {
            if (target.getTrueOwner() != this.getTrueOwner()
                    && this.getTrueOwner() != target.getTrueOwner()
                    && target.getTarget() == this.getTrueOwner()){
                this.setTarget(target);
            }
        }
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.lifeSpanDamage();
        }
    }

    public void lifeSpanDamage(){
        this.limitedLifeTicks = 20;
        this.hurt(DamageSource.STARVE, 1.0F);
    }

    public Team getTeam() {
        if (this.getTrueOwner() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.getTrueOwner() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (entityIn == livingentity) {
                return true;
            }
            return livingentity.isAlliedTo(entityIn);
        }
        if (entityIn instanceof OwnedEntity && ((OwnedEntity) entityIn).getTrueOwner() == this.getTrueOwner()){
            return true;
        }
        return super.isAlliedTo(entityIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(HOSTILE, false);
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

        if (compound.contains("isHostile")){
            this.setHostile(compound.getBoolean("isHostile"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

        this.reassessGoal();
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        if (this.isHostile()){
            compound.putBoolean("isHostile", this.isHostile());
        }
        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.reassessGoal();
        return pSpawnData;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void setTrueOwner(LivingEntity livingEntity){
        this.setOwnerId(livingEntity.getUUID());
    }

    public void setHostile(boolean hostile){
        this.entityData.set(HOSTILE, hostile);
        this.targetSelector.addGoal(2, this.targetGoal);
    }

    public boolean isHostile(){
        return this.entityData.get(HOSTILE);
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != ModEffects.GOLDTOUCHED.get() && super.canBeAffected(pPotioneffect);
    }

    protected int getExperienceReward(PlayerEntity pPlayer) {
        if (this.isHostile()) {
            this.xpReward = this.xpReward();
        }

        return super.getExperienceReward(pPlayer);
    }

    public int xpReward(){
        return 5;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.isHostile();
    }

    public class OwnerHurtTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(OwnedEntity summonedEntity) {
            super(summonedEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity livingentity = OwnedEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != OwnedEntity.this.getTrueOwner();
            }
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = OwnedEntity.this.getTrueOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtMobTimestamp();
            }

            super.start();
        }
    }

    public class OwnerHurtByTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(OwnedEntity summonedEntity) {
            super(summonedEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity livingentity = OwnedEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != OwnedEntity.this.getTrueOwner();
            }
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = OwnedEntity.this.getTrueOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }

    public static class LesserFollowOwnerGoal extends Goal {
        private final OwnedEntity ownedEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;

        public LesserFollowOwnerGoal(OwnedEntity ownedEntity, double speed, float minDist, float maxDist) {
            this.ownedEntity = ownedEntity;
            this.level = ownedEntity.level;
            this.followSpeed = speed;
            this.navigation = ownedEntity.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(ownedEntity.getNavigation() instanceof GroundPathNavigator) && !(ownedEntity.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.ownedEntity.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.ownedEntity.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else {
                return !(this.ownedEntity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.ownedEntity.getPathfindingMalus(PathNodeType.WATER);
            this.ownedEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.ownedEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.ownedEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.ownedEntity.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.ownedEntity.isLeashed() && !this.ownedEntity.isPassenger()) {
                    this.navigation.moveTo(this.owner, this.followSpeed);
                }
            }
        }
    }
}
