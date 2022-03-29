package com.Polarice3.Goety.compat.jei;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.init.ModRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModRitualCategory implements IRecipeCategory<RitualRecipe> {
    private final IDrawable background;
    private final IDrawable arrow;
    private final String localizedName;
    private final ItemStack darkAltar = new ItemStack(ModRegistry.DARK_ALTAR.get());
    private final ItemStack pedestals = new ItemStack(ModRegistry.PEDESTAL_DUMMY.get());
    private final ItemStack requireSacrifice = new ItemStack(ModRegistry.JEI_DUMMY_REQUIRE_SACRIFICE.get());
    private final int iconWidth = 16;
    private final int ritualCenterX;
    private final int ritualCenterY;
    private int recipeOutputOffsetX = 50;

    public ModRitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(168, 120);
        this.ritualCenterX = this.background.getWidth() / 2 - this.iconWidth / 2 - 30;
        this.ritualCenterY = this.background.getHeight() / 2 - this.iconWidth / 2 + 10;
        this.localizedName = I18n.get(Goety.MOD_ID + ".jei.ritual");
        this.darkAltar.getOrCreateTag().putBoolean("RenderFull", true);
        this.pedestals.getOrCreateTag().putBoolean("RenderFull", true);
        this.arrow = guiHelper.createDrawable(
                new ResourceLocation(Goety.MOD_ID, "textures/gui/jei/arrow.png"), 0, 0, 64, 46);
    }

    @Override
    public ResourceLocation getUid() {
        return ModRecipeSerializer.RITUAL.getId();
    }

    @Override
    public Class<? extends RitualRecipe> getRecipeClass() {
        return RitualRecipe.class;
    }

    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(RitualRecipe recipe, IIngredients ingredients) {
        Stream<Ingredient> ingredientStream = Stream.concat(
                Stream.of(recipe.getActivationItem()),
                recipe.getIngredients().stream()
        );
        if (recipe.requiresItemUse()) {
            ingredientStream = Stream.concat(Stream.of(recipe.getItemToUse()), ingredientStream);
        }
        ingredients.setInputIngredients(
                ingredientStream.collect(Collectors.toList())
        );

        ingredients.setOutputs(VanillaTypes.ITEM, Stream.of(recipe.getResultItem()).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RitualRecipe recipe, IIngredients ingredients) {
        int index = 0;
        this.recipeOutputOffsetX = 75;

        int currentIngredient = 0;
        List<ItemStack> itemToUse = new ArrayList<>();
        if (recipe.requiresItemUse()) {
            itemToUse = ingredients.getInputs(VanillaTypes.ITEM).get(currentIngredient++);
        }
        List<ItemStack> activationItem = ingredients.getInputs(VanillaTypes.ITEM).get(currentIngredient++);
        List<List<ItemStack>> inputItems =
                ingredients.getInputs(VanillaTypes.ITEM).stream().skip(currentIngredient).collect(Collectors.toList());

        recipeLayout.getItemStacks().init(index, true, this.ritualCenterX, this.ritualCenterY - 15);
        recipeLayout.getItemStacks().set(index, activationItem);
        index++;

        recipeLayout.getItemStacks().init(index, false, this.ritualCenterX, this.ritualCenterY);
        recipeLayout.getItemStacks().set(index, this.darkAltar);
        index++;

        int sacrificialCircleRadius = 30;
        int pedestalsPaddingVertical = 20;
        int pedestalsPaddingHorizontal = 15;
        List<Vector3i> pedestalsPosition = Stream.of(
                new Vector3i(this.ritualCenterX, this.ritualCenterY - sacrificialCircleRadius, 0),
                new Vector3i(this.ritualCenterX + sacrificialCircleRadius, this.ritualCenterY, 0),
                new Vector3i(this.ritualCenterX, this.ritualCenterY + sacrificialCircleRadius, 0),
                new Vector3i(this.ritualCenterX - sacrificialCircleRadius, this.ritualCenterY, 0),

                new Vector3i(this.ritualCenterX + pedestalsPaddingHorizontal,
                        this.ritualCenterY - sacrificialCircleRadius,
                        0),
                new Vector3i(this.ritualCenterX + sacrificialCircleRadius,
                        this.ritualCenterY - pedestalsPaddingVertical, 0),
                new Vector3i(this.ritualCenterX - pedestalsPaddingHorizontal,
                        this.ritualCenterY + sacrificialCircleRadius,
                        0),
                new Vector3i(this.ritualCenterX - sacrificialCircleRadius,
                        this.ritualCenterY + pedestalsPaddingVertical, 0),

                new Vector3i(this.ritualCenterX - pedestalsPaddingHorizontal,
                        this.ritualCenterY - sacrificialCircleRadius,
                        0),
                new Vector3i(this.ritualCenterX + sacrificialCircleRadius,
                        this.ritualCenterY + pedestalsPaddingVertical, 0),
                new Vector3i(this.ritualCenterX + pedestalsPaddingHorizontal,
                        this.ritualCenterY + sacrificialCircleRadius,
                        0),
                new Vector3i(this.ritualCenterX - sacrificialCircleRadius,
                        this.ritualCenterY - pedestalsPaddingVertical, 0)
        ).collect(Collectors.toList());

        for (int i = 0; i < inputItems.size(); i++) {
            Vector3i pos = pedestalsPosition.get(i);

            recipeLayout.getItemStacks().init(index, true, pos.getX(), pos.getY() - 5);
            recipeLayout.getItemStacks().set(index, inputItems.get(i));
            index++;

            recipeLayout.getItemStacks().init(index, false, pos.getX(), pos.getY());
            recipeLayout.getItemStacks().set(index, this.pedestals);
            index++;

        }

        if (recipe.getResultItem().getItem() != ModRegistry.JEI_DUMMY_NONE.get()) {
            recipeLayout.getItemStacks()
                    .init(index, false, this.ritualCenterX + this.recipeOutputOffsetX, this.ritualCenterY - 15);
            recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
            index++;
        } else {
            recipeLayout.getItemStacks()
                    .init(index, false, this.ritualCenterX + this.recipeOutputOffsetX, this.ritualCenterY - 15);
            recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
            index++;
        }
        recipeLayout.getItemStacks()
                .init(index, false, this.ritualCenterX + this.recipeOutputOffsetX, this.ritualCenterY);
        recipeLayout.getItemStacks().set(index, this.darkAltar);
        index++;

        if (recipe.getCraftType().contains("animalis")){
            recipeLayout.getItemStacks().init(index, false, 0, 0);
            recipeLayout.getItemStacks().set(index, new ItemStack(ModRegistry.ANIMALISCORE.get()));
            index++;
        } else if (recipe.getCraftType().contains("necroturgy")){
            recipeLayout.getItemStacks().init(index, false, 0, 0);
            recipeLayout.getItemStacks().set(index, new ItemStack(Items.ZOMBIE_HEAD));
            index++;
        } else if (recipe.getCraftType().contains("forge")){
            recipeLayout.getItemStacks().init(index, false, 0, 0);
            recipeLayout.getItemStacks().set(index, new ItemStack(Items.ANVIL));
            index++;
        } else {
            recipeLayout.getItemStacks().init(index, false, 0, 0);
            recipeLayout.getItemStacks().set(index, new ItemStack(Items.OBSIDIAN));
            index++;
        }

        if (recipe.requiresItemUse()) {
            int infotextY = 0;
            if (recipe.requiresSacrifice()) {
                infotextY += 10;
            }

            infotextY += 10;
            String text = I18n.get("jei.goety.item_to_use");
            int itemToUseY = infotextY - 5;
            int itemToUseX = this.getStringCenteredMaxX(Minecraft.getInstance().font, text, 84, infotextY);

            recipeLayout.getItemStacks().init(index, false, itemToUseX, itemToUseY);
            recipeLayout.getItemStacks().set(index, itemToUse);
            index++;
        }
    }

    @Override
    public void draw(RitualRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        this.arrow.draw(matrixStack, this.ritualCenterX + this.recipeOutputOffsetX - 20, this.ritualCenterY);
        RenderSystem.disableBlend();

        int infotextY = 0;
        if (recipe.requiresSacrifice()) {
            infotextY += 10;
            this.drawStringCentered(matrixStack, Minecraft.getInstance().font,
                    I18n.get("jei.goety.sacrifice", I18n.get(recipe.getEntityToSacrificeDisplayName())), 84, infotextY);
        }

        if (recipe.requiresItemUse()) {
            infotextY += 10;
            String text = I18n.get("jei.goety.item_to_use");
            this.drawStringCentered(matrixStack, Minecraft.getInstance().font, text, 84, infotextY);
        }

        if (recipe.getEntityToSummon() != null) {
            infotextY += 10;
            this.drawStringCentered(matrixStack, Minecraft.getInstance().font,
                    I18n.get("jei.goety.summon", I18n.get(recipe.getEntityToSummon().getDescriptionId())),
                    84, infotextY);
        }

        if (recipe.getCraftType() != null) {
            infotextY += 5;
            this.drawStringCentered(matrixStack, Minecraft.getInstance().font,
                    I18n.get("jei.goety.craftType." + I18n.get(recipe.getCraftType())),
                    84, infotextY);
        }

    }

    protected int getStringCenteredMaxX(FontRenderer fontRenderer, String text, int x, int y) {
        int width = fontRenderer.width(text);
        int actualX = (int) (x - width / 2.0f);
        return actualX + width;
    }

    protected void drawStringCentered(MatrixStack matrixStack, FontRenderer fontRenderer, String text, int x, int y) {
        fontRenderer.draw(matrixStack, text, (x - fontRenderer.width(text) / 2.0f), y, 0);
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