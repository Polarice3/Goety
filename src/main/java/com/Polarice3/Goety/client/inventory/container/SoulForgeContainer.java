package com.Polarice3.Goety.client.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

public class SoulForgeContainer extends Container {
    private final IInventory container;
    private final IIntArray data;
    protected final World level;

    protected SoulForgeContainer(ContainerType<?> pMenuType,  int pContainerId, PlayerInventory pPlayerInventory) {
        this(pMenuType, pContainerId, pPlayerInventory, new Inventory(3), new IntArray(4));
    }

    public SoulForgeContainer(ContainerType<?> pMenuType, int pContainerId, PlayerInventory pPlayerInventory, Inventory inventory, IntArray intArray) {
        super(pMenuType, pContainerId);
        checkContainerSize(inventory, 3);
        checkContainerDataCount(intArray, 4);
        this.container = inventory;
        this.data = intArray;
        this.level = pPlayerInventory.player.level;
        this.addSlot(new Slot(inventory, 0, 56, 17));
        this.addSlot(new FurnaceResultSlot(pPlayerInventory.player, inventory, 1, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(data);

    }

    public int getResultSlotIndex() {
        return 1;
    }


    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return false;
    }
}
