package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.common.entities.projectiles.DesiccatedSkullEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class MarcireEntity extends MonsterEntity implements IDeadMob, IRangedAttackMob {
    private final EntityPredicate entityPredicate = (new EntityPredicate()).range(8).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public MarcireEntity(EntityType<? extends MonsterEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SkullGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, DEAD_TARGETS));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public void tick() {
        super.tick();
        if (this.getTarget() != null){
            LivingEntity livingEntity = this.getTarget();
            if (this.distanceToSqr(livingEntity) < 27){
                if (LocustLimit(this) < 3){
                    if (this.tickCount % 20 == 0){
                        this.playSound(SoundEvents.BEEHIVE_EXIT, 1.0F, 1.0F);
                        LocustEntity locustEntity = new LocustEntity(ModEntityType.LOCUST.get(), this.level);
                        locustEntity.setPos(this.getX(), this.getY(), this.getZ());
                        locustEntity.setOwnerId(this.getUUID());
                        locustEntity.setLifespan(true);
                        locustEntity.setTarget(livingEntity);
                        this.level.addFreshEntity(locustEntity);
                    }
                }
            }
        }
    }

    public int LocustLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(LocustEntity.class, this.entityPredicate, entityLiving, entityLiving.getBoundingBox().inflate(8)).size();
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof IDeadMob) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected boolean isSunBurnTick() {
        return false;
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.SKELETON.getDefaultLootTable();
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.populateDefaultEquipmentSlots(pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.CORRUPTSTAFF.get()));
    }

    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())){
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 200));
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 200));
        }
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
        double d1 = pTarget.getX() - this.getX();
        double d2 = pTarget.getY(0.5D) - this.getY(0.5D);
        double d3 = pTarget.getZ() - this.getZ();
        DesiccatedSkullEntity corruptSkull = new DesiccatedSkullEntity(this.level, this, d1, d2, d3);
        corruptSkull.setPos(corruptSkull.getX(), this.getY(0.75F), corruptSkull.getZ());
        corruptSkull.setDangerous(this.random.nextFloat() < 0.05F);
        this.level.addFreshEntity(corruptSkull);
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return 1.74F;
    }

    public double getMyRidingOffset() {
        return -0.6D;
    }

    static class SkullGoal extends RangedAttackGoal {
        public MarcireEntity wizard;

        public SkullGoal(MarcireEntity entity) {
            super(entity, 1.0D, 20, 20.0F);
            this.wizard = entity;
        }

        public boolean canUse() {
            if (super.canUse()){
                return this.wizard.getMainHandItem().getItem() == ModItems.CORRUPTSTAFF.get();
            } else {
                return false;
            }
        }

        public void start() {
            super.start();
            this.wizard.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.wizard.setAggressive(false);
        }
    }

}
