package com.Polarice3.Goety.client.inventory.container;

import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class FocusBagContainer extends Container {
    private final ItemStack stack;

    public static FocusBagContainer createContainerClientSide(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new FocusBagContainer(id, inventory, new FocusBagItemHandler(ItemStack.EMPTY), ItemStack.EMPTY);
    }

    public FocusBagContainer(int id, PlayerInventory playerInventory, FocusBagItemHandler handler, ItemStack stack) {
        super(ModContainerType.FOCUSBAG.get(), id);
        this.stack = stack;
        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(handler, i + 1, 62 - 18 + i * 18, 25));
        }
        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(handler,6 + i, 62 - 18 + i * 18, 43));
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ItemStack quickMoveStack(PlayerEntity pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 11) {
                if (!this.moveItemStackTo(itemstack1, 11, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 11, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return (player.getMainHandItem() == stack || player.getOffhandItem() == stack) && !stack.isEmpty();
    }
}
