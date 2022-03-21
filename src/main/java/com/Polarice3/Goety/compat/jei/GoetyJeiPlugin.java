package com.Polarice3.Goety.compat.jei;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

@JeiPlugin
public class GoetyJeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CursedBurnerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DarkAltarCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModRegistry.CURSED_BURNER.get()), CursedBurnerCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModRegistry.DARK_ALTAR.get()), DarkAltarCategory.UID);
    }

    public void registerRecipes(IRecipeRegistration registration) {
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().level);
        registration.addRecipes(JeiRecipes.getCursedBurnerRecipes(world), CursedBurnerCategory.UID);
        registration.addRecipes(JeiRecipes.getDarkAltarRecipes(world), DarkAltarCategory.UID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Goety.MOD_ID, "jei_plugin");
    }
}
