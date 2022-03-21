package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class DarkAltarRecipes extends ModSoulCraftRecipe {
    public DarkAltarRecipes(ResourceLocation pId, String pGroup, String pCraftType, Ingredient pMain, ItemStack pResult, int pSoulCost, int pCookingTime) {
        super(ModRecipeType.DARK_ALTAR_RECIPES, pId, pGroup, pCraftType, pMain, pResult, pSoulCost, pCookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModRegistry.DARK_ALTAR.get());
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.DARK_ALTAR_RECIPES.get();
    }
}
