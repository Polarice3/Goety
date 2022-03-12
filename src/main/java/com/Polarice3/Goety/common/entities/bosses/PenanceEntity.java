package com.Polarice3.Goety.common.entities.bosses;

import com.Polarice3.Goety.common.entities.projectiles.WarpedSpearEntity;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public class PenanceEntity extends CreatureEntity implements IRangedAttackMob {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof PenanceEntity);
    };
    public int circleTick;
    public int stunTick;
    public int roarTick;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;

    public PenanceEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.xpReward = 50;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new PenanceAttackGoal(this, 1.0D, 40, 10.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.HORSE_STEP, 0.25F, 1.0F);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModRegistry.WARPED_SPEAR.get()));
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        WarpedSpearEntity spearEntity = new WarpedSpearEntity(this.level, this, new ItemStack(ModRegistry.WARPED_SPEAR.get()));
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - spearEntity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        spearEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(spearEntity);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213684_dX() {
        return this.stunTick;
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213687_eg() {
        return this.roarTick;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("StunTick", this.stunTick);
        compound.putInt("RoarTick", this.roarTick);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.stunTick = compound.getInt("StunTick");
        this.roarTick = compound.getInt("RoarTick");
    }

    @OnlyIn(Dist.CLIENT)
    public float getRearingAmount(float p_110223_1_) {
        return MathHelper.lerp(p_110223_1_, this.prevRearingAmount, this.rearingAmount);
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.stunTick == 0 && !(entity instanceof SpectralArrowEntity) && !(entity instanceof FireballEntity)) {
            return false;
        } else if (entity instanceof SpectralArrowEntity) {
            this.stunTick = 60;
            return super.hurt(source, amount);
        } else if (entity instanceof FireballEntity) {
            this.stunTick = 60;
            return super.hurt(source, amount);
        } else if (entity == this.getVehicle()){
            return false;
        }  else {
            return super.hurt(source, amount);
        }
    }

    public void aiStep() {
        super.aiStep();
        this.prevHeadLean = this.headLean;
        this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;
        if (this.headLean < 0.0F) {
            this.headLean = 0.0F;
        }

        this.prevRearingAmount = this.rearingAmount;

        this.prevMouthOpenness = this.mouthOpenness;
        this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;
        if (this.mouthOpenness < 0.0F) {
            this.mouthOpenness = 0.0F;
        }

        if (this.roarTick == 0){
            this.removeEffect(Effects.GLOWING);
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;
            if (this.rearingAmount < 0.0F) {
                this.rearingAmount = 0.0F;
            }
        }

        if (this.roarTick > 0) {
            --this.roarTick;
            this.headLean = 0.0F;
            this.prevHeadLean = this.headLean;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;
            if (this.rearingAmount > 1.0F) {
                this.rearingAmount = 1.0F;
            }
            if (this.roarTick == 10) {
                this.roar();
            }
        }

        if (this.stunTick > 0) {
            --this.stunTick;
            this.func_213682_eh();
            if (this.stunTick == 0) {
                this.playSound(SoundEvents.SKELETON_HORSE_DEATH, 1.0F, 1.0F);
                this.roarTick = 20;
            }
        }
    }

    private void func_213682_eh() {
        if (this.random.nextInt(6) == 0) {
            double d0 = this.getX() - (double)this.getBbWidth() * Math.sin((double)(this.yBodyRot * ((float)Math.PI / 180F))) + (this.random.nextDouble() * 0.6D - 0.3D);
            double d1 = this.getY() + (double)this.getBbHeight() - 0.3D;
            double d2 = this.getZ() + (double)this.getBbWidth() * Math.cos((double)(this.yBodyRot * ((float)Math.PI / 180F))) + (this.random.nextDouble() * 0.6D - 0.3D);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, d0, d1, d2, 0.4980392156862745D, 0.5137254901960784D, 0.5725490196078431D);
        }

    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.stunTick > 0 || this.roarTick > 0;
    }

    public boolean canSee(Entity entityIn) {
        return this.stunTick <= 0 && this.roarTick <= 0 && super.canSee(entityIn);
    }

    private void roar() {
        if (this.isAlive()) {
            for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), field_213690_b)) {
                entity.hurt(DamageSource.mobAttack(this), 6.0F);
                entity.setSecondsOnFire(30);
                this.launch(entity);
            }

            Vector3d vector3d = this.getBoundingBox().getCenter();

            for(int i = 0; i < 40; ++i) {
                double d0 = this.random.nextGaussian() * 0.2D;
                double d1 = this.random.nextGaussian() * 0.2D;
                double d2 = this.random.nextGaussian() * 0.2D;
                this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
            }
        }

    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    static class PenanceAttackGoal extends RangedAttackGoal {
        private final PenanceEntity field_204728_a;

        public PenanceAttackGoal(IRangedAttackMob p_i48907_1_, double p_i48907_2_, int p_i48907_4_, float p_i48907_5_) {
            super(p_i48907_1_, p_i48907_2_, p_i48907_4_, p_i48907_5_);
            this.field_204728_a = (PenanceEntity)p_i48907_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.field_204728_a.stunTick <= 0 && this.field_204728_a.roarTick <= 0 && this.field_204728_a.getMainHandItem().getItem() == ModRegistry.WARPED_SPEAR.get();
        }

        public void start() {
            super.start();
            this.field_204728_a.setAggressive(true);
            this.field_204728_a.startUsingItem(Hand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.field_204728_a.stopUsingItem();
            this.field_204728_a.setAggressive(false);
        }
    }


}
