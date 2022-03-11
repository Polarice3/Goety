package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.Goety;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializer{
    public static DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Goety.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<CursedBurnerRecipes>> CURSED_BURNER_RECIPES = RECIPE_SERIALIZERS.register("cursed_burner_recipes",
            () -> new CursedBurnerRecipeSerializer<>(CursedBurnerRecipes::new, 200));


}
