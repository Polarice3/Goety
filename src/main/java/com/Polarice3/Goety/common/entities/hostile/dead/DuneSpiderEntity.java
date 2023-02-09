package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class DuneSpiderEntity extends MonsterEntity implements IDeadMob, ICustomAttributes {
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(DuneSpiderEntity.class, DataSerializers.BYTE);

    public DuneSpiderEntity(EntityType<? extends DuneSpiderEntity> p_i50214_1_, World p_i50214_2_) {
        super(p_i50214_1_, p_i50214_2_);
        ICustomAttributes.applyAttributesForEntity(p_i50214_1_, this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LeapingAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new DuneSpiderEntity.AttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new DuneSpiderEntity.TargetGoal<>(this, LivingEntity.class));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.DuneSpiderHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, MobConfig.DuneSpiderDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public double getPassengersRidingOffset() {
        return (double)(this.getBbHeight() * 0.5F);
    }

    protected PathNavigator createNavigation(World pLevel) {
        return new ClimberPathNavigator(this, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }

    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())){
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            int random = this.level.random.nextInt(8);
            if (random == 0){
                EffectsUtil.amplifyEffect(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 140));
            } else {
                EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 140));
            }
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 140));
        }
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState pState, Vector3d pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }

    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.SPIDER.getDefaultLootTable();
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        if (pPotioneffect.getEffect() == Effects.POISON || pPotioneffect.getEffect() == ModEffects.DESICCATE.get()) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, pPotioneffect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(pPotioneffect);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(MonsterEntity p_i46676_1_) {
            super(p_i46676_1_, 1.0D, true);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        public boolean canContinueToUse() {
            float f = this.mob.getBrightness();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return 4.0F + pAttackTarget.getBbWidth();
        }
    }

    static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public TargetGoal(MonsterEntity p_i45818_1_, Class<T> p_i45818_2_) {
            super(p_i45818_1_, p_i45818_2_, 0, false, false, DEAD_TARGETS);
        }

        public boolean canUse() {
            float f = this.mob.getBrightness();
            return !(f >= 0.5F) && super.canUse();
        }
    }

    static class LeapingAtTargetGoal extends Goal {
        private final MobEntity mob;
        private LivingEntity target;
        private final float yd;

        public LeapingAtTargetGoal(MobEntity p_i1630_1_, float p_i1630_2_) {
            this.mob = p_i1630_1_;
            this.yd = p_i1630_2_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.isVehicle()) {
                return false;
            } else {
                this.target = this.mob.getTarget();
                if (this.target == null) {
                    return false;
                } else {
                    double d0 = this.mob.distanceToSqr(this.target);
                    if (!(d0 < 4.0D) && !(d0 > 24.0D)) {
                        if (!this.mob.isOnGround()) {
                            return false;
                        } else {
                            return this.mob.getRandom().nextInt(5) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.isOnGround();
        }

        public void start() {
            Vector3d vector3d = this.mob.getDeltaMovement();
            Vector3d vector3d1 = new Vector3d(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
            if (vector3d1.lengthSqr() > 1.0E-7D) {
                vector3d1 = vector3d1.normalize().scale(0.8D).add(vector3d.scale(0.4D));
            }

            this.mob.setDeltaMovement(vector3d1.x, this.yd, vector3d1.z);
        }
    }
}
