package com.Polarice3.Goety.client.inventory.container;
/*

import com.Polarice3.Goety.common.tileentities.SoulForgeTileEntity;
import com.Polarice3.Goety.init.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class SoulForgeContainer extends Container {
    public final SoulForgeTileEntity tileEntity;
    private final IWorldPosCallable worldPosCallable;
    private final IIntArray field_216983_d;

    public SoulForgeContainer(int id, PlayerInventory playerInventory, SoulForgeTileEntity tileEntity) {
        super(ModContainerType.SOULFORGE.get(), id);
        this.tileEntity = tileEntity;
        this.levelPosCallable = IWorldPosCallable.of(tileEntity.getLevel(), tileEntity.getPos());
        this.addSlot(new InputSlot(playerInventory, 0, 51, 21));
        this.addSlot(new FuelSlot(this, playerInventory, 1, 80, 54));
        this.addSlot(new ResultSlot(playerInventory.player, playerInventory, 2, 113, 22));
        this.field_216983_d = tileEntity.soulforgeData;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public SoulForgeContainer(int id, PlayerInventory playerInventory, PacketBuffer data) {
        this(id, playerInventory, soulForgeTileEntity(playerInventory, data));
    }

    private static SoulForgeTileEntity soulForgeTileEntity(PlayerInventory playerInventory, PacketBuffer data){
        Objects.requireNonNull(playerInventory, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Packet Buffer cannot be null");
        TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if (tileEntity instanceof SoulForgeTileEntity){
            return (SoulForgeTileEntity) tileEntity;
        }
        throw new IllegalStateException("Wrong Tile Entity");
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return isWithinUsableDistance(worldPosCallable, playerIn, RegistryHandler.SOULFORGE.get());
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                */
/*if (this.hasRecipe(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else*//*

                if (index >= 3 && index < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.field_216983_d.get(0);
        int j = this.field_216983_d.get(2);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFuelTotal() {
        return this.field_216983_d.get(1);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isOnFire() {
        return this.field_216983_d.get(0) > 0;
    }

    static class InputSlot extends Slot {
        public InputSlot(IInventory iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(stack);
        }

        public int getSlotStackLimit() {
            return 64;
        }
    }

    class FuelSlot extends Slot {
        private final SoulForgeContainer furnaceContainer;

        public FuelSlot(SoulForgeContainer furnaceContainer, IInventory furnaceInventory, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
            super(furnaceInventory, p_i50084_3_, p_i50084_4_, p_i50084_5_);
            this.furnaceContainer = furnaceContainer;
        }

        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == RegistryHandler.GOLDTOTEM.get();
        }

    }

    public class ResultSlot extends Slot {
        private final PlayerEntity player;
        private int removeCount;

        public ResultSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
            super(inventoryIn, slotIndex, xPosition, yPosition);
            this.player = player;
        }

        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        public ItemStack decrStackSize(int amount) {
            if (this.hasItem()) {
                this.removeCount += Math.min(amount, this.getItem().getCount());
            }

            return super.decrStackSize(amount);
        }

        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            super.onTake(thePlayer, stack);
            return stack;
        }

        protected void onCrafting(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        protected void onCrafting(ItemStack stack) {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            this.removeCount = 0;
        }
    }


}
*/
