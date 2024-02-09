package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.common.entities.ai.StealTotemGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.function.Predicate;

public abstract class HuntingIllagerEntity extends SpellcastingIllagerEntity {
    private static final DataParameter<Boolean> RIDER = EntityDataManager.defineId(HuntingIllagerEntity.class, DataSerializers.BOOLEAN);
    public final Predicate<Entity> field_213690_b = Entity::isAlive;
    public final Inventory inventory = new Inventory(1);

    protected HuntingIllagerEntity(EntityType<? extends HuntingIllagerEntity> p_i48551_1_, World p_i48551_2_) {
        super(p_i48551_1_, p_i48551_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new StealTotemGoal<>(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public void tick(){
        super.tick();
        EquipmentSlotType equipmentslottype = EquipmentSlotType.OFFHAND;
        ItemStack itemstack1 = this.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            if (!this.inventory.isEmpty()) {
                for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = this.inventory.getItem(i);
                    if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                        this.setItemSlot(equipmentslottype, itemstack);
                    }
                }
            }
        }
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D), field_213690_b)) {
            if (this.isRider()){
                if (entity instanceof RavagerEntity){
                    RavagerEntity ravagerEntity = (RavagerEntity) entity;
                    if (!ravagerEntity.isVehicle() && !this.isPassenger()){
                        this.startRiding(ravagerEntity, true);
                    }
                }
            }
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RIDER, false);
    }

    public boolean isRider(){
        return this.entityData.get(RIDER);
    }

    public void setRider(boolean pIsRider){
        this.entityData.set(RIDER, pIsRider);
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

    public void pickUpItem(ItemEntity pItemEntity) {
        ItemStack itemstack = pItemEntity.getItem();
        if (itemstack.getItem() instanceof ITotem) {
            if (this.inventory.canAddItem(itemstack)) {
                this.onItemPickup(pItemEntity);
                this.inventory.addItem(itemstack);
                this.take(pItemEntity, itemstack.getCount());
                pItemEntity.remove();
            } else {
                super.pickUpItem(pItemEntity);
            }
        } else if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
            EquipmentSlotType equipmentslottype = EquipmentSlotType.OFFHAND;
            ItemStack itemstack1 = this.getItemBySlot(equipmentslottype);
            if (itemstack1.isEmpty()) {
                this.onItemPickup(pItemEntity);
                this.setItemSlot(equipmentslottype, itemstack);
                this.take(pItemEntity, itemstack.getCount());
                pItemEntity.remove();
            } else if (this.inventory.canAddItem(itemstack)) {
                this.onItemPickup(pItemEntity);
                this.inventory.addItem(itemstack);
                this.take(pItemEntity, itemstack.getCount());
                pItemEntity.remove();
            } else {
                super.pickUpItem(pItemEntity);
            }
        } else {
            super.pickUpItem(pItemEntity);
        }

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

    public void die(DamageSource pCause) {
        if (!this.inventory.isEmpty()){
            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (itemstack != ItemStack.EMPTY){
                    if (itemstack.getItem() instanceof ITotem){
                        ITotem.increaseSouls(itemstack, MainConfig.IllagerSouls.get());
                    }
                    ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack);
                    itemEntity.setDefaultPickUpDelay();
                    this.level.addFreshEntity(itemEntity);
                }
            }

        }
        super.die(pCause);
    }
}
