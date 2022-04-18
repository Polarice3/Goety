package com.Polarice3.Goety.compat.jei;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.init.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class GoetyJeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CursedBurnerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURSED_BURNER.get()), CursedBurnerCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DARK_ALTAR.get()), ModRecipeSerializer.RITUAL.getId());
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PEDESTAL.get()), ModRecipeSerializer.RITUAL.getId());
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURSED_KILN.get()), VanillaRecipeCategoryUid.FURNACE);
    }

    public void registerRecipes(IRecipeRegistration registration) {
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().level);
        RecipeManager recipeManager = world.getRecipeManager();
        registration.addRecipes(JeiRecipes.getCursedBurnerRecipes(world), CursedBurnerCategory.UID);
        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ModRecipeSerializer.RITUAL_TYPE.get());
        registration.addRecipes(ritualRecipes, ModRecipeSerializer.RITUAL.getId());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Goety.MOD_ID, "jei_plugin");
    }
}
