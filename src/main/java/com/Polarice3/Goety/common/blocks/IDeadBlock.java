package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.SoundUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static net.minecraft.block.Block.dropResources;

public interface IDeadBlock {

    default void spreadSand(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.NotDeadSandImmune(blockState)) {
                if (blockState.getMaterial() == Material.STONE) {
                    pLevel.destroyBlock(blockpos, false);
                    pLevel.setBlockAndUpdate(blockpos, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                } else if (blockState.is(BlockTags.LOGS)) {
                    pLevel.destroyBlock(blockpos, false);
                    pLevel.setBlockAndUpdate(blockpos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
                } else {
                    pLevel.destroyBlock(blockpos, false);
                    pLevel.playSound(null, blockpos, SoundEvents.SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pLevel.setBlockAndUpdate(blockpos, ModBlocks.DEAD_SAND.get().defaultBlockState());
                }
            }

            if (dryUpWater(pLevel, pPos)){
                new SoundUtil(pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            dryUpAnimals(pLevel, pPos);
            growHauntedCactus(pLevel, pPos, pRandom);

            if (BlockFinder.NotDeadSandImmune(blockState)) {
                for (int l1 = -4; l1 <= 0; ++l1) {
                    int random = pRandom.nextInt(2);
                    int l2 = random == 0 ? l1 : 0;
                    BlockPos blockpos1 = pPos.offset(0, l2, 0);
                    BlockState blockState1 = pLevel.getBlockState(blockpos1);

                    if (blockState1.getMaterial() == Material.STONE) {
                        pLevel.destroyBlock(blockpos1, false);
                        pLevel.setBlockAndUpdate(blockpos1, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                    }
                }
            }

            for (int k1 = -16; k1 <= 16; ++k1) {
                BlockPos blockpos1 = pPos.offset(0, k1, 0);
                BlockState blockState1 = pLevel.getBlockState(blockpos1);

                if (blockState1.is(BlockTags.LOGS) && blockState1.getBlock() != ModBlocks.HAUNTED_LOG.get()) {
                    pLevel.destroyBlock(blockpos1, false);
                    pLevel.setBlockAndUpdate(blockpos1, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState1.getValue(RotatedPillarBlock.AXIS)));
                }
            }
        }
    }

    default void growHauntedCactus(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            int random = pRandom.nextInt(256);
            if (random == 0) {
                BlockPos blockpos = pPos.above();
                BlockPos west = blockpos.west();
                BlockPos east = blockpos.east();
                BlockPos north = blockpos.north();
                BlockPos south = blockpos.south();
                if (pLevel.isEmptyBlock(blockpos) && pLevel.isEmptyBlock(west) && pLevel.isEmptyBlock(east)
                && pLevel.isEmptyBlock(north) && pLevel.isEmptyBlock(south) && !pLevel.isEmptyBlock(pPos.below())) {
                    if (getSurroundingCactus(pLevel, pPos).isEmpty() && getTotalCactus(pLevel, pPos).size() < 6) {
                        pLevel.setBlockAndUpdate(blockpos, ModBlocks.HAUNTED_CACTUS.get().defaultBlockState());
                    }
                }
            }
        }
    }

    default List<Block> getSurroundingCactus(World world, BlockPos currentTile) {
        List<Block> result = new ArrayList<>();
        int random = world.random.nextInt(8);
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                currentTile.offset(-random, -random, -random),
                currentTile.offset(random, random, random));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = world.getBlockState(blockToCheck);
            if (blockState.getBlock() == ModBlocks.HAUNTED_CACTUS.get()){
                result.add(blockState.getBlock());
            }
        }
        return result;
    }

    default List<Block> getTotalCactus(World world, BlockPos currentTile) {
        List<Block> result = new ArrayList<>();
        int radius = 24;
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                currentTile.offset(-radius, -radius, -radius),
                currentTile.offset(radius, radius, radius));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = world.getBlockState(blockToCheck);
            if (blockState.getBlock() == ModBlocks.HAUNTED_CACTUS.get()){
                result.add(blockState.getBlock());
            }
        }
        return result;
    }

    default void spreadSandOnly(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.NotDeadSandImmune(blockState)
                    && blockState.getMaterial() != Material.STONE
                    && !blockState.is(BlockTags.LOGS)) {
                pLevel.destroyBlock(blockpos, false);
                pLevel.playSound(null, blockpos, SoundEvents.SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlockAndUpdate(blockpos, ModBlocks.DEAD_SAND.get().defaultBlockState());
            }
        }
    }

    default void spreadHaunt(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (blockState.is(BlockTags.LOGS)) {
                pLevel.destroyBlock(blockpos, false);
                pLevel.setBlockAndUpdate(blockpos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
            }
            if (blockState.getBlock() instanceof LeavesBlock) {
                pLevel.removeBlock(blockpos, false);
            }
        }
    }

    default boolean dryUpWater(World pLevel, BlockPos pPos) {
        if (MainConfig.DeadSandSpread.get()) {
            if (pLevel.isDay() && !pLevel.isRaining() && !pLevel.isThundering()) {
                Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
                queue.add(new Tuple<>(pPos, 0));
                int i = 0;

                while (!queue.isEmpty()) {
                    Tuple<BlockPos, Integer> tuple = queue.poll();
                    BlockPos blockpos = tuple.getA();
                    int j = tuple.getB();

                    for (Direction direction : Direction.values()) {
                        BlockPos blockpos1 = blockpos.relative(direction);
                        BlockState blockstate = pLevel.getBlockState(blockpos1);
                        FluidState fluidstate = pLevel.getFluidState(blockpos1);
                        Material material = blockstate.getMaterial();
                        if (fluidstate.is(FluidTags.WATER)) {
                            if (blockstate.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler) blockstate.getBlock()).takeLiquid(pLevel, blockpos1, blockstate) != Fluids.EMPTY) {
                                ++i;
                                if (j < 6) {
                                    queue.add(new Tuple<>(blockpos1, j + 1));
                                }
                            } else if (blockstate.getBlock() instanceof FlowingFluidBlock) {
                                pLevel.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                                ++i;
                                if (j < 6) {
                                    queue.add(new Tuple<>(blockpos1, j + 1));
                                }
                            } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                                TileEntity tileentity = blockstate.hasTileEntity() ? pLevel.getBlockEntity(blockpos1) : null;
                                dropResources(blockstate, pLevel, blockpos1, tileentity);
                                pLevel.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                                ++i;
                                if (j < 6) {
                                    queue.add(new Tuple<>(blockpos1, j + 1));
                                }
                            }
                        }
                    }

                    if (i > 32) {
                        break;
                    }
                }

                return i > 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    default void dryUpAnimals(ServerWorld pLevel, BlockPos pPos){
        List<Block> result = new ArrayList<>();
        List<Block> result2 = new ArrayList<>();
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                pPos.offset(-16, -2, -16),
                pPos.offset(16, 2, 16));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = pLevel.getBlockState(blockToCheck);
            if (blockState.getBlock() instanceof IDeadBlock){
                result.add(blockState.getBlock());
            }
            if (blockState.getFluidState().is(FluidTags.WATER)){
                result2.add(blockState.getBlock());
            }
        }
        if (result.size() > 32 && result2.isEmpty()) {
            if (pLevel.isDay() && !pLevel.isRaining() && !pLevel.isThundering()) {
                for (LivingEntity livingEntity : pLevel.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(pPos.above()))) {
                    if (livingEntity instanceof AnimalEntity) {
                        livingEntity.hurt(DamageSource.DRY_OUT, 1.0F);
                        livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 1200));
                        livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1200));
                    }
                }
            }
        }
    }
}
