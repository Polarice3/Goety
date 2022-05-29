package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class HuskarlEntity extends ZombieEntity {
    public boolean shade;

    public HuskarlEntity(EntityType<? extends HuskarlEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    public void tick(){
        super.tick();
        if (this.isAggressive()){
            this.maxUpStep = 2.0F;
        } else {
            this.maxUpStep = 1.0F;
        }
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.HUSK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    public void setShade(boolean shade){
        this.shade = shade;
    }

    public boolean isShade(){
        return this.shade;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("shade", this.isShade());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.shade = compound.getBoolean("shade");
        this.setShade(this.shade);
    }

    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (this.isShade()) {
                ShadeEntity shade = this.convertTo(ModEntityType.SHADE.get(), false);
                shade.setPos(this.getX(), this.getY(), this.getZ());
                if (this.hasCustomName()) {
                    shade.setCustomName(this.getCustomName());
                }
                shade.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                serverWorld.addFreshEntity(shade);
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && this.getMainHandItem().isEmpty() && pEntity instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)pEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 140 * (int)f));
        }

        return flag;
    }

    protected boolean convertsInWater() {
        return true;
    }

    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.DROWNED);
        if (!this.isSilent()) {
            this.level.levelEvent((PlayerEntity)null, 1040, this.blockPosition(), 0);
        }

    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        int random = this.random.nextInt(12);
        int random2 = this.random.nextInt(2);
        switch (random){
            case 0:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
                break;
            case 2:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                break;
        }
        switch (random2){
            case 0:
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
        }
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

}
