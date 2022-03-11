package com.Polarice3.Goety.compat.jei;

import com.Polarice3.Goety.client.inventory.crafting.CursedBurnerRecipes;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.World;

import java.util.Set;

public class JeiRecipes {
    public static Set<CursedBurnerRecipes> getCursedBurnerRecipes(World world)
    {
        return ImmutableSet.copyOf(world.getRecipeManager().getAllRecipesFor(ModRecipeType.CURSED_BURNER_RECIPES));
    }
}
