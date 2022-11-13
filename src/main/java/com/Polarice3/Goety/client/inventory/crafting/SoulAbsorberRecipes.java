package com.Polarice3.Goety.client.inventory.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SoulAbsorberRecipes implements IRecipe<IInventory> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final int soulIncrease;
    protected final int cookingTime;

    public SoulAbsorberRecipes(ResourceLocation pId, Ingredient pIngredient, int pSouls, int pCookingTime) {
        this.id = pId;
        this.ingredient = pIngredient;
        this.soulIncrease = pSouls;
        this.cookingTime = pCookingTime;
    }

    public boolean matches(IInventory pInv, World pLevel) {
        return this.ingredient.test(pInv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public int getSoulIncrease(){
        return this.soulIncrease;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.SOUL_ABSORBER_RECIPES.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeSerializer.SOUL_ABSORBER_TYPE.get();
    }
}
