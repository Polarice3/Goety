package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IDeadBlock {

    default void spreadSand(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.NotDeadSandImmune(blockState, pLevel, blockpos, pPos)) {
                if (blockState.getMaterial() == Material.STONE) {
                    pLevel.removeBlock(blockpos, false);
                    pLevel.playSound(null, blockpos, SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pLevel.setBlockAndUpdate(blockpos, ModRegistry.DEAD_SANDSTONE.get().defaultBlockState());
                } else if (blockState.is(BlockTags.LOGS)) {
                    pLevel.removeBlock(blockpos, false);
                    pLevel.playSound(null, blockpos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pLevel.setBlockAndUpdate(blockpos, ModRegistry.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
                } else {
                    pLevel.removeBlock(blockpos, false);
                    pLevel.playSound(null, blockpos, SoundEvents.SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pLevel.setBlockAndUpdate(blockpos, ModRegistry.DEAD_SAND.get().defaultBlockState());
                }
            }

            if (BlockFinder.NotDeadSandImmune(blockState, pLevel, blockpos, pPos.below())) {
                for (int l1 = -4; l1 <= 0; ++l1) {
                    int random = pRandom.nextInt(2);
                    int l2 = random == 0 ? l1 : 0;
                    BlockPos blockpos1 = pPos.offset(0, l2, 0);
                    BlockState blockState1 = pLevel.getBlockState(blockpos1);

                    if (blockState1.getMaterial() == Material.STONE) {
                        pLevel.removeBlock(blockpos1, false);
                        pLevel.playSound(null, blockpos1, SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        pLevel.setBlockAndUpdate(blockpos1, ModRegistry.DEAD_SANDSTONE.get().defaultBlockState());
                    }
                }
            }

            for (int k1 = -16; k1 <= 16; ++k1) {
                BlockPos blockpos1 = pPos.offset(0, k1, 0);
                BlockState blockState1 = pLevel.getBlockState(blockpos1);

                if (blockState1.is(BlockTags.LOGS) && blockState1.getBlock() != ModRegistry.HAUNTED_LOG.get()) {
                    pLevel.removeBlock(blockpos1, false);
                    pLevel.playSound(null, blockpos1, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pLevel.setBlockAndUpdate(blockpos1, ModRegistry.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState1.getValue(RotatedPillarBlock.AXIS)));
                }
            }
        }
    }

    default void growHauntedCactus(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            int random = pRandom.nextInt(4);
            if (random == 0) {
                BlockPos blockpos = pPos.above();
                BlockPos west = blockpos.west();
                BlockPos east = blockpos.east();
                BlockPos north = blockpos.north();
                BlockPos south = blockpos.south();
                if (pLevel.isEmptyBlock(blockpos) && pLevel.isEmptyBlock(west) && pLevel.isEmptyBlock(east)
                && pLevel.isEmptyBlock(north) && pLevel.isEmptyBlock(south) && !pLevel.isEmptyBlock(pPos.below())) {
                    if (getCactus(pLevel, pPos).isEmpty()) {
                        pLevel.setBlockAndUpdate(blockpos, ModRegistry.HAUNTED_CACTUS.get().defaultBlockState());
                    }
                }
            }
        }
    }

    default List<Block> getCactus(World world, BlockPos currentTile) {
        List<Block> result = new ArrayList<>();
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                currentTile.offset(-8, -8, -8),
                currentTile.offset(8, 8, 8));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = world.getBlockState(blockToCheck);
            if (blockState.getBlock() == ModRegistry.HAUNTED_CACTUS.get()){
                result.add(blockState.getBlock());
            }
        }
        return result;
    }

    default void spreadSandOnly(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.NotDeadSandImmune(blockState, pLevel, blockpos, pPos)
                    && blockState.getMaterial() != Material.STONE
                    && !blockState.is(BlockTags.LOGS)) {
                pLevel.removeBlock(blockpos, false);
                pLevel.playSound(null, blockpos, SoundEvents.SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlockAndUpdate(blockpos, ModRegistry.DEAD_SAND.get().defaultBlockState());
            }
        }
    }

    default void spreadHaunt(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom){
        BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
        BlockState blockState = pLevel.getBlockState(blockpos);
        if (blockState.is(BlockTags.LOGS)) {
            pLevel.removeBlock(blockpos, false);
            pLevel.playSound(null, blockpos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            pLevel.setBlockAndUpdate(blockpos, ModRegistry.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
        }
        if (blockState.getBlock() instanceof LeavesBlock) {
            pLevel.removeBlock(blockpos, false);
        }
    }

    default void breakParticles(BlockState pState, ServerWorld pLevel, BlockPos pPos){
        double d0 = Math.min(0.2F / 15.0F, 2.5D);
        int i = (int)(150.0D * d0);
        pLevel.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, pState).setPos(pPos), pPos.getX(), pPos.getY(), pPos.getZ(), i, 0.0D, 0.0D, 0.0D, (double)0.15F);

    }
}
