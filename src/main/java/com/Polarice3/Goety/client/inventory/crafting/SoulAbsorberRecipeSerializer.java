package com.Polarice3.Goety.client.inventory.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SoulAbsorberRecipeSerializer <T extends SoulAbsorberRecipes> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final int defaultSoulIncrease;
    private final int defaultCookingTime;
    private final SoulAbsorberRecipeSerializer.IFactory<T> factory;

    public SoulAbsorberRecipeSerializer(SoulAbsorberRecipeSerializer.IFactory<T> pFactory, int pDefaultSoulIncrease, int pDefaultCookingTime) {
        this.defaultSoulIncrease = pDefaultSoulIncrease;
        this.defaultCookingTime = pDefaultCookingTime;
        this.factory = pFactory;
    }

    @Override
    public T fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
        JsonElement jsonelement = JSONUtils.isArrayNode(pJson, "ingredient") ? JSONUtils.getAsJsonArray(pJson, "ingredient") : JSONUtils.getAsJsonObject(pJson, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(jsonelement);
        int s = JSONUtils.getAsInt(pJson, "soulIncrease", this.defaultSoulIncrease);
        int i = JSONUtils.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
        return this.factory.create(pRecipeId, ingredient, s, i);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
        Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
        int s = pBuffer.readVarInt();
        int i = pBuffer.readVarInt();
        return this.factory.create(pRecipeId, ingredient, i, s);
    }

    @Override
    public void toNetwork(PacketBuffer pBuffer, T pRecipe) {
        pRecipe.ingredient.toNetwork(pBuffer);
        pBuffer.writeVarInt(pRecipe.soulIncrease);
        pBuffer.writeVarInt(pRecipe.cookingTime);
    }

    interface IFactory<T extends SoulAbsorberRecipes> {
        T create(ResourceLocation p_create_1_, Ingredient p_create_3_, int p_create_4_, int p_create_5_);
    }
}
