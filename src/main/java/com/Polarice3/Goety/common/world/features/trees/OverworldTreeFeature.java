package com.Polarice3.Goety.common.world.features.trees;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import java.util.*;

public class OverworldTreeFeature extends Feature<BaseTreeFeatureConfig> {

    public OverworldTreeFeature(Codec<BaseTreeFeatureConfig> p_i231999_1_) {
        super(p_i231999_1_);
    }

    public static boolean isFree(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, (p_236417_0_) -> {
            return p_236417_0_.is(BlockTags.LOGS);
        });
    }

    private static boolean isVine(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_236415_0_) -> {
            return p_236415_0_.is(Blocks.VINE);
        });
    }

    private static boolean isBlockWater(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_236413_0_) -> {
            return p_236413_0_.is(Blocks.WATER);
        });
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_236411_0_) -> {
            return p_236411_0_.isAir() || p_236411_0_.is(BlockTags.LEAVES);
        });
    }

    private static boolean isGrassOrDirtOrFarmland(IWorldGenerationBaseReader p_236418_0_, BlockPos p_236418_1_) {
        return p_236418_0_.isStateAtPosition(p_236418_1_, (p_236409_0_) -> {
            Block block = p_236409_0_.getBlock();
            return isDirt(block) || block == Blocks.FARMLAND;
        });
    }

    private static boolean isReplaceablePlant(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_236406_0_) -> {
            Material material = p_236406_0_.getMaterial();
            return material == Material.REPLACEABLE_PLANT;
        });
    }

    public static void setBlockKnownShape(IWorldWriter pLevel, BlockPos pPos, BlockState pState) {
        pLevel.setBlock(pPos, pState, 19);
    }

    public static boolean validTreePos(IWorldGenerationBaseReader pLevel, BlockPos pPos) {
        return isAirOrLeaves(pLevel, pPos) || isReplaceablePlant(pLevel, pPos) || isBlockWater(pLevel, pPos);
    }

    private boolean doPlace(IWorldGenerationReader p_225557_1_, Random p_225557_2_, BlockPos p_225557_3_, Set<BlockPos> p_225557_4_, Set<BlockPos> p_225557_5_, MutableBoundingBox p_225557_6_, BaseTreeFeatureConfig p_225557_7_) {
        int i = p_225557_7_.trunkPlacer.getTreeHeight(p_225557_2_);
        int j = p_225557_7_.foliagePlacer.foliageHeight(p_225557_2_, i, p_225557_7_);
        int k = i - j;
        int l = p_225557_7_.foliagePlacer.foliageRadius(p_225557_2_, k);
        BlockPos blockpos;
        if (!p_225557_7_.fromSapling) {
            int i1 = p_225557_1_.getHeightmapPos(Heightmap.Type.OCEAN_FLOOR, p_225557_3_).getY();
            int j1 = p_225557_1_.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, p_225557_3_).getY();
            if (j1 - i1 > p_225557_7_.maxWaterDepth) {
                return false;
            }

            int k1;
            if (p_225557_7_.heightmap == Heightmap.Type.OCEAN_FLOOR) {
                k1 = i1;
            } else if (p_225557_7_.heightmap == Heightmap.Type.WORLD_SURFACE) {
                k1 = j1;
            } else {
                k1 = p_225557_1_.getHeightmapPos(p_225557_7_.heightmap, p_225557_3_).getY();
            }

            blockpos = new BlockPos(p_225557_3_.getX(), k1, p_225557_3_.getZ());
        } else {
            blockpos = p_225557_3_;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
            if (!isGrassOrDirtOrFarmland(p_225557_1_, blockpos.below())) {
                return false;
            } else {
                OptionalInt optionalint = p_225557_7_.minimumSize.minClippedHeight();
                int l1 = this.getMaxFreeTreeHeight(p_225557_1_, i, blockpos, p_225557_7_);
                if (l1 >= i || optionalint.isPresent() && l1 >= optionalint.getAsInt()) {
                    List<FoliagePlacer.Foliage> list = p_225557_7_.trunkPlacer.placeTrunk(p_225557_1_, p_225557_2_, l1, blockpos, p_225557_4_, p_225557_6_, p_225557_7_);
                    list.forEach((p_236407_8_) -> {
                        p_225557_7_.foliagePlacer.createFoliage(p_225557_1_, p_225557_2_, p_225557_7_, l1, p_236407_8_, j, l, p_225557_5_, p_225557_6_);
                    });
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(IWorldGenerationBaseReader pLevel, int pTrunkHeight, BlockPos pTopPosition, BaseTreeFeatureConfig pConfig) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i <= pTrunkHeight + 1; ++i) {
            int j = pConfig.minimumSize.getSizeAtHeight(pTrunkHeight, i);

            for(int k = -j; k <= j; ++k) {
                for(int l = -j; l <= j; ++l) {
                    blockpos$mutable.setWithOffset(pTopPosition, k, i, l);
                    if (!isFree(pLevel, blockpos$mutable) || !pConfig.ignoreVines && isVine(pLevel, blockpos$mutable)) {
                        return i - 2;
                    }
                }
            }
        }

        return pTrunkHeight;
    }

    protected void setBlock(IWorldWriter pLevel, BlockPos pPos, BlockState pState) {
        setBlockKnownShape(pLevel, pPos, pState);
    }

    public final boolean place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, BaseTreeFeatureConfig p_241855_5_) {
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        MutableBoundingBox mutableboundingbox = MutableBoundingBox.getUnknownBox();
        boolean flag = this.doPlace(p_241855_1_, p_241855_3_, p_241855_4_, set, set1, mutableboundingbox, p_241855_5_);
        if (mutableboundingbox.x0 <= mutableboundingbox.x1 && flag && !set.isEmpty() && p_241855_1_.getLevel().dimension() == World.OVERWORLD) {
            if (!p_241855_5_.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list1 = Lists.newArrayList(set1);
                list.sort(Comparator.comparingInt(Vector3i::getY));
                list1.sort(Comparator.comparingInt(Vector3i::getY));
                p_241855_5_.decorators.forEach((p_236405_6_) -> {
                    p_236405_6_.place(p_241855_1_, p_241855_3_, list, list1, set2, mutableboundingbox);
                });
            }

            VoxelShapePart voxelshapepart = this.updateLeaves(p_241855_1_, mutableboundingbox, set, set2);
            Template.updateShapeAtEdge(p_241855_1_, 3, voxelshapepart, mutableboundingbox.x0, mutableboundingbox.y0, mutableboundingbox.z0);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShapePart updateLeaves(IWorld pLevel, MutableBoundingBox pBoundingBox, Set<BlockPos> pLogPositions, Set<BlockPos> pFoliagePositions) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(pBoundingBox.getXSpan(), pBoundingBox.getYSpan(), pBoundingBox.getZSpan());
        int i = 6;

        for(int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(BlockPos blockpos : Lists.newArrayList(pFoliagePositions)) {
            if (pBoundingBox.isInside(blockpos)) {
                voxelshapepart.setFull(blockpos.getX() - pBoundingBox.x0, blockpos.getY() - pBoundingBox.y0, blockpos.getZ() - pBoundingBox.z0, true, true);
            }
        }

        for(BlockPos blockpos1 : Lists.newArrayList(pLogPositions)) {
            if (pBoundingBox.isInside(blockpos1)) {
                voxelshapepart.setFull(blockpos1.getX() - pBoundingBox.x0, blockpos1.getY() - pBoundingBox.y0, blockpos1.getZ() - pBoundingBox.z0, true, true);
            }

            for(Direction direction : Direction.values()) {
                blockpos$mutable.setWithOffset(blockpos1, direction);
                if (!pLogPositions.contains(blockpos$mutable)) {
                    BlockState blockstate = pLevel.getBlockState(blockpos$mutable);
                    if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
                        list.get(0).add(blockpos$mutable.immutable());
                        setBlockKnownShape(pLevel, blockpos$mutable, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(1)));
                        if (pBoundingBox.isInside(blockpos$mutable)) {
                            voxelshapepart.setFull(blockpos$mutable.getX() - pBoundingBox.x0, blockpos$mutable.getY() - pBoundingBox.y0, blockpos$mutable.getZ() - pBoundingBox.z0, true, true);
                        }
                    }
                }
            }
        }

        for(int l = 1; l < 6; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for(BlockPos blockpos2 : set) {
                if (pBoundingBox.isInside(blockpos2)) {
                    voxelshapepart.setFull(blockpos2.getX() - pBoundingBox.x0, blockpos2.getY() - pBoundingBox.y0, blockpos2.getZ() - pBoundingBox.z0, true, true);
                }

                for(Direction direction1 : Direction.values()) {
                    blockpos$mutable.setWithOffset(blockpos2, direction1);
                    if (!set.contains(blockpos$mutable) && !set1.contains(blockpos$mutable)) {
                        BlockState blockstate1 = pLevel.getBlockState(blockpos$mutable);
                        if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
                            int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
                            if (k > l + 1) {
                                BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(l + 1));
                                setBlockKnownShape(pLevel, blockpos$mutable, blockstate2);
                                if (pBoundingBox.isInside(blockpos$mutable)) {
                                    voxelshapepart.setFull(blockpos$mutable.getX() - pBoundingBox.x0, blockpos$mutable.getY() - pBoundingBox.y0, blockpos$mutable.getZ() - pBoundingBox.z0, true, true);
                                }

                                set1.add(blockpos$mutable.immutable());
                            }
                        }
                    }
                }
            }
        }

        return voxelshapepart;
    }
}
