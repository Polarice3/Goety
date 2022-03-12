package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.common.entities.projectiles.WitchBombEntity;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FanaticEntity extends AbstractCultistEntity {

    public FanaticEntity(EntityType<? extends FanaticEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(2, new ThrowBombsGoal(this));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public boolean hasBomb(){
        return this.getItemInHand(Hand.OFF_HAND).getItem() == ModRegistry.WITCHBOMB.get();
    }

    static class ThrowBombsGoal extends Goal{
        public int bombTimer;
        public FanaticEntity fanatic;

        public ThrowBombsGoal(FanaticEntity fanatic){
            this.fanatic = fanatic;
        }

        @Override
        public boolean canUse() {
            if (this.fanatic.getTarget() != null && this.fanatic.hasBomb()){
                LivingEntity entity = this.fanatic.getTarget();
                return this.fanatic.distanceTo(entity) > 2.0;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.fanatic.getTarget() != null && this.fanatic.hasBomb();
        }

        @Override
        public void stop() {
            this.bombTimer = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.bombTimer;
            if (this.bombTimer >= 60) {
                LivingEntity livingEntity = this.fanatic.getTarget();
                WitchBombEntity snowballentity = new WitchBombEntity(this.fanatic.level, this.fanatic);
                Vector3d vector3d = livingEntity.getDeltaMovement();
                double d0 = livingEntity.getX() + vector3d.x - this.fanatic.getX();
                double d1 = livingEntity.getEyeY() - (double) 1.1F - this.fanatic.getY();
                double d2 = livingEntity.getZ() + vector3d.z - this.fanatic.getZ();
                float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                snowballentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 2.0F);
                this.fanatic.playSound(SoundEvents.WITCH_THROW, 1.0F, 0.4F / (this.fanatic.getRandom().nextFloat() * 0.4F + 0.8F));
                this.fanatic.level.addFreshEntity(snowballentity);
                this.bombTimer = 0;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isAggressive()) {
            if (!hasBomb()) {
                return ArmPose.ATTACKING;
            } else {
                return ArmPose.BOMB_AND_WEAPON;
            }
        } else {
            return ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        int random = this.random.nextInt(9);
        int random2 = this.random.nextInt(5);
        int random3 = this.random.nextInt(4);
        switch (random){
            case 0:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
                break;
            case 2:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                break;
            case 4:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                break;
            case 5:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                break;
            case 6:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                break;
            case 7:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
                break;
            case 8:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_PICKAXE));
        }
        switch (random2){
            case 0:
            case 1:
            case 2:
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                break;
            case 4:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        }
        if (random3 == 0) {
            this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModRegistry.WITCHBOMB.get()));
        }
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }
}
