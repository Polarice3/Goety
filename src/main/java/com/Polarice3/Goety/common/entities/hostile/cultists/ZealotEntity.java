package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.common.entities.ai.ManoeuvrableCrossbowGoal;
import com.Polarice3.Goety.common.entities.ai.PotionGroupGoal;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ZealotEntity extends AbstractCultistEntity implements ICrossbowUser, IRangedAttackMob, ICultist {
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(ZealotEntity.class, DataSerializers.BOOLEAN);

    public ZealotEntity(EntityType<? extends ZealotEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PotionGroupGoal<>(this, 1.25F));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 30.0F));
        this.goalSelector.addGoal(2, new ManoeuvrableCrossbowGoal<>(this, 1.0D, 24.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.ZealotHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.ZEALOT_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ZEALOT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ZEALOT_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.ZEALOT_CELEBRATE.get();
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if (worldIn.getRandom().nextInt(100) == 0) {
            CrimsonSpiderEntity spider = new CrimsonSpiderEntity(ModEntityType.CRIMSON_SPIDER.get(), level);
            if (this.isPersistenceRequired()){
                spider.setPersistenceRequired();
            }
            spider.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            spider.finalizeSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, (ILivingEntityData)null, (CompoundNBT)null);
            this.startRiding(spider);
            worldIn.addFreshEntity(spider);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        int random = this.level.random.nextInt(2);
        if (random == 1) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.025F);
        boolean flag = true;

        for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR && equipmentslottype != EquipmentSlotType.HEAD) {
                ItemStack itemstack = this.getItemBySlot(equipmentslottype);
                if (!flag && this.random.nextFloat() < 0.25F) {
                    break;
                }

                flag = false;
                if (itemstack.isEmpty()) {
                    int i = this.random.nextInt(8);
                    if (i == 4){
                        --i;
                    }
                    Item item = getEquipmentForSlot(equipmentslottype, i);
                    if (item != null) {
                        this.setItemSlot(equipmentslottype, new ItemStack(item));
                        this.setDropChance(equipmentslottype, 0.025F);
                    }
                }
            }
        }
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.CULTIST_HELM.get()));
        this.setDropChance(EquipmentSlotType.HEAD, 0.0F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGING_STATE, false);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    protected AbstractArrowEntity getMobArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, arrowStack, distanceFactor);
        if (!this.getActiveEffects().isEmpty()){
            ArrowEntity arrowEntity = (ArrowEntity) abstractarrowentity;
            for (EffectInstance effectInstance : this.getActiveEffects()){
                if (!effectInstance.getEffect().isBeneficial()) {
                    arrowEntity.addEffect(effectInstance);
                }
            }
        }
        if (this.isOnFire()){
            abstractarrowentity.setSecondsOnFire(100);
        }
        return abstractarrowentity;
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.BOW || p_230280_1_ == Items.CROSSBOW;
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractCultistEntity.ArmPose getArmPose() {
        if(this.getMainHandItem().getItem() == Items.CROSSBOW){
            if (this.isCharging()) {
                return AbstractCultistEntity.ArmPose.CROSSBOW_CHARGE;
            } else {
                return AbstractCultistEntity.ArmPose.CROSSBOW_HOLD;
            }
        } else if (this.getMainHandItem().getItem() == Items.BOW) {
            if (this.isAggressive()){
                return ArmPose.BOW_AND_ARROW;
            } else {
                return ArmPose.NEUTRAL;
            }
        } else {
            return ArmPose.NEUTRAL;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getMobArrow(itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
