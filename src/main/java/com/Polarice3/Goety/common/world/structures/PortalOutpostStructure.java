package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.world.structures.pieces.PortalOutpostPiece;
import com.Polarice3.Goety.init.ModEntityType;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.List;
import java.util.Random;

public class PortalOutpostStructure extends Structure<NoFeatureConfig> {
    private static final List<MobSpawnInfo.Spawners> ENEMIES = ImmutableList.of(
            new MobSpawnInfo.Spawners(ModEntityType.FANATIC.get(), 20, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.ZEALOT.get(), 20, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.DISCIPLE.get(), 1, 1, 1),
            new MobSpawnInfo.Spawners(EntityType.WITCH, 1, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.BELDAM.get(), 1, 1, 1));

    public PortalOutpostStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return ENEMIES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkrandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        BlockState bottomBlock = columnOfBlocks.getBlockState(centerOfChunk.below(landHeight));
        return MainConfig.PortalOutpostGen.get() && topBlock.getFluidState().isEmpty() && bottomBlock.getFluidState().isEmpty() && isNearPortal(chunkGenerator, seed, chunkrandom, chunkX, chunkZ);
    }

    private boolean isNearPortal(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(Structure.RUINED_PORTAL);
        if (structureseparationsettings != null) {
            for (int i = p_242782_5_ - 3; i <= p_242782_5_ + 3; ++i) {
                for (int j = p_242782_6_ - 3; j <= p_242782_6_ + 3; ++j) {
                    ChunkPos chunkpos = Structure.RUINED_PORTAL.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
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
                PortalOutpostPiece structure = new PortalOutpostPiece(templateManagerIn, blockpos, rotation);
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
