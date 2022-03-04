package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.init.ModRegistryHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConquillagerEntity extends AbstractIllagerEntity implements ICrossbowUser {
    private static final DataParameter<Boolean> IS_CHARGING_CROSSBOW = EntityDataManager.defineId(PillagerEntity.class, DataSerializers.BOOLEAN);
    private final Inventory inventory = new Inventory(5);
    private final Predicate<Entity> field_213690_b = Entity::isAlive;

    protected ConquillagerEntity(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FindTargetGoal(this, 10.0F));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public void tick() {
        super.tick();
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D), field_213690_b)) {
            if (!(entity instanceof PatrollerEntity)) {
                entity.addEffect(new EffectInstance(ModRegistryHandler.ILLAGUE.get(), 12000));
            }
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.CROSSBOW;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean pIsCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, pIsCharging);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundNBT()));
            }
        }

        pCompound.put("Inventory", listnbt);
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(item -> item instanceof CrossbowItem)) {
            return ArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? ArmPose.ATTACKING : ArmPose.NEUTRAL;
        }
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        ListNBT listnbt = pCompound.getList("Inventory", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
    }

    public float getWalkTargetValue(BlockPos pPos, IWorldReader pLevel) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        return !blockstate.is(Blocks.GRASS_BLOCK) && !blockstate.is(Blocks.SAND) ? 0.5F - pLevel.getBrightness(pPos) : 10.0F;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    protected void enchantSpawnedWeapon(float p_241844_1_) {
        super.enchantSpawnedWeapon(p_241844_1_);
        ItemStack itemstack = this.getMainHandItem();
        if (itemstack.getItem() == Items.CROSSBOW) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
            map.putIfAbsent(Enchantments.PIERCING, 4);
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PILLAGER_HURT;
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    private ItemStack getFirework(DyeColor pColor, int pFlightTime) {
        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 1);
        ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
        CompoundNBT compoundnbt = itemstack1.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        list.add(pColor.getFireworkColor());
        compoundnbt.putIntArray("Colors", list);
        compoundnbt.putByte("Type", (byte)FireworkRocketItem.Shape.BURST.getId());
        CompoundNBT compoundnbt1 = itemstack.getOrCreateTagElement("Fireworks");
        ListNBT listnbt = new ListNBT();
        CompoundNBT compoundnbt2 = itemstack1.getTagElement("Explosion");
        if (compoundnbt2 != null) {
            listnbt.add(compoundnbt2);
        }

        compoundnbt1.putByte("Flight", (byte)pFlightTime);
        if (!listnbt.isEmpty()) {
            compoundnbt1.put("Explosions", listnbt);
        }

        return itemstack;
    }

    public ItemStack getProjectile(ItemStack pShootable) {
        DyeColor dyecolor = Util.getRandom(DyeColor.values(), random);
        int i = random.nextInt(3);
        return getFirework(dyecolor, i);
    }

    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack itemstack = pItemEntity.getItem();
        if (itemstack.getItem() instanceof BannerItem) {
            super.pickUpItem(pItemEntity);
        } else {
            Item item = itemstack.getItem();
            if (this.wantsItem(item)) {
                this.onItemPickup(pItemEntity);
                ItemStack itemstack1 = this.inventory.addItem(itemstack);
                if (itemstack1.isEmpty()) {
                    pItemEntity.remove();
                } else {
                    itemstack.setCount(itemstack1.getCount());
                }
            }
        }

    }

    private boolean wantsItem(Item p_213672_1_) {
        return this.hasActiveRaid() && p_213672_1_ == Items.WHITE_BANNER;
    }

    public boolean setSlot(int pSlotIndex, ItemStack pStack) {
        if (super.setSlot(pSlotIndex, pStack)) {
            return true;
        } else {
            int i = pSlotIndex - 300;
            if (i >= 0 && i < this.inventory.getContainerSize()) {
                this.inventory.setItem(i, pStack);
                return true;
            } else {
                return false;
            }
        }
    }

    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {
        Raid raid = this.getCurrentRaid();
        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            ItemStack itemstack = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> map = Maps.newHashMap();
            if (pWave > raid.getNumGroups(Difficulty.NORMAL)) {
                map.put(Enchantments.QUICK_CHARGE, 2);
            } else if (pWave > raid.getNumGroups(Difficulty.EASY)) {
                map.put(Enchantments.QUICK_CHARGE, 1);
            }

            map.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
        }

    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }
}
