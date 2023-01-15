package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.common.entities.ai.FrostBallGoal;
import com.Polarice3.Goety.common.entities.ai.SpewingAttackGoal;
import com.Polarice3.Goety.common.entities.ally.StrayMinionEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.projectiles.FrostBallEntity;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class AbstractDredenEntity extends AbstractWraithEntity implements ISpewing, IRangedAttackMob {
    protected static final DataParameter<Boolean> SPEWING = EntityDataManager.defineId(AbstractDredenEntity.class, DataSerializers.BOOLEAN);

    public AbstractDredenEntity(EntityType<? extends SummonedEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FrostBallGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new SpewingAttackGoal<>(this, 20.0F, 20, 0.025F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPEWING, false);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof StrayEntity && this.isHostile()) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof StrayMinionEntity && !this.isHostile()) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }  else {
            return super.isAlliedTo(entityIn);
        }
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != Effects.MOVEMENT_SLOWDOWN && super.canBeAffected(potioneffectIn);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.DREDEN_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.DREDEN_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.DREDEN_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (pSource.getEntity() instanceof StrayEntity || pSource.getEntity() instanceof StrayMinionEntity) {
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    public void aiStep() {
        super.aiStep();

        if (isSpewing() && this.isAlive()) {
            if (this.level.isClientSide) {
                Vector3d lookAngle = this.getLookAngle();

                double distance = 0.9;
                double px = this.getX() + lookAngle.x * distance;
                double py = this.getY() + 1.5F + lookAngle.y * distance;
                double pz = this.getZ() + lookAngle.z * distance;

                for (int i = 0; i < 2; i++) {
                    double dx = lookAngle.x;
                    double dy = lookAngle.y;
                    double dz = lookAngle.z;

                    double spread = 5.0D + this.random.nextDouble() * 2.5D;
                    double velocity = 0.6D + this.random.nextDouble() * 0.6D;

                    dx += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dy += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dz += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dx *= velocity;
                    dy *= velocity;
                    dz *= velocity;

                    this.level.addParticle(ParticleTypes.CLOUD, px, py, pz, dx, dy, dz);
                }
            }

            this.playSound(SoundEvents.PLAYER_BREATH, random.nextFloat() * 0.5F, random.nextFloat() * 0.5F);
        }

        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            int f = MathHelper.floor(this.getX());
            int g = MathHelper.floor(this.getY());
            int h = MathHelper.floor(this.getZ());
            if (this.isAlive()) {
                serverWorld.sendParticles(ParticleTypes.CLOUD, this.getRandomX(0.5D), this.getY() + 0.5D, this.getRandomZ(0.5D), 1, (0.5D - this.random.nextDouble()) * 0.15D, (double) 0.01F, (0.5D - this.random.nextDouble()) * 0.15D, (0.5D - this.random.nextDouble()) * 0.15D);
                if (this.level.getBiome(new BlockPos(f, 0, h)).getTemperature(new BlockPos(f, g, h)) > 1.0F && this.level.isDay()) {
                    this.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
                }
                if (this.level.isRainingAt(this.blockPosition()) && this.level.getBiome(this.blockPosition()).shouldSnow(this.level, this.blockPosition())){
                    this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 20, 1, false, false));
                    if (this.tickCount % 20 == 0){
                        this.heal(1.0F);
                    }
                }
            }
        }

    }

    @Override
    public void attackAI() {
    }

    @Override
    public boolean isSpewing() {
        return this.entityData.get(SPEWING);
    }

    @Override
    public void setSpewing(boolean flag) {
        this.entityData.set(SPEWING, flag);
    }

    @Override
    public void doSpewing(Entity target) {
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) target;
            if (livingEntity.hurt(ModDamageSource.frostBreath(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))){
                if (!livingEntity.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100));
                } else {
                    if (this.random.nextFloat() <= 0.01F) {
                        EffectsUtil.amplifyEffect(livingEntity, Effects.MOVEMENT_SLOWDOWN, 100);
                    } else {
                        EffectsUtil.resetDuration(livingEntity, Effects.MOVEMENT_SLOWDOWN, 100);
                    }
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        this.playSound(ModSounds.DREDEN_SHOOT.get(), 1.0F, 1.0F);
        double d1 = pTarget.getX() - this.getX();
        double d2 = pTarget.getY(0.5D) - this.getY(0.5D);
        double d3 = pTarget.getZ() - this.getZ();
        FrostBallEntity frostBall = new FrostBallEntity(this.level, this, d1, d2, d3);
        frostBall.setPos(frostBall.getX(), this.getY(0.75F), frostBall.getZ());
        this.level.addFreshEntity(frostBall);
        this.swing(Hand.MAIN_HAND);
    }
}
