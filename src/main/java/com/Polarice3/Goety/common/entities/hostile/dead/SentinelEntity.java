package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;

public class SentinelEntity extends MonsterEntity implements IDeadMob{
    private int attackTimer;
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public SentinelEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, DEAD_TARGETS));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 17.0D);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    protected void doPush(Entity entityIn) {
        if (entityIn instanceof IMob && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entityIn);
        }

        super.doPush(entityIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public void aiStep() {
        super.aiStep();
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.hurt(DamageSource.STARVE, this.getMaxHealth());
        }
        if (this.tickCount % 20 == 0) {
            this.heal(1.0F);
        }
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.level.isClientSide) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
            boolean flag = false;
            AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.2D);

            for(BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                BlockState blockstate = this.level.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof LeavesBlock || block instanceof RotatedPillarBlock) {
                    flag = this.level.destroyBlock(blockpos, true, this) || flag;
                }
            }

            if (!flag && this.onGround) {
                this.jumpFromGround();
            }
        }

        for(LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), field_213690_b)) {
            if (!(livingEntity instanceof IDeadMob)){
                if (livingEntity instanceof PlayerEntity){
                    if (MobUtil.playerValidity((PlayerEntity) livingEntity, true)){
                        livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200, 1));
                    }
                } else {
                    livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200, 1));
                }
            }
        }
    }

    private float func_226511_et_() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean ignoreExplosion(){
        return this.tickCount < 20;
    }

    public boolean doHurtTarget(Entity entityIn) {
        this.attackTimer = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        float f = this.func_226511_et_();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f1);
        if (flag) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(0.0D, (double)0.4F, 0.0D));
            this.doEnchantDamageEffects(this, entityIn);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())){
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            int random = this.level.random.nextInt(8);
            if (random == 0){
                EffectsUtil.amplifyEffect(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 1200));
            } else {
                EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 1200));
            }
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
        }
    }

    public float getBrightness() {
        return 1.0F;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (source == DamageSource.MAGIC){
            return false;
        } else {
            int r = this.level.getDifficulty() == Difficulty.HARD ? 64: 128;
            int random = this.level.random.nextInt(r);
            if (random == 1) {
                BlockPos blockpos = this.blockPosition().offset(0, 1, 0);
                LocustEntity locust = new LocustEntity(ModEntityType.LOCUST.get(), this.level);
                locust.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                locust.setOwnerId(this.uuid);
                locust.setLifespan(true);
                this.level.addFreshEntity(locust);
            }
            return super.hurt(source, amount);
        }
    }

    public int getAttackTimer() {
        return this.attackTimer;
    }

    public void die(DamageSource cause) {
        super.die(cause);
    }


}
