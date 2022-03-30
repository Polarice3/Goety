package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ai.CreatureBowAttackGoal;
import com.Polarice3.Goety.common.entities.hostile.ScorchEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class SkeletonMinionEntity extends SummonedEntity implements IRangedAttackMob {
    private final CreatureBowAttackGoal<SkeletonMinionEntity> bowGoal = new CreatureBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {

        public void stop() {
            super.stop();
            SkeletonMinionEntity.this.setAggressive(false);
        }

        public void start() {
            super.start();
            SkeletonMinionEntity.this.setAggressive(true);
        }
    };

    public SkeletonMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(this.isUpgraded());
    }

    public void tick() {
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 2.0F);
        }
        if (this.getTrueOwner() != null) {
            if (MainConfig.SkeletonLimit.get() < SkeletonLimit(this.getTrueOwner())) {
                if (this.tickCount % 20 == 0) {
                    this.hurt(DamageSource.STARVE, 5.0F);
                }
            }
        }
        super.tick();
    }

    @Override
    protected boolean isSunSensitive() {
        return true;
    }

    public int SkeletonLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(SkeletonMinionEntity.class, this.summonCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(64.0D)).size();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new CreatureBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));

    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem));
            if (itemstack.getItem() == Items.BOW) {
                int i = 20;
                if (!this.isUpgraded()) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.reassessWeaponGoal();
    }

    public void setItemSlot(EquipmentSlotType pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entityIn.setSecondsOnFire(2 * (int)f);
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != ModRegistry.HOSTED.get();
    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        float f = difficultyIn.getSpecialMultiplier();
        this.reassessWeaponGoal();
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }
        return spawnDataIn;
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getMobArrow(itemstack, distanceFactor);
        abstractarrowentity = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity getMobArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, arrowStack, distanceFactor);
        int random = this.level.random.nextInt(4);
        if (this.isUpgraded() && abstractarrowentity instanceof ArrowEntity && random == 0) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.WEAKNESS, 300));
        }

        return abstractarrowentity;
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.BOW;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.74F;
    }

    public double getMyRidingOffset() {
        return -0.6D;
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && p_230254_1_ == this.getTrueOwner()) {
            if (item == Items.BONE && this.getHealth() < this.getMaxHealth()) {
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.heal(2.0F);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.CONSUME;
            }
            if (item instanceof SwordItem){
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
                this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                this.spawnAtLocation(itemstack2);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.CONSUME;
            }
            if (item instanceof BowItem){
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
                this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                this.spawnAtLocation(itemstack2);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.CONSUME;
            }
            if (item instanceof ArmorItem){
                ItemStack helmet = this.getItemBySlot(EquipmentSlotType.HEAD);
                ItemStack chestplate = this.getItemBySlot(EquipmentSlotType.CHEST);
                ItemStack legging = this.getItemBySlot(EquipmentSlotType.LEGS);
                ItemStack boots = this.getItemBySlot(EquipmentSlotType.FEET);
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                if (((ArmorItem) item).getSlot() == EquipmentSlotType.HEAD){
                    this.setItemSlot(EquipmentSlotType.HEAD, itemstack.copy());
                    this.setGuaranteedDrop(EquipmentSlotType.HEAD);
                    this.spawnAtLocation(helmet);
                }
                if (((ArmorItem) item).getSlot() == EquipmentSlotType.CHEST){
                    this.setItemSlot(EquipmentSlotType.CHEST, itemstack.copy());
                    this.setGuaranteedDrop(EquipmentSlotType.CHEST);
                    this.spawnAtLocation(chestplate);
                }
                if (((ArmorItem) item).getSlot() == EquipmentSlotType.LEGS){
                    this.setItemSlot(EquipmentSlotType.LEGS, itemstack.copy());
                    this.setGuaranteedDrop(EquipmentSlotType.LEGS);
                    this.spawnAtLocation(legging);
                }
                if (((ArmorItem) item).getSlot() == EquipmentSlotType.FEET){
                    this.setItemSlot(EquipmentSlotType.FEET, itemstack.copy());
                    this.setGuaranteedDrop(EquipmentSlotType.FEET);
                    this.spawnAtLocation(boots);
                }
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.CONSUME;
            } else {
                return ActionResultType.PASS;
            }
        } else {
            return ActionResultType.PASS;
        }
    }
}
