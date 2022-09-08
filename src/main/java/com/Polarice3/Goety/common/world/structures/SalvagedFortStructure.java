package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.world.structures.pieces.SalvagedFortPiece;
import com.Polarice3.Goety.init.ModStructures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.Random;

public class SalvagedFortStructure extends ModStructureTemplate {

    protected boolean linearSeparation() {
        return false;
    }

    @Override
    public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkrandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        for(Biome biome1 : biomeSource.getBiomesWithin(chunkX * 16 + 9, chunkGenerator.getSeaLevel(), chunkZ * 16 + 9, 32)) {
            if (!biome1.getGenerationSettings().isValidStart(this)) {
                return false;
            }
        }
        BlockState bottomBlock = columnOfBlocks.getBlockState(centerOfChunk.below(landHeight));
        return MainConfig.SalvagedFortGen.get() && topBlock.getFluidState().isEmpty() && bottomBlock.getFluidState().isEmpty() && !isNearVillage(chunkGenerator, seed, chunkrandom, chunkX, chunkZ) && !isNearFort(chunkGenerator, seed, chunkrandom, chunkX, chunkZ);
    }

    private boolean isNearVillage(ChunkGenerator chunkGenerator, long l, SharedSeedRandom sharedSeedRandom, int i1, int i2) {
        StructureSeparationSettings structureseparationsettings = chunkGenerator.getSettings().getConfig(Structure.VILLAGE);
        if (structureseparationsettings != null) {
            for (int i = i1 - 10; i <= i1 + 10; ++i) {
                for (int j = i2 - 10; j <= i2 + 10; ++j) {
                    ChunkPos chunkpos = Structure.VILLAGE.getPotentialFeatureChunk(structureseparationsettings, l, sharedSeedRandom, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean isNearFort(ChunkGenerator chunkGenerator, long l, SharedSeedRandom sharedSeedRandom, int i1, int i2) {
        StructureSeparationSettings structureseparationsettings = chunkGenerator.getSettings().getConfig(ModStructures.DECREPIT_FORT.get());
        if (structureseparationsettings != null) {
            for (int i = i1 - 10; i <= i1 + 10; ++i) {
                for (int j = i2 - 10; j <= i2 + 10; ++j) {
                    ChunkPos chunkpos = ModStructures.DECREPIT_FORT.get().getPotentialFeatureChunk(structureseparationsettings, l, sharedSeedRandom, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> config, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int ref, long seed) {
            super(config, chunkPosX, chunkPosZ, bounds, ref, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = chunkGenerator.getBaseHeight(x, z, Heightmap.Type.MOTION_BLOCKING);
            Rotation rotation = Rotation.getRandom(this.random);
            int i = 5;
            int j = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                i = -5;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                i = -5;
                j = -5;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                j = -5;
            }

            int k = (chunkX << 4) + 7;
            int l = (chunkZ << 4) + 7;
            int i1 = chunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Type.WORLD_SURFACE_WG);
            int j1 = chunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Type.WORLD_SURFACE_WG);
            int k1 = chunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Type.WORLD_SURFACE_WG);
            int l1 = chunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Type.WORLD_SURFACE_WG);
            int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
            if (i2 >= 60) {
                BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i2 + 1, chunkZ * 16 + 8);
                SalvagedFortPiece structure = new SalvagedFortPiece(templateManagerIn, blockpos, rotation);
                this.pieces.add(structure);
                this.calculateBoundingBox();
            }
        }

        public void placeInChunk(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos) {
            super.placeInChunk(pLevel, pStructureManager, pChunkGenerator, pRandom, pBox, pChunkPos);
            int i = this.boundingBox.y0;

            for(int j = pBox.x0; j <= pBox.x1; ++j) {
                for(int k = pBox.z0; k <= pBox.z1; ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!pLevel.isEmptyBlock(blockpos) && this.boundingBox.isInside(blockpos)) {
                        boolean flag = false;

                        for(StructurePiece structurepiece : this.pieces) {
                            if (structurepiece.getBoundingBox().isInside(blockpos)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for(int l = i - 1; l > 1; --l) {
                                BlockPos blockpos1 = new BlockPos(j, l, k);
                                if (!pLevel.isEmptyBlock(blockpos1) && !pLevel.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                    break;
                                }

                                pLevel.setBlock(blockpos1, Blocks.DIRT.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

        }
    }
}
