package com.Polarice3.Goety.common.world.structures.pieces;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

/**
 * This class is based on a part of the Astral Sorcery Mod
 * The source code that is based for this mod can be found on Astral Sorcery github.
 * Code based made by HellFirePvP
 */
public abstract class ModStructurePiece extends TemplateStructurePiece {
    private final Rotation rotation;
    private int yOffset = 0;

    public ModStructurePiece(IStructurePieceType structurePieceTypeIn, TemplateManager templateManager, BlockPos templatePosition, Rotation pRotation) {
        super(structurePieceTypeIn, 0);
        this.templatePosition = templatePosition;
        this.rotation = pRotation;
        this.loadTemplate(templateManager);
    }

    public ModStructurePiece(IStructurePieceType structurePieceTypeIn, TemplateManager templateManager, CompoundNBT nbt) {
        super(structurePieceTypeIn, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.loadTemplate(templateManager);
    }

    private void loadTemplate(TemplateManager templateManager) {
        Template tpl = templateManager.getOrCreate(this.getStructureName());
        PlacementSettings settings = new PlacementSettings()
                .setIgnoreEntities(true)
                .setRotation(this.rotation)
                .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(tpl, this.templatePosition, settings);
    }

    protected void addAdditionalSaveData(CompoundNBT p_143011_1_) {
        super.addAdditionalSaveData(p_143011_1_);
        p_143011_1_.putString("Rot", this.rotation.name());
    }

    public <T extends ModStructurePiece> T setYOffset(int yOffset) {
        this.yOffset = yOffset;
        return (T) this;
    }

    public abstract ResourceLocation getStructureName();

    @Override
    public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos chunkPos, BlockPos pPos) {
        MutableBoundingBox mutableBoundingBox = new MutableBoundingBox(pBox);
        mutableBoundingBox.move(0, this.yOffset, 0);

        BlockPos original = this.templatePosition;
        this.templatePosition = original.above(this.yOffset);
        try {
            return super.postProcess(pLevel, pStructureManager, pChunkGenerator, pRandom, mutableBoundingBox, chunkPos, pPos.above(yOffset));
        } finally {
            this.templatePosition = original;
            this.placeSettings.setBoundingBox(pBox);
            this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
        }
    }

}
