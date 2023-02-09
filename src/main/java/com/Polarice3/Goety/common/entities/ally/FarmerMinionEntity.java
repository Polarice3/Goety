package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ai.FarmingGoal;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FarmerMinionEntity extends ZombieMinionEntity {
    public final Inventory inventory = new Inventory(8);

    public FarmerMinionEntity(EntityType<? extends ZombieMinionEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected boolean isSunSensitive() {
        return true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new FarmingGoal<>(this));
        this.goalSelector.addGoal(3, new TakeSeedGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.getBlockEntity(this.blockPosition().below()) instanceof HopperTileEntity){
            HopperTileEntity tileEntity = (HopperTileEntity) this.level.getBlockEntity(this.blockPosition().below());
            if (tileEntity != null) {
                if (!this.inventory.isEmpty()) {
                    Inventory inventory = this.inventory;
                    for (int i = 0; i < inventory.getContainerSize(); ++i) {
                        ItemStack itemstack = inventory.getItem(i);
                        if (!(itemstack.getItem() instanceof IPlantable)) {
                            tileEntity.setItem(0, itemstack);
                        }
                    }
                }
            }
        }
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
        if (this.inventory.canAddItem(itemstack)) {
            this.onItemPickup(pItemEntity);
            this.inventory.addItem(itemstack);
            this.take(pItemEntity, itemstack.getCount());
            pItemEntity.remove();
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
                    ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack);
                    itemEntity.setDefaultPickUpDelay();
                    this.level.addFreshEntity(itemEntity);
                }
            }

        }
        super.die(pCause);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_HOE));
    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return spawnDataIn;
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand p_230254_2_) {
        if (!this.level.isClientSide){
            ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
            Item item = itemstack.getItem();
            ItemStack itemstack2 = this.getMainHandItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if (item == Items.ROTTEN_FLESH && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.heal(2.0F);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.CONSUME;
                }
                if (item instanceof HoeItem) {
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                    this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                    this.spawnAtLocation(itemstack2);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                    return ActionResultType.CONSUME;
                }
                if (pPlayer.isShiftKeyDown() || pPlayer.isCrouching()){
                    if (!this.inventory.isEmpty()) {
                        InventoryHelper.dropContents(this.level, pPlayer, this.inventory);
                        EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                        return ActionResultType.CONSUME;
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    static class TakeSeedGoal extends Goal {
        private static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> !itemEntity.hasPickUpDelay()
                && itemEntity.isAlive()
                && itemEntity.getItem().getItem() instanceof BlockItem && ((BlockItem) itemEntity.getItem().getItem()).getBlock() instanceof IPlantable;
        private final FarmerMinionEntity mob;

        public TakeSeedGoal(FarmerMinionEntity p_i50572_2_) {
            this.mob = p_i50572_2_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(16.0D, 8.0D, 16.0D), ALLOWED_ITEMS);
            for(int i = 0; i < this.mob.inventory.getContainerSize(); ++i) {
                if (!list.isEmpty() && this.mob.inventory.canAddItem(list.get(i).getItem())) {
                    return this.mob.getNavigation().moveTo(list.get(i), 1.15F);
                }
            }
            return false;
        }

        public void tick() {
            if (this.mob.getNavigation().getTargetPos().closerThan(this.mob.position(), 1.414D)) {
                List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(4.0D, 4.0D, 4.0D), ALLOWED_ITEMS);
                if (!list.isEmpty()) {
                    this.mob.pickUpItem(list.get(0));
                }
            }

        }
    }
}
