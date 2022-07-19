package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ChannellerEntity extends AbstractCultistEntity implements ICultist{
    private static final DataParameter<Boolean> IS_PRAYING = EntityDataManager.defineId(ChannellerEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof ChannellerEntity);
    };
    private static final DataParameter<Integer> TARGET_ALLY = EntityDataManager.defineId(ChannellerEntity.class, DataSerializers.INT);
    private int prayingTick;

    public ChannellerEntity(EntityType<? extends ChannellerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WANDERING_TRADER_NO;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_PRAYING, false);
        this.entityData.define(TARGET_ALLY, 0);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.prayingTick = compound.getInt("prayingTick");
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("prayingTick", this.prayingTick);
    }

    private void setAllyTarget(int AllyTargetIn) {
        this.entityData.set(TARGET_ALLY, AllyTargetIn);
    }

    public boolean hasAllyTarget() {
        return this.entityData.get(TARGET_ALLY) != 0;
    }

    public MonsterEntity getAllyTarget() {
        Entity entity = this.level.getEntity(this.entityData.get(TARGET_ALLY));
        if (entity instanceof MonsterEntity
                && !(entity instanceof CreeperEntity)) {
            return (MonsterEntity) entity;
        } else {
            return null;
        }
    }

    public boolean hurt(DamageSource source, float amount){
        if (!this.isPraying()) {
            return super.hurt(source, amount);
        } else {
            return super.hurt(source, amount/2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.prayingTick > 0) {
            return ArmPose.SPELLCASTING;
        } else {
            return ChannellerEntity.ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return ilivingentitydata;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    private final EntityPredicate ally = (new EntityPredicate().range(32.0D).allowSameTeam().selector(field_213690_b));

    public void setIsPraying(boolean praying) {
        this.entityData.set(IS_PRAYING, praying);
    }

    public boolean isPraying() {
        return this.entityData.get(IS_PRAYING);
    }

    public void aiStep() {
        super.aiStep();
        List<MonsterEntity> list = this.level.getNearbyEntities(MonsterEntity.class, this.ally, this, this.getBoundingBox().inflate(64.0D, 8.0D, 64.0D));
        if (!list.isEmpty() && !this.hasAllyTarget()) {
            MonsterEntity ally = list.get(this.random.nextInt(list.size()));
            this.setAllyTarget(ally.getId());
        }
        if (this.hasAllyTarget()) {
            if (this.prayingTick < 20) {
                ++this.prayingTick;
            } else {
                if (this.getAllyTarget() != null) {
                    if (this.distanceTo(this.getAllyTarget()) >= 12.0D) {
                        Vector3d vector3d = getAllyTarget().position();
                        this.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                    } else {
                        this.navigation.stop();
                        this.noActionTime = 0;
                        this.setIsPraying(true);
                        this.getLookControl().setLookAt(this.getAllyTarget(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
                        this.getAllyTarget().setTarget(this.getTarget());
                        this.getAllyTarget().addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 60, 1));
                        this.getAllyTarget().addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 60, 1));
                        this.getAllyTarget().addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 60, 1));
                        this.getAllyTarget().setPersistenceRequired();
                        if (this.getHealth() < this.getMaxHealth()) {
                            if (this.tickCount % 10 == 0) {
                                this.getAllyTarget().hurt(DamageSource.STARVE, 5.0F);
                                this.heal(5.0F);
                            }
                        }
                    }
                    if (this.getAllyTarget().isDeadOrDying()) {
                        this.setAllyTarget(0);
                        this.setIsPraying(false);
                    }
                } else {
                    this.prayingTick = 0;
                }
            }
        } else {
            this.prayingTick = 0;
            if (this.tickCount % 100 == 0){
                if (this.level instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    MonsterEntity minion = null;
                    switch (this.random.nextInt(3)) {
                        case (0):
                            minion = ModEntityType.ZOMBIE_VILLAGER_MINION.get().create(this.level);
                            break;
                        case (1):
                            minion = ModEntityType.SKELETON_VILLAGER_MINION.get().create(this.level);
                            break;
                        case (2):
                            minion = ModEntityType.ZPIGLIN_MINION.get().create(this.level);
                            break;
                    }
                    if (minion != null) {
                        minion.setPos(this.getX(), this.getY(), this.getZ());
                        minion.spawnAnim();
                        minion.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        serverWorld.addFreshEntity(minion);
                    }
                }
            }
        }
    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.isMagic()) {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
    }

}
