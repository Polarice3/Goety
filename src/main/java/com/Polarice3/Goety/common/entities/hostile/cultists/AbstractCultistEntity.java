package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class AbstractCultistEntity extends AbstractRaiderEntity {
    private static final DataParameter<Float> DATA_REINFORCEMENT_CHANCE = EntityDataManager.defineId(AbstractCultistEntity.class, DataSerializers.FLOAT);

    protected AbstractCultistEntity(EntityType<? extends AbstractRaiderEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.getNavigation().setCanFloat(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this).setAlertOthers()));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REINFORCEMENT_CHANCE, 0.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getEntity() instanceof ICultist){
            this.conversion();
        }
        if (this.getReinforcementChance() < 0.0F){
            this.setReinforcementChance(0.0F);
        }
    }

    public void conversion(){
        if (MainConfig.CultistSpread.get()) {
            int timer = 1200;
            if (this.hasActiveRaid()) {
                timer = 400;
            }
            if (this.tickCount % timer == 0) {
                MobUtil.secretConversion(this);
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isPassenger() && this.getVehicle() instanceof CrimsonSpiderEntity){
            if (pSource == DamageSource.IN_WALL){
                return false;
            }
        }
        if (this.level instanceof ServerWorld){
            ServerWorld serverworld = (ServerWorld)this.level;
            if (this.getEntity() instanceof ICultist) {
                LivingEntity livingentity = this.getTarget();
                if (livingentity == null && pSource.getEntity() instanceof LivingEntity) {
                    livingentity = (LivingEntity) pSource.getEntity();
                }

                int i = MathHelper.floor(this.getX());
                int j = MathHelper.floor(this.getY());
                int k = MathHelper.floor(this.getZ());

                if (livingentity != null && this.level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < this.getReinforcementChance() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    AbstractCultistEntity cultist = null;
                    int random = this.random.nextInt(12);
                    switch (random){
                        case 4:
                            cultist = ModEntityType.FANATIC.get().create(serverworld);
                            break;
                        case 8:
                            cultist = ModEntityType.ZEALOT.get().create(serverworld);
                            break;
                        case 9:
                            cultist = ModEntityType.DISCIPLE.get().create(serverworld);
                            break;
                        case 10:
                            cultist = ModEntityType.BELDAM.get().create(serverworld);
                            break;
                        case 11:
                            cultist = ModEntityType.THUG.get().create(serverworld);
                    }
                    if (cultist != null) {
                        for (int l = 0; l < 50; ++l) {
                            int i1 = i + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            int j1 = j + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            int k1 = k + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            BlockPos blockpos = new BlockPos(i1, j1, k1);
                            EntityType<?> entitytype = cultist.getType();
                            EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
                            if (WorldEntitySpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level, blockpos, entitytype) && EntitySpawnPlacementRegistry.checkSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos, this.level.random)) {
                                cultist.setPos((double) i1, (double) j1, (double) k1);
                                if (!this.level.hasNearbyAlivePlayer((double) i1, (double) j1, (double) k1, 7.0D) && this.level.isUnobstructed(cultist) && this.level.noCollision(cultist) && !this.level.containsAnyLiquid(cultist.getBoundingBox())) {
                                    cultist.setTarget(livingentity);
                                    cultist.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData) null, (CompoundNBT) null);
                                    serverworld.addFreshEntityWithPassengers(cultist);
                                    this.setReinforcementChance(this.getReinforcementChance() - 0.05F);
                                    if (cultist instanceof ICultist){
                                        cultist.setReinforcementChance(cultist.getReinforcementChance() - 0.05F);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (this.getEntity() instanceof ICultist){
            this.randomizeReinforcementsChance();
            if (this.random.nextFloat() < difficultyIn.getSpecialMultiplier() * 0.05F) {
                this.setReinforcementChance(this.getReinforcementChance() +(float) (this.random.nextDouble() * 0.25D + 0.5D));
            }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void randomizeReinforcementsChance() {
        this.setReinforcementChance((float) (this.random.nextDouble() * 0.1D));
    }

    public void setReinforcementChance (float reinforcementChance){
        this.entityData.set(DATA_REINFORCEMENT_CHANCE, reinforcementChance);
    }

    public float getReinforcementChance() {
        return this.entityData.get(DATA_REINFORCEMENT_CHANCE);
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        return ArmPose.CROSSED;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof WitchEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractCultistEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof IDeadMob) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isAlliedTo(entityIn);
        } else return entityIn instanceof ICultistMinion;
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        CROSSED,
        ATTACKING,
        ZOMBIE,
        SPELLCASTING,
        SPELL_AND_WEAPON,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        BOMB_AND_WEAPON,
        THROW_SPEAR,
        NEUTRAL;
    }
}
