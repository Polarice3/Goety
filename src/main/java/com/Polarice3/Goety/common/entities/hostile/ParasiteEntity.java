package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.common.entities.neutral.MinionEntity;
import com.Polarice3.Goety.common.entities.neutral.MutatedEntity;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class ParasiteEntity extends MonsterEntity {
    private int lifetime;
    private boolean attackAll;
    private int field_234358_by_ = 0;
    private static final Predicate<LivingEntity> TARGETS = (enemy) -> !(enemy instanceof ParasiteEntity)
            && !(enemy instanceof AbstractSkeletonEntity)
            && !(enemy instanceof VexEntity) && !(enemy instanceof MinionEntity)
            && !(enemy instanceof GhastEntity) && !(enemy instanceof BlazeEntity) && !(enemy instanceof SlimeEntity)
            && !(enemy instanceof MutatedEntity)
            && !(enemy instanceof GuardianEntity) && !(enemy instanceof IronGolemEntity) && !(enemy instanceof TankEntity)
            && !(enemy instanceof SilverfishEntity) && !(enemy instanceof EndermiteEntity)
            && !(enemy instanceof WitherEntity) && !(enemy instanceof EnderDragonEntity);
    private static final Predicate<LivingEntity> HOSTED = (p_213797_0_) ->
            p_213797_0_.hasEffect(ModRegistry.HOSTED.get()) && !(p_213797_0_ instanceof ParasiteEntity) && p_213797_0_.attackable();

    public ParasiteEntity(EntityType<? extends ParasiteEntity> type, World worldIn) {
        super(type, worldIn);
        this.xpReward = 3;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, true, HOSTED));
        this.targetSelector.addGoal(2, new AttackAllGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.lifetime = compound.getInt("Lifetime");
        this.attackAll = compound.getBoolean("AttackAll");
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Lifetime", this.lifetime);
        compound.putBoolean("AttackAll", this.attackAll);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.13F;
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMITE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENDERMITE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMITE_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F);
    }

    public boolean isAttackAll() {
        return this.attackAll;
    }

    public void setAttackAll(boolean attackAll) {
        this.attackAll = attackAll;
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && entityIn instanceof LivingEntity) {
            int random = this.level.random.nextInt(16);
            if (random == 0) {
                float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
                ((LivingEntity) entityIn).addEffect(new EffectInstance(ModRegistry.HOSTED.get(), 140 * (int) f));
                this.lifetime = 0;
            }
        }

        return flag;
    }

    public void customServerAiStep(){
        if (this.func_234364_eK_()) {
            ++this.field_234358_by_;
            if (this.field_234358_by_ > 300 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, EntityType.ENDERMITE, (timer) -> this.field_234358_by_ = timer)) {
                this.playSound(SoundEvents.ENDERMITE_DEATH, 1.0F, 1.0F);
                this.func_234360_a_((ServerWorld)this.level);
            }
        } else {
            this.field_234358_by_ = 0;
        }
        super.customServerAiStep();
    }

    private void func_234360_a_(ServerWorld p_234360_1_) {
        EndermiteEntity endermiteEntity = this.convertTo(EntityType.ENDERMITE, true);
        if (endermiteEntity != null) {
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, endermiteEntity);
        }

    }

    public void setYBodyRot(float offset) {
        this.yRot = offset;
        super.setYBodyRot(offset);
    }

    public double getMyRidingOffset() {
        return 0.1D;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.isPersistenceRequired()) {
            ++this.lifetime;
        }

        if (this.lifetime >= 300 && !this.level.dimensionType().createDragonFight()) {
            this.actuallyHurt(DamageSource.STARVE, this.getMaxHealth());
        }
        this.yBodyRot = this.yRot;
    }

    public boolean func_234364_eK_() {
        return this.level.dimensionType().createDragonFight() && !this.isNoAi();
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    class AttackAllGoal extends NearestAttackableTargetGoal<LivingEntity> {

        public AttackAllGoal(ParasiteEntity p_i47061_1_) {
            super(p_i47061_1_, LivingEntity.class, 0, true, true, TARGETS);
        }

        public boolean canUse() {
            return ParasiteEntity.this.isAttackAll();
        }
    }

}
