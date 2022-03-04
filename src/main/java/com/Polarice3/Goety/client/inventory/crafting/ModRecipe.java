package com.Polarice3.Goety.client.inventory.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class ModRecipe implements IRecipe<RecipeWrapper> {
    public final ResourceLocation name;

    public ModRecipe(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return true;
    }

    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return ItemStack.EMPTY;
    }

    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return this.name;
    }
}
