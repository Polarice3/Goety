package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.world.structures.pieces.WarpedShrinePiece;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.List;

public class WarpedShrineStructure extends ModStructureTemplate{
    private static final List<MobSpawnInfo.Spawners> ENEMIES = ImmutableList.of(
            new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 20, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.FANATIC.get(), 5, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.ZEALOT.get(), 5, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.DISCIPLE.get(), 1, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityType.BELDAM.get(), 1, 1, 1));

    protected boolean linearSeparation() {
        return false;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return ENEMIES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkrandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        int x = chunkX * 16, z = chunkZ * 16;
        return MainConfig.WarpedShrineGen.get()
                && !BlockFinder.isLavaLake(chunkGenerator, x, z)
                && !isNearBastion(chunkGenerator, seed, chunkrandom, chunkX, chunkZ)
                && !isNearFortress(chunkGenerator, seed, chunkrandom, chunkX, chunkZ)
                && BlockFinder.verticalSpace(chunkGenerator, x, z, 32, 64, 16);
    }

    private boolean isNearBastion(ChunkGenerator chunkGenerator, long pSeed, SharedSeedRandom pRandom, int chunkX, int chunkZ) {
        StructureSeparationSettings structureseparationsettings = chunkGenerator.getSettings().getConfig(Structure.BASTION_REMNANT);
        if (structureseparationsettings != null) {
            for (int i = chunkX - 5; i <= chunkX + 5; ++i) {
                for (int j = chunkZ - 5; j <= chunkZ + 5; ++j) {
                    ChunkPos chunkpos = Structure.BASTION_REMNANT.getPotentialFeatureChunk(structureseparationsettings, pSeed, pRandom, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean isNearFortress(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(Structure.NETHER_BRIDGE);
        if (structureseparationsettings != null) {
            for (int i = p_242782_5_ - 5; i <= p_242782_5_ + 5; ++i) {
                for (int j = p_242782_6_ - 5; j <= p_242782_6_ + 5; ++j) {
                    ChunkPos chunkpos = Structure.NETHER_BRIDGE.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return WarpedShrineStructure.Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> config, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int ref, long seed) {
            super(config, chunkPosX, chunkPosZ, bounds, ref, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.getRandom(this.random);
            BlockPos blockpos = new BlockPos(chunkX * 16, BlockFinder.getHighestLand(chunkGenerator, boundingBox, false).getY(), chunkZ * 16);
            WarpedShrinePiece structure = new WarpedShrinePiece(templateManagerIn, blockpos, rotation);
            this.pieces.add(structure);
            this.calculateBoundingBox();
        }
    }
}
