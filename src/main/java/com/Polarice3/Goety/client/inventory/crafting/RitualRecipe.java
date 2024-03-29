package com.Polarice3.Goety.client.inventory.crafting;

import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.init.ModRituals;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class RitualRecipe extends ModShapelessRecipe {
    public static Serializer SERIALIZER = new Serializer();

    private final ResourceLocation ritualType;
    private final Ritual ritual;
    private final String craftType;
    private final int soulCost;
    private final Ingredient activationItem;
    private final ITag<EntityType<?>> entityToSacrifice;
    private final ITag<EntityType<?>> entityToConvert;
    private final EntityType<?> entityToSummon;
    private final EntityType<?> entityToConvertInto;
    private final int duration;
    private final int summonLife;
    private final float durationPerIngredient;
    private final String entityToSacrificeDisplayName;
    private final String entityToConvertDisplayName;

    public RitualRecipe(ResourceLocation id, String group, String pCraftType, ResourceLocation ritualType,
                        ItemStack result, EntityType<?> entityToSummon, EntityType<?> entityToConvertInto, Ingredient activationItem, NonNullList<Ingredient> input, int duration, int summonLife, int pSoulCost,
                        ITag<EntityType<?>> entityToSacrifice, String entityToSacrificeDisplayName,
                        ITag<EntityType<?>> entityToConvert, String entityToConvertDisplayName) {
        super(id, group, result, input);
        this.craftType = pCraftType;
        this.soulCost = pSoulCost;
        this.entityToSummon = entityToSummon;
        this.entityToConvertInto = entityToConvertInto;
        this.ritualType = ritualType;
        this.ritual = ModRituals.RITUAL_FACTORIES.getValue(this.ritualType).create(this);
        this.activationItem = activationItem;
        this.duration = duration;
        this.summonLife = summonLife;
        this.durationPerIngredient = this.duration / (float) (this.getIngredients().size() + 1);
        this.entityToSacrifice = entityToSacrifice;
        this.entityToSacrificeDisplayName = entityToSacrificeDisplayName;
        this.entityToConvert = entityToConvert;
        this.entityToConvertDisplayName = entityToConvertDisplayName;
    }

    public String getCraftType() {
        return this.craftType;
    }

    public int getSoulCost() {
        return this.soulCost;
    }

    public Ingredient getActivationItem() {
        return this.activationItem;
    }

    public int getDuration() {
        return this.duration;
    }

    public float getDurationPerIngredient() {
        return this.durationPerIngredient;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public boolean matches(World world, BlockPos darkAltarPos, ItemStack activationItem) {
        return this.ritual.identify(world, darkAltarPos, activationItem);
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory inventory, @Nonnull World world) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory inventoryCrafting) {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeSerializer.RITUAL_TYPE.get();
    }

    public ITag<EntityType<?>> getEntityToSacrifice() {
        return this.entityToSacrifice;
    }

    public boolean requiresSacrifice() {
        return this.entityToSacrifice != null;
    }

    public EntityType<?> getEntityToSummon() {
        return this.entityToSummon;
    }

    public EntityType<?> getEntityToConvertInto() {
        return this.entityToConvertInto;
    }

    public ITag<EntityType<?>> getEntityToConvert() {
        return this.entityToConvert;
    }

    public boolean isConversion(){
        return this.entityToConvert != null && this.entityToConvertInto != null;
    }

    public ResourceLocation getRitualType() {
        return this.ritualType;
    }

    public Ritual getRitual() {
        return this.ritual;
    }

    public String getEntityToSacrificeDisplayName() {
        return this.entityToSacrificeDisplayName;
    }

    public String getEntityToConvertDisplayName() {
        return this.entityToConvertDisplayName;
    }

    public int getSummonLife() {
        return this.summonLife;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RitualRecipe> {
        private static final ModShapelessRecipe.Serializer serializer = new ModShapelessRecipe.Serializer();

        @Override
        public RitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            String group = JSONUtils.getAsString(json, "group", "");
            String craftType = JSONUtils.getAsString(json, "craftType", "");
            NonNullList<Ingredient> ingredients = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

            ResourceLocation ritualType = new ResourceLocation(json.get("ritual_type").getAsString());

            EntityType<?> entityToSummon = null;
            if (json.has("entity_to_summon")) {
                entityToSummon = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(json, "entity_to_summon")));
            }

            JsonElement activationItemElement =
                    JSONUtils.isArrayNode(json, "activation_item") ? JSONUtils.getAsJsonArray(json,
                            "activation_item") : JSONUtils.getAsJsonObject(json, "activation_item");
            Ingredient activationItem = Ingredient.fromJson(activationItemElement);

            int duration = JSONUtils.getAsInt(json, "duration", 30);
            int summonLife = JSONUtils.getAsInt(json, "summonLife", -1);
            int soulCost = JSONUtils.getAsInt(json, "soulCost", 0);

            ITag<EntityType<?>> entityToSacrifice = null;
            String entityToSacrificeDisplayName = "";
            if (json.has("entity_to_sacrifice")) {
                entityToSacrifice = TagCollectionManager.getInstance().getEntityTypes()
                        .getTagOrEmpty(new ResourceLocation(JSONUtils.getAsString(json.getAsJsonObject("entity_to_sacrifice"), "tag")));
                entityToSacrificeDisplayName = json.getAsJsonObject("entity_to_sacrifice").get("display_name").getAsString();
            }

            EntityType<?> entityToConvertInto = null;
            ITag<EntityType<?>> entityToConvert = null;
            String entityToConvertDisplayName = "";

            if (json.has("entity_to_convert")){
                entityToConvert = TagCollectionManager.getInstance().getEntityTypes()
                        .getTagOrEmpty(new ResourceLocation(JSONUtils.getAsString(json.getAsJsonObject("entity_to_convert"), "tag")));

                entityToConvertDisplayName = json.getAsJsonObject("entity_to_convert").get("display_name").getAsString();
            }
            if (json.has("entity_to_convert_into")) {
                entityToConvertInto = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(json, "entity_to_convert_into")));
            }

            return new RitualRecipe(recipeId, group, craftType, ritualType,
                    result, entityToSummon, entityToConvertInto, activationItem, ingredients, duration,
                    summonLife, soulCost, entityToSacrifice, entityToSacrificeDisplayName, entityToConvert, entityToConvertDisplayName);
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

        @Override
        public RitualRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            ModShapelessRecipe recipe = serializer.fromNetwork(recipeId, buffer);
            String craftType = buffer.readUtf(32767);

            ResourceLocation ritualType = buffer.readResourceLocation();

            EntityType<?> entityToSummon = null;
            if (buffer.readBoolean()) {
                entityToSummon = buffer.readRegistryId();
            }

            int duration = buffer.readVarInt();
            int summonLife = buffer.readVarInt();
            int soulCost = buffer.readVarInt();

            Ingredient activationItem = Ingredient.fromNetwork(buffer);

            ITag<EntityType<?>> entityToSacrifice = null;
            String entityToSacrificeDisplayName = "";
            if (buffer.readBoolean()) {
                entityToSacrifice = TagCollectionManager.getInstance().getEntityTypes().getTagOrEmpty(buffer.readResourceLocation());
                entityToSacrificeDisplayName = buffer.readUtf();
            }

            EntityType<?> entityToConvertInto = null;
            ITag<EntityType<?>> entityToConvert = null;
            String entityToConvertDisplayName = "";
            if (buffer.readBoolean()) {
                entityToConvert = entityToSacrifice = TagCollectionManager.getInstance().getEntityTypes().getTagOrEmpty(buffer.readResourceLocation());
                entityToConvertDisplayName = buffer.readUtf();
            }

            if (buffer.readBoolean()){
                entityToConvertInto = buffer.readRegistryId();
            }

            assert recipe != null;
            return new RitualRecipe(recipeId, recipe.getGroup(), craftType, ritualType,
                    recipe.getResultItem(), entityToSummon, entityToConvertInto, activationItem, recipe.getIngredients(), duration,
                    summonLife, soulCost, entityToSacrifice, entityToSacrificeDisplayName, entityToConvert, entityToConvertDisplayName);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, RitualRecipe recipe) {
            serializer.toNetwork(buffer, recipe);
            buffer.writeUtf(recipe.craftType);

            buffer.writeResourceLocation(recipe.ritualType);

            buffer.writeBoolean(recipe.entityToSummon != null);
            if (recipe.entityToSummon != null)
                buffer.writeRegistryId(recipe.entityToSummon);

            buffer.writeVarInt(recipe.duration);
            buffer.writeVarInt(recipe.summonLife);
            buffer.writeVarInt(recipe.soulCost);
            recipe.activationItem.toNetwork(buffer);
            buffer.writeBoolean(recipe.entityToSacrifice != null);
            if (recipe.entityToSacrifice != null) {
                buffer.writeResourceLocation(TagCollectionManager.getInstance().getEntityTypes().getId(recipe.entityToSacrifice));
                buffer.writeUtf(recipe.entityToSacrificeDisplayName);
            }
            buffer.writeBoolean(recipe.entityToConvert != null);
            if (recipe.entityToConvert != null){
                buffer.writeResourceLocation(TagCollectionManager.getInstance().getEntityTypes().getId(recipe.entityToConvert));
                buffer.writeUtf(recipe.entityToConvertDisplayName);
            }
            buffer.writeBoolean(recipe.entityToConvertInto != null);
            if (recipe.entityToConvertInto != null) {
                buffer.writeRegistryId(recipe.entityToConvertInto);
            }
        }
    }
}
/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */