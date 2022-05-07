package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.Goety;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Optional;

public interface ModRecipeType<T extends IRecipe<?>> {
    IRecipeType<CursedBurnerRecipes> CURSED_BURNER_RECIPES = register("cursed_burner_recipes");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String pIdentifier) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Goety.MOD_ID, pIdentifier), new IRecipeType<T>() {
            public String toString() {
                return pIdentifier;
            }
        });
    }

    default <C extends IInventory> Optional<T> tryMatch(IRecipe<C> pRecipe, World pLevel, C pContainer) {
        return pRecipe.matches(pContainer, pLevel) ? Optional.of((T)pRecipe) : Optional.empty();
    }
}
