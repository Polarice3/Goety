package com.Polarice3.Goety.data;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.ModelUtils;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider {
    private final Consumer<IFinishedBlockState> blockStateOutput;
    private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
    private final Consumer<Item> skippedAutoModelsOutput;

    public ModBlockModelProvider(Consumer<IFinishedBlockState> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        this.blockStateOutput = pBlockStateOutput;
        this.modelOutput = pModelOutput;
        this.skippedAutoModelsOutput = pSkippedAutoModelsOutput;
    }

    private ModBlockModelProvider.BreakParticleHelper blockEntityModels(ResourceLocation pEntityBlockModelLocation, Block pParticleBlock) {
        return new ModBlockModelProvider.BreakParticleHelper(pEntityBlockModelLocation, pParticleBlock);
    }

    private static FinishedVariantBlockState createSimpleBlock(Block pBlock, ResourceLocation pModelLocation) {
        return FinishedVariantBlockState.multiVariant(pBlock, BlockModelDefinition.variant().with(BlockModelFields.MODEL, pModelLocation));
    }

    private void skipAutoItemBlock(Block pBlock) {
        this.skippedAutoModelsOutput.accept(pBlock.asItem());
    }

    public void run(){
        this.blockEntityModels(ModelUtils.decorateBlockModelLocation("tall_skull"), Blocks.SOUL_SAND).createWithCustomBlockItemModel(StockModelShapes.SKULL_INVENTORY, ModBlocks.TALL_SKULL_BLOCK.get()).createWithoutBlockItem(ModBlocks.WALL_TALL_SKULL_BLOCK.get());
    }

    class BreakParticleHelper {
        private final ResourceLocation baseModel;

        public BreakParticleHelper(ResourceLocation pBaseModel, Block pParticleBlock) {
            this.baseModel = StockModelShapes.PARTICLE_ONLY.create(pBaseModel, ModelTextures.particle(pParticleBlock), ModBlockModelProvider.this.modelOutput);
        }

        public ModBlockModelProvider.BreakParticleHelper create(Block... pBlocks) {
            for(Block block : pBlocks) {
                ModBlockModelProvider.this.blockStateOutput.accept(ModBlockModelProvider.createSimpleBlock(block, this.baseModel));
            }

            return this;
        }

        public ModBlockModelProvider.BreakParticleHelper createWithoutBlockItem(Block... pBlocks) {
            for(Block block : pBlocks) {
                ModBlockModelProvider.this.skipAutoItemBlock(block);
            }

            return this.create(pBlocks);
        }

        public ModBlockModelProvider.BreakParticleHelper createWithCustomBlockItemModel(ModelsUtil pModelTemplate, Block... pBlocks) {
            for(Block block : pBlocks) {
                pModelTemplate.create(ModelsResourceUtil.getModelLocation(block.asItem()), ModelTextures.particle(block), ModBlockModelProvider.this.modelOutput);
            }

            return this.create(pBlocks);
        }
    }
}
