package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class CursedBurnerRecipes extends ModCookingRecipe {
    public CursedBurnerRecipes(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipeType.CURSED_BURNER_RECIPES, pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.CURSED_BURNER.get());
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.CURSED_BURNER_RECIPES.get();
    }
}
