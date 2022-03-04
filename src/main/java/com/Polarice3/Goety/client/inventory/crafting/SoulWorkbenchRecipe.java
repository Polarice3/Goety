package com.Polarice3.Goety.client.inventory.crafting;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class SoulWorkbenchRecipe {
    Object[] craftSlots, totemSlot;
    ItemStack result;
    ResourceLocation registryName;

    public SoulWorkbenchRecipe(Object[] core, Object[] extras, ItemStack result) {
        this.craftSlots = core;
        this.totemSlot = extras;
        this.result = result;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public SoulWorkbenchRecipe setRegistryName(String domain, String path) {
        this.registryName = new ResourceLocation(domain, path);
        return this;
    }

    public SoulWorkbenchRecipe setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    public Object[] getCraftSlots() {
        return craftSlots;
    }

    public Object[] getTotemSlot() {
        return totemSlot;
    }

    static boolean matches(Object match, ItemStack stack) {
        if (match instanceof ItemStack) {
            return ItemStack.isSame((ItemStack) match, stack);
        }
        else if (match instanceof Item) {
            return (Item) match == stack.getItem();
        }
        else if (match instanceof Block) {
            return ((Block) match).asItem() == stack.getItem();
        }
        else if (match instanceof ITag) {
            return ((ITag<Item>) match).contains(stack.getItem());
        }
        return false;
    }

    public boolean matches(IInventory coreInv, IInventory extraInv) {
        if (coreInv.getContainerSize() < 9 || extraInv.getContainerSize() < 1) return false;
        for (int i = 0; i < craftSlots.length; i ++) {
            if (!matches(craftSlots[i], coreInv.getItem(i))) return false;
        }
        for (int i = 0; i < totemSlot.length; i ++) {
            if (!matches(totemSlot[i], extraInv.getItem(i))) return false;
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(IInventory coreInv, IInventory extraInv) {
        NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

        for(int i = 0; i < items.size(); ++i) {
            IInventory inv = i < 9 ? coreInv : extraInv;
            ItemStack item = inv.getItem(i < 9 ? i : i - 9);
            if (item.hasContainerItem()) {
                items.set(i, item.getContainerItem());
            }
        }

        return items;
    }

    public ItemStack getResult() {
        return result.copy();
    }
}
