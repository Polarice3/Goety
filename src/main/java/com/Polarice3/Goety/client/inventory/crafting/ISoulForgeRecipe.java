package com.Polarice3.Goety.client.inventory.crafting;

import net.minecraft.item.ItemStack;

public interface ISoulForgeRecipe {

    boolean isInput(ItemStack input);

    ItemStack getOutput(ItemStack input);
}
