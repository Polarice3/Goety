package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.world.features.RuinedRitualFeature;
import com.Polarice3.Goety.common.world.structures.pieces.RuinedRitualPiece;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class RuinedRitualStructure extends Structure<RuinedRitualFeature>{

    public RuinedRitualStructure(Codec<RuinedRitualFeature> p_i231984_1_) {
        super(p_i231984_1_);
    }

    public Structure.IStartFactory<RuinedRitualFeature> getStartFactory() {
        return RuinedRitualStructure.Start::new;
    }

    @Override
    public String getFeatureName() {
        return this.getRegistryName().toString();
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkrandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, RuinedRitualFeature featureConfig) {
        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        BlockState bottomBlock = columnOfBlocks.getBlockState(centerOfChunk.below(landHeight));
        return MainConfig.RuinedRitualGen.get() && topBlock.getFluidState().isEmpty() && bottomBlock.getFluidState().isEmpty() && !isNearVillage(chunkGenerator, seed, chunkrandom, chunkX, chunkZ);
    }

    private boolean isNearVillage(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(Structure.VILLAGE);
        if (structureseparationsettings != null) {
            for (int i = p_242782_5_ - 10; i <= p_242782_5_ + 10; ++i) {
                for (int j = p_242782_6_ - 10; j <= p_242782_6_ + 10; ++j) {
                    ChunkPos chunkpos = Structure.VILLAGE.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private static boolean isCold(BlockPos pPos, Biome pBiome) {
        return pBiome.getTemperature(pPos) < 0.15F;
    }

    private static int findSuitableY(ChunkGenerator p_236339_1_, int p_236339_4_, MutableBoundingBox p_236339_6_) {
        List<BlockPos> list1 = ImmutableList.of(new BlockPos(p_236339_6_.x0, 0, p_236339_6_.z0), new BlockPos(p_236339_6_.x1, 0, p_236339_6_.z0), new BlockPos(p_236339_6_.x0, 0, p_236339_6_.z1), new BlockPos(p_236339_6_.x1, 0, p_236339_6_.z1));
        List<IBlockReader> list = list1.stream().map((p_236333_1_) -> {
            return p_236339_1_.getBaseColumn(p_236333_1_.getX(), p_236333_1_.getZ());
        }).collect(Collectors.toList());
        Heightmap.Type heightmap$type = Heightmap.Type.WORLD_SURFACE_WG;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        int k;
        for(k = p_236339_4_; k > 15; --k) {
            int l = 0;
            blockpos$mutable.set(0, k, 0);

            for(IBlockReader iblockreader : list) {
                BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
                if (heightmap$type.isOpaque().test(blockstate)) {
                    ++l;
                    if (l == 3) {
                        return k;
                    }
                }
            }
        }

        return k;
    }

    private static int randomIntInclusive(Random p_236335_0_, int p_236335_1_, int p_236335_2_) {
        return p_236335_0_.nextInt(p_236335_2_ - p_236335_1_ + 1) + p_236335_1_;
    }

    private static int getRandomWithinInterval(Random pRandom, int pMin, int pMax) {
        return pMin < pMax ? randomIntInclusive(pRandom, pMin, pMax) : pMax;
    }

    public static enum Location implements IStringSerializable {
        STANDARD("standard"),
        JUNGLE("jungle");

        public static final Codec<RuinedRitualStructure.Location> CODEC = IStringSerializable.fromEnum(RuinedRitualStructure.Location::values, RuinedRitualStructure.Location::byName);
        private static final Map<String, RuinedRitualStructure.Location> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(RuinedRitualStructure.Location::getName, (p_236345_0_) -> {
            return p_236345_0_;
        }));
        private final String name;

        private Location(String pName) {
            this.name = pName;
        }

        public String getName() {
            return this.name;
        }

        public static RuinedRitualStructure.Location byName(String p_236346_0_) {
            return BY_NAME.get(p_236346_0_);
        }

        public String getSerializedName() {
            return this.name;
        }
    }

    public static class Start extends StructureStart<RuinedRitualFeature> {
        protected Start(Structure<RuinedRitualFeature> p_i231985_1_, int p_i231985_2_, int p_i231985_3_, MutableBoundingBox p_i231985_4_, int p_i231985_5_, long p_i231985_6_) {
            super(p_i231985_1_, p_i231985_2_, p_i231985_3_, p_i231985_4_, p_i231985_5_, p_i231985_6_);
        }

        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biome, RuinedRitualFeature config) {
            RuinedRitualPiece.Serializer RuinedRitualPiece$serializer = new RuinedRitualPiece.Serializer();
            if (config.locationType == Location.JUNGLE) {
                RuinedRitualPiece$serializer.mossiness = 0.8F;
                RuinedRitualPiece$serializer.overgrown = true;
                RuinedRitualPiece$serializer.vines = true;
            }

            Template template = templateManagerIn.getOrCreate(ConstantPaths.getRuinedRitual());
            Rotation rotation = Util.getRandom(Rotation.values(), this.random);
            Mirror mirror = this.random.nextFloat() < 0.5F ? Mirror.NONE : Mirror.FRONT_BACK;
            BlockPos blockpos = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
            BlockPos blockpos1 = (new ChunkPos(chunkX, chunkZ)).getWorldPosition();
            MutableBoundingBox mutableboundingbox = template.getBoundingBox(blockpos1, rotation, blockpos, mirror);
            Vector3i vector3i = mutableboundingbox.getCenter();
            int i = vector3i.getX();
            int j = vector3i.getZ();
            int k = chunkGenerator.getBaseHeight(i, j, RuinedRitualPiece.getHeightMapType()) - 1;
            int l = RuinedRitualStructure.findSuitableY(chunkGenerator, k, mutableboundingbox);
            BlockPos blockpos2 = new BlockPos(blockpos1.getX(), l, blockpos1.getZ());
            if (config.locationType == Location.STANDARD) {
                RuinedRitualPiece$serializer.cold = RuinedRitualStructure.isCold(blockpos2, biome);
            }

            this.pieces.add(new RuinedRitualPiece(blockpos2, RuinedRitualPiece$serializer, ConstantPaths.getRuinedRitual(), template, rotation, mirror, blockpos));
            this.calculateBoundingBox();
        }
    }
}
