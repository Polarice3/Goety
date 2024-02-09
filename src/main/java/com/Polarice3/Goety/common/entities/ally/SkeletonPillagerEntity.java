package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.common.entities.ai.CreatureCrossbowAttackGoal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class SkeletonPillagerEntity extends AbstractSMEntity implements ICrossbowUser {
    private final CreatureCrossbowAttackGoal<SkeletonPillagerEntity> crossbowAttackGoal = new CreatureCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    private static final DataParameter<Boolean> IS_CHARGING_CROSSBOW = EntityDataManager.defineId(SkeletonPillagerEntity.class, DataSerializers.BOOLEAN);

    public SkeletonPillagerEntity(EntityType<? extends AbstractSMEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public void targetSelectGoal(){
        super.targetSelectGoal();
        this.targetSelector.addGoal(1, new NaturalAttackGoal<>(this, IronGolemEntity.class));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.MAX_HEALTH, AttributesConfig.SkeletonPillagerHealth.get())
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.SkeletonPillagerDamage.get())
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.crossbowAttackGoal);
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack2 = this.getOffhandItem();
            if (itemstack.getItem() instanceof CrossbowItem) {
                this.goalSelector.addGoal(3, this.crossbowAttackGoal);
            } else {
                this.goalSelector.addGoal(3, this.meleeGoal);
            }

        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    public boolean canFireProjectileWeapon(ShootableItem p_33280_) {
        return p_33280_ == Items.CROSSBOW;
    }

    @Override
    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (this.isNatural()){
            this.setHostile(true);
        }
        return spawnDataIn;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_219060_) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    protected void enchantSpawnedWeapon(float p_219057_) {
        super.enchantSpawnedWeapon(p_219057_);
        if (this.random.nextInt(300) == 0) {
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.getItem() == Items.CROSSBOW) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
                map.putIfAbsent(Enchantments.PIERCING, 1);
                EnchantmentHelper.setEnchantments(map, itemstack);
                this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
        }

    }

    public void performRangedAttack(LivingEntity p_33272_, float p_33273_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public void shootCrossbowProjectile(LivingEntity p_33275_, ItemStack p_33276_, ProjectileEntity p_33277_, float p_33278_) {
        this.shootCrossbowProjectile(this, p_33275_, p_33277_, p_33278_, 1.6F);
    }

    public static class NaturalAttackGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        protected SkeletonPillagerEntity skeletonPillager;

        public NaturalAttackGoal(SkeletonPillagerEntity skeletonPillager, Class<T> p_26061_) {
            super(skeletonPillager, p_26061_, true);
            this.skeletonPillager = skeletonPillager;
        }

        public boolean canUse() {
            return super.canUse() && this.skeletonPillager.isNatural() && (this.skeletonPillager.getTrueOwner() == null || this.skeletonPillager.getTrueOwner() instanceof AbstractIllagerEntity) && this.target != null && !this.target.isBaby();
        }
    }
}
