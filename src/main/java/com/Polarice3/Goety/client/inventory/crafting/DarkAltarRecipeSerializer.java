package com.Polarice3.Goety.client.inventory.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class DarkAltarRecipeSerializer<T extends ModSoulCraftRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final int defaultCookingTime;
    private final DarkAltarRecipeSerializer.IFactory<T> factory;

    public DarkAltarRecipeSerializer(IFactory<T> pFactory, int pDefaultCookingTime) {
        this.defaultCookingTime = pDefaultCookingTime;
        this.factory = pFactory;
    }

    public T fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
        String s = JSONUtils.getAsString(pJson, "group", "");
        String c = JSONUtils.getAsString(pJson, "craftType", "");
        JsonElement jsonelement = JSONUtils.isArrayNode(pJson, "main") ? JSONUtils.getAsJsonArray(pJson, "main") : JSONUtils.getAsJsonObject(pJson, "main");
        Ingredient ingredient = Ingredient.fromJson(jsonelement);
//        NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(pJson, "ingredients"));
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
        int f = JSONUtils.getAsInt(pJson, "soulCost", 0);
        int i = JSONUtils.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
        return this.factory.create(pRecipeId, s, c, ingredient, itemstack, f, i);
    }

    private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < pIngredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
            if (!ingredient.isEmpty()) {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    public T fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
        String s = pBuffer.readUtf(32767);
        String c = pBuffer.readUtf(32767);
        Ingredient main = Ingredient.fromNetwork(pBuffer);
        ItemStack itemstack = pBuffer.readItem();
        int f = pBuffer.readVarInt();
        int i = pBuffer.readVarInt();
        return this.factory.create(pRecipeId, s, c, main, itemstack, f, i);
    }

    public void toNetwork(PacketBuffer pBuffer, T pRecipe) {
        pBuffer.writeUtf(pRecipe.group);
        pRecipe.main.toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.result);
        pBuffer.writeVarInt(pRecipe.soulCost);
        pBuffer.writeVarInt(pRecipe.cookingTime);
    }

    interface IFactory<T extends ModSoulCraftRecipe> {
        T create(ResourceLocation p_create_1_, String p_create_2_, String p, Ingredient p_create_3_, ItemStack p_create_4_, int p_create_5_, int p_create_6_);
    }
}
