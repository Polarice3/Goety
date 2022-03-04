package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nonnull;

public class PhilosophersStoneItem extends Item implements IForgeItem {
    public PhilosophersStoneItem(){
        super(new Properties().tab(Goety.TAB).durability(64));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        container.setDamageValue(itemStack.getDamageValue() + 1);
        return container;
    }

}
