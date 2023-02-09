package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MobConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effect;
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
import java.util.Random;

public class CrimsonSpiderEntity extends AbstractCultistEntity {
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(CrimsonSpiderEntity.class, DataSerializers.BYTE);

    public CrimsonSpiderEntity(EntityType<? extends AbstractCultistEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(2, new AttackGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.CrimsonSpiderHealth.get())
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, MobConfig.CrimsonSpiderDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.75F;
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
        if (!this.level.isClientSide && this.isAggressive()) {
            this.setClimbing(this.horizontalCollision);
        }

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

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        if (pPotioneffect.getEffect() == Effects.POISON) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, pPotioneffect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(pPotioneffect);
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.SPIDER.getDefaultLootTable();
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
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

        if (pSpawnData == null) {
            pSpawnData = new CrimsonSpiderEntity.GroupData();
            ((CrimsonSpiderEntity.GroupData)pSpawnData).setRandomEffect(pLevel.getRandom());

        }

        if (pSpawnData instanceof CrimsonSpiderEntity.GroupData) {
            Effect effect = ((CrimsonSpiderEntity.GroupData)pSpawnData).effect;
            if (effect != null) {
                this.addEffect(new EffectInstance(effect, Integer.MAX_VALUE));
            }
        }

        return pSpawnData;
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return 0.65F;
    }

    public static class GroupData implements ILivingEntityData {
        public Effect effect;

        public void setRandomEffect(Random pRand) {
            int i = pRand.nextInt(5);
            switch (i){
                case 0:
                    break;
                case 1:
                    this.effect = Effects.MOVEMENT_SPEED;
                    break;
                case 2:
                    this.effect = Effects.DAMAGE_BOOST;
                    break;
                case 3:
                    this.effect = Effects.REGENERATION;
                    break;
                case 4:
                    this.effect = Effects.FIRE_RESISTANCE;
            }
        }
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(CrimsonSpiderEntity p_i46676_1_) {
            super(p_i46676_1_, 1.0D, true);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return 4.0F + pAttackTarget.getBbWidth();
        }
    }
}
