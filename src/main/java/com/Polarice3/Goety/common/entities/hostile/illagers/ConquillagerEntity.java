package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.ParticleUtil;
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
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConquillagerEntity extends HuntingIllagerEntity implements ICrossbowUser {
    private static final DataParameter<Boolean> IS_CHARGING_CROSSBOW = EntityDataManager.defineId(ConquillagerEntity.class, DataSerializers.BOOLEAN);
    private final Predicate<Entity> field_213690_b = Entity::isAlive;

    public ConquillagerEntity(EntityType<? extends ConquillagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new FindTargetGoal(this, 10.0F));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0D, 16.0F));
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
                if (entity instanceof PlayerEntity){
                    if (!((PlayerEntity) entity).isCreative()){
                        entity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), 12000, 0, false, false));

                    }
                } else if (entity instanceof AbstractVillagerEntity) {
                    entity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), 12000, 0, false, false));
                } else {
                    entity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), 2000, 0, false, false));
                }
            }
        }
        if (this.level.isClientSide) {
            if (this.tickCount % 20 == 0){
                for(int i = 0; i < 8; ++i) {
                    new ParticleUtil(ModParticleTypes.PLAGUE_EFFECT.get(), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.5D, 0.0D);
                }
            }
        }
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return null;
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

    public void shootCrossbowProjectile(LivingEntity shooter, ItemStack itemStack, ProjectileEntity projectileEntity, float p_230284_4_) {
        this.shootCrossbowProjectile(this, shooter, projectileEntity, p_230284_4_, 1.6F);
    }

    public void performCrossbowAttack(LivingEntity shooter, float velocity) {
        Hand hand = ProjectileHelper.getWeaponHoldingHand(shooter, item -> item instanceof CrossbowItem);
        ItemStack itemstack = shooter.getItemInHand(hand);
        if (shooter.isHolding(item -> item instanceof CrossbowItem)) {
            CrossbowItem.performShooting(shooter.level, shooter, hand, itemstack, velocity, (float)(14 - shooter.level.getDifficulty().getId() * 4));
        }

        this.onCrossbowAttackPerformed();
    }

    public void shootCrossbowProjectile(LivingEntity shooter, LivingEntity target, ProjectileEntity projectileEntity, float p_234279_4_, float velocity) {
        double d0 = target.getX() - shooter.getX();
        double d1 = target.getY(0.5F) - shooter.getY(0.5F);
        double d2 = target.getZ() - shooter.getZ();
        Vector3f vector3f = this.getProjectileShotVector(shooter, new Vector3d(d0, d1, d2), p_234279_4_);
        projectileEntity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, (float)(14 - shooter.level.getDifficulty().getId() * 4));
        shooter.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
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
