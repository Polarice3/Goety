package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.Goety;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializer{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, Goety.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<CursedBurnerRecipes>> CURSED_BURNER_RECIPES = RECIPE_SERIALIZERS.register("cursed_burner_recipes",
            () -> new CursedBurnerRecipeSerializer<>(CursedBurnerRecipes::new, 200));

    public static final RegistryObject<IRecipeSerializer<SoulAbsorberRecipes>> SOUL_ABSORBER_RECIPES = RECIPE_SERIALIZERS.register("soul_absorber_recipes",
            () -> new SoulAbsorberRecipeSerializer<>(SoulAbsorberRecipes::new, 25, 200));

    public static final NonNullLazy<IRecipeType<RitualRecipe>> RITUAL_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("goety:ritual"));

    public static final NonNullLazy<IRecipeType<SoulAbsorberRecipes>> SOUL_ABSORBER_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("goety:soul_absorber_recipes"));

    public static final RegistryObject<IRecipeSerializer<RitualRecipe>> RITUAL = RECIPE_SERIALIZERS.register("ritual",
            () -> RitualRecipe.SERIALIZER);
}
