package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.Objects;

public class FallenEntity extends ZombieEntity implements IDeadMob, ICustomAttributes {

    public FallenEntity(EntityType<? extends ZombieEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        ICustomAttributes.applyAttributesForEntity(p_i48553_1_, this);
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, HuskarlEntity.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, DEAD_TARGETS));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.FallenHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, MobConfig.FallenDamage.get())
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected boolean isSunSensitive() {
        return false;
    }

    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())) {
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 400));
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 400));
        }
        this.heal(1.0F);
    }

    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null){
            return ModSounds.FALLEN_ANGRY.get();
        } else {
            return ModSounds.FALLEN_AMBIENT.get();
        }
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.FALLEN_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FALLEN_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    protected boolean convertsInWater() {
        return false;
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.ZOMBIE.getDefaultLootTable();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        int random = this.random.nextInt(8);
        int random2 = this.random.nextInt(8);
        switch (random){
            case 3:
                this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                break;
            case 6:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                break;
            case 7:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                break;
        }
        switch (random2){
            case 4:
                this.setItemSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                break;
            case 5:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                break;
            case 6:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                break;
            case 7:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                break;
        }
    }

}
