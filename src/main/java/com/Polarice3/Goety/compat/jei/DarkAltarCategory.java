package com.Polarice3.Goety.compat.jei;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.crafting.CursedBurnerRecipes;
import com.Polarice3.Goety.client.inventory.crafting.DarkAltarRecipes;
import com.Polarice3.Goety.init.ModRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class DarkAltarCategory implements IRecipeCategory<DarkAltarRecipes> {
    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;
    private final ITextComponent localizedName;
    private final int regularCookTime;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    protected static final int inputSlot = 0;
    protected static final int outputSlot = 2;
    public static final ResourceLocation UID = new ResourceLocation(Goety.MOD_ID, "dark_altar");

    public DarkAltarCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModRegistry.DARK_ALTAR.get()));
        background = guiHelper.drawableBuilder(new ResourceLocation(Goety.MOD_ID, "textures/gui/jei/jei_gui.png"), 0, 220, 82, 34)
                .addPadding(0, 10, 0, 0)
                .build();
        this.regularCookTime = 400;
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(new ResourceLocation(Goety.MOD_ID, "textures/gui/jei/jei_gui.png"), 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        localizedName = new TranslationTextComponent("gui.jei.category.dark_altar");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends DarkAltarRecipes> getRecipeClass() {
        return DarkAltarRecipes.class;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(DarkAltarRecipes recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(ModRegistry.EMPTYCORE.get()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    @Deprecated
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return localizedName;
    }

    @Override
    public void draw(DarkAltarRecipes recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(matrixStack, 24, 8);
        drawCookTime(recipe, matrixStack, 35);
        drawSoulCost(recipe, matrixStack, 35);
    }

    protected IDrawableAnimated getArrow(DarkAltarRecipes recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    protected void drawCookTime(DarkAltarRecipes recipe, MatrixStack matrixStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.dark_altar.time", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawSoulCost(DarkAltarRecipes recipe, MatrixStack matrixStack, int y) {
        int soulCost = recipe.getSoulCost();
        if (soulCost > 0) {
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.dark_altar.soulcost", soulCost);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth - 20, y, 0xFF808080);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DarkAltarRecipes recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 8);
        guiItemStacks.init(outputSlot, false, 60, 8);

        guiItemStacks.set(ingredients);
    }
}
