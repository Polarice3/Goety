package com.Polarice3.Goety.client.inventory.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ModSoulCraftRecipe implements IRecipe<IInventory> {
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final String craftType;
    protected final Ingredient main;
    protected final int soulCost;
    protected final ItemStack result;
    protected final int cookingTime;

    public ModSoulCraftRecipe(IRecipeType<?> pType, ResourceLocation pId, String pGroup, String pCraftType, Ingredient pMain, ItemStack pResult, int pSoulCost, int pCookingTime) {
        this.type = pType;
        this.id = pId;
        this.group = pGroup;
        this.craftType = pCraftType;
        this.main = pMain;
        this.result = pResult;
        this.soulCost = pSoulCost;
        this.cookingTime = pCookingTime;
    }

    public boolean matches(IInventory pInv, World pLevel) {
        return this.main.test(pInv.getItem(0));
    }

    public ItemStack assemble(IInventory pInv) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public Ingredient getMain() {
        return this.main;
    }

    public int getSoulCost() {
        return this.soulCost;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public String getGroup() {
        return this.group;
    }

    public String getCraftType() {
        return this.craftType;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeType<?> getType() {
        return this.type;
    }
}
