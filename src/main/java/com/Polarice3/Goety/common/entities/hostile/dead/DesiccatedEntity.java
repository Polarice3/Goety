package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class DesiccatedEntity extends AbstractSkeletonEntity implements IDeadMob, ICustomAttributes {

    public DesiccatedEntity(EntityType<? extends AbstractSkeletonEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
        ICustomAttributes.applyAttributesForEntity(p_i48555_1_, this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, DEAD_TARGETS));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.DesiccatedHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, MobConfig.DesiccatedDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
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

    protected boolean isSunBurnTick() {
        return false;
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.SKELETON.getDefaultLootTable();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        int i = this.random.nextInt(4);
        if (i == 0) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.reassessWeaponGoal();
        return ilivingentitydata;
    }

    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())){
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 200));
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 200));
        }
    }

    protected AbstractArrowEntity getArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.getArrow(pArrowStack, pDistanceFactor);
        if (abstractarrowentity instanceof ArrowEntity){
            ArrowEntity arrowEntity = (ArrowEntity) abstractarrowentity;
            arrowEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 100));
        }
        return abstractarrowentity;
    }
}
