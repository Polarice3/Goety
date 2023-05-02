package com.Polarice3.Goety.client.inventory.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ModShapelessRecipe implements IRecipe<CraftingInventory> {
   private final ResourceLocation id;
   private final String group;
   private final ItemStack result;
   private final NonNullList<Ingredient> ingredients;
   private final boolean isSimple;

   public ModShapelessRecipe(ResourceLocation pId, String pGroup, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
      this.id = pId;
      this.group = pGroup;
      this.result = pResult;
      this.ingredients = pIngredients;
      this.isSimple = pIngredients.stream().allMatch(Ingredient::isSimple);
   }

   public IRecipeType<?> getType() {
      return IRecipeType.CRAFTING;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public IRecipeSerializer<?> getSerializer() {
      return ModRecipeSerializer.MODDED_SHAPELESS.get();
   }

   public String getGroup() {
      return this.group;
   }

   public ItemStack getResultItem() {
      return this.result;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.ingredients;
   }

   public boolean matches(CraftingInventory pInv, World pLevel) {
      RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
      java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
      int i = 0;

      for(int j = 0; j < pInv.getContainerSize(); ++j) {
         ItemStack itemstack = pInv.getItem(j);
         if (!itemstack.isEmpty()) {
            ++i;
            if (isSimple)
            recipeitemhelper.accountStack(itemstack, 1);
            else inputs.add(itemstack);
         }
      }

      return i == this.ingredients.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
   }

   public ItemStack assemble(CraftingInventory pInv) {
      return this.result.copy();
   }

   public boolean canCraftInDimensions(int pWidth, int pHeight) {
      return pWidth * pHeight >= this.ingredients.size();
   }

   public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ModShapelessRecipe> {
      private static final ResourceLocation NAME = new ResourceLocation("minecraft", "crafting_shapeless");
      public ModShapelessRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
         String s = JSONUtils.getAsString(pJson, "group", "");
         NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(pJson, "ingredients"));
         if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
         } else if (nonnulllist.size() > 3 * 3) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
         } else {
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"));
            return new ModShapelessRecipe(pRecipeId, s, itemstack, nonnulllist);
         }
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

      public ModShapelessRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
         String s = pBuffer.readUtf(32767);
         int i = pBuffer.readVarInt();
         NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

         for(int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.fromNetwork(pBuffer));
         }

         ItemStack itemstack = pBuffer.readItem();
         return new ModShapelessRecipe(pRecipeId, s, itemstack, nonnulllist);
      }

      public void toNetwork(PacketBuffer pBuffer, ModShapelessRecipe pRecipe) {
         pBuffer.writeUtf(pRecipe.group);
         pBuffer.writeVarInt(pRecipe.ingredients.size());

         for(Ingredient ingredient : pRecipe.ingredients) {
            ingredient.toNetwork(pBuffer);
         }

         pBuffer.writeItem(pRecipe.result);
      }
   }
}
