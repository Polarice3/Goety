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

import java.util.ArrayList;
import java.util.List;

public class SoulForgeRecipe implements IRecipe<IInventory> {
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookTime;
    private static List<ISoulForgeRecipe> recipes = new ArrayList<ISoulForgeRecipe>();


    public SoulForgeRecipe(IRecipeType<?> typeIn, ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        this.type = typeIn;
        this.id = idIn;
        this.group = groupIn;
        this.ingredient = ingredientIn;
        this.result = resultIn;
        this.experience = experienceIn;
        this.cookTime = cookTimeIn;
    }

    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    public static boolean canbeCrafted(NonNullList<ItemStack> inputs, int[] inputIndexes)
    {
        for (int i : inputIndexes)
        {
            if (hasOutput(inputs.get(i)))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean hasOutput(ItemStack input)
    {
        return !getOutput(input).isEmpty();
    }

    public static ItemStack getOutput(ItemStack input)
    {
        if (input.isEmpty() || input.getCount() != 1) return ItemStack.EMPTY;

        for (ISoulForgeRecipe recipe : recipes)
        {
            ItemStack output = recipe.getOutput(input);
            if (!output.isEmpty())
            {
                return output;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean canFit(int width, int height) {
        return true;
    }

    public ItemStack getRecipeOutput() {
        return this.result;
    }

    public String getGroup() {
        return this.group;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    public IRecipeType<?> getType() {
        return this.type;
    }
}
