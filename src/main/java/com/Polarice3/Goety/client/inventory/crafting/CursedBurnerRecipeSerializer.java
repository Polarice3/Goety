package com.Polarice3.Goety.client.inventory.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CursedBurnerRecipeSerializer <T extends ModCookingRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final int defaultCookingTime;
    private final CursedBurnerRecipeSerializer.IFactory<T> factory;

    public CursedBurnerRecipeSerializer(IFactory<T> pFactory, int pDefaultCookingTime) {
        this.defaultCookingTime = pDefaultCookingTime;
        this.factory = pFactory;
    }

    public T fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
        String s = JSONUtils.getAsString(pJson, "group", "");
        JsonElement jsonelement = JSONUtils.isArrayNode(pJson, "ingredient") ? JSONUtils.getAsJsonArray(pJson, "ingredient") : JSONUtils.getAsJsonObject(pJson, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(jsonelement);
        if (!pJson.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack itemstack;
        if (pJson.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"));
        else {
            String s1 = JSONUtils.getAsString(pJson, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                return new IllegalStateException("Item: " + s1 + " does not exist");
            }));
        }
        float f = JSONUtils.getAsFloat(pJson, "experience", 0.0F);
        int i = JSONUtils.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
        return this.factory.create(pRecipeId, s, ingredient, itemstack, f, i);
    }

    public T fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
        String s = pBuffer.readUtf(32767);
        Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
        ItemStack itemstack = pBuffer.readItem();
        float f = pBuffer.readFloat();
        int i = pBuffer.readVarInt();
        return this.factory.create(pRecipeId, s, ingredient, itemstack, f, i);
    }

    public void toNetwork(PacketBuffer pBuffer, T pRecipe) {
        pBuffer.writeUtf(pRecipe.group);
        pRecipe.ingredient.toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.result);
        pBuffer.writeFloat(pRecipe.experience);
        pBuffer.writeVarInt(pRecipe.cookingTime);
    }

    interface IFactory<T extends ModCookingRecipe> {
        T create(ResourceLocation p_create_1_, String p_create_2_, Ingredient p_create_3_, ItemStack p_create_4_, float p_create_5_, int p_create_6_);
    }
}
