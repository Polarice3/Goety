package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static net.minecraft.block.Block.dropResources;

public interface IDeadBlock {

    int SAND_LIMIT = 256;
    int SAND_RANGE = 8;

    default void spreadSand(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            boolean flag = false;
            BlockState blockState = pLevel.getBlockState(pPos);
            for(Direction direction : Direction.values()) {
                BlockPos blockpos1 = pPos.relative(direction);
                BlockState blockstate = pLevel.getBlockState(blockpos1);
                if (BlockFinder.ActivateDeadSand(blockstate)) {
                    flag = true;
                }
            }
            if (flag) {
                int pos1 = pRandom.nextInt(3) - 1;
                int pos2 = pRandom.nextInt(3) - 1;
                int pos3 = pRandom.nextInt(3) - 1;
                if (pos1 == 0 && pos2 == 0 && pos3 == 0) {
                    pos2 = 1;
                }
                BlockPos blockpos = pPos.offset(pos1, pos2, pos3);

                if (!BlockFinder.isInRain(pLevel, pPos)) {
                    if (getTotalDeadSands(pLevel, pPos).size() < SAND_LIMIT) {
                        BlockFinder.DeadSandReplaceLagFree(blockpos, pLevel);
                    } else {
                        if (pRandom.nextFloat() < 0.5F) {
                            BlockFinder.DeadSandReplaceLagFree(blockpos, pLevel);
                        }
                    }
                    for (int j1 = -2; j1 < 2; ++j1) {
                        for (int k1 = 0; k1 <= 1; ++k1) {
                            for (int l1 = -2; l1 < 2; ++l1) {
                                BlockPos blockpos2 = pPos.offset(j1, k1, l1);
                                BlockState blockState2 = pLevel.getBlockState(blockpos2);
                                if (blockState2.getBlock() instanceof BushBlock && BlockFinder.validPlants(blockpos2, pLevel)) {
                                    pLevel.removeBlock(blockpos2, false);
                                    pLevel.setBlockAndUpdate(blockpos2, ModBlocks.HAUNTED_BUSH.get().defaultBlockState());
                                }
                                if (BlockFinder.LivingBlocks(blockState2)) {
                                    pLevel.removeBlock(blockpos2, false);
                                    pLevel.setBlockAndUpdate(blockpos2, ModBlocks.DEAD_BLOCK.get().defaultBlockState());
                                }
                                if (blockState2.getBlock() instanceof TallGrassBlock || blockState2.getBlock() instanceof SnowBlock) {
                                    pLevel.removeBlock(blockpos2, false);
                                }
                            }
                        }
                    }
                }

                if (blockState.isSolidRender(pLevel, pPos)){
                    dryUpWater(pLevel, pPos);
                    growHauntedCactus(pLevel, pPos, pRandom);
                    growIronFingers(pLevel, pPos);
                    blockSky(pLevel, pPos);
                }
                desiccateMobs(pLevel, pPos);

                for (int l1 = -4; l1 <= 0; ++l1) {
                    int random = pRandom.nextInt(2);
                    int l2 = random == 0 ? l1 : 0;
                    BlockPos blockpos1 = pPos.offset(0, l2, 0);
                    BlockState blockState1 = pLevel.getBlockState(blockpos1);
                    BlockState blockState2 = pLevel.getBlockState(pPos.below());

                    if (blockState1.getMaterial() == Material.STONE && BlockFinder.NotDeadSandImmune(blockState1)) {
                        if (BlockFinder.NotDeadSandImmune(blockState2)) {
                            pLevel.destroyBlock(blockpos1, false);
                            pLevel.setBlockAndUpdate(blockpos1, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                        }
                    }
                }

                for (int k1 = 0; k1 <= 16; ++k1) {
                    BlockPos blockpos1 = pPos.offset(0, k1, 0);
                    BlockState blockState1 = pLevel.getBlockState(blockpos1);
                    BlockState blockState2 = pLevel.getBlockState(pPos.above());

                    if (BlockFinder.ActivateDeadSand(blockState2)) {
                        if (BlockFinder.NotDeadSandImmune(blockState1)) {
                            if (blockState1.is(BlockTags.LOGS) && blockState1.getBlock() != ModBlocks.HAUNTED_LOG.get()) {
                                pLevel.destroyBlock(blockpos1, false);
                                pLevel.setBlockAndUpdate(blockpos1, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState1.getValue(RotatedPillarBlock.AXIS)));
                            }
                        }
                        if (blockState1.is(Blocks.CACTUS) || blockState1.getBlock() instanceof CactusBlock) {
                            pLevel.removeBlock(blockpos1, false);
                            pLevel.setBlockAndUpdate(blockpos1, ModBlocks.HAUNTED_CACTUS.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    default void spreadDeadBlocks(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            int pos1 = pRandom.nextInt(3) - 1;
            int pos2 = pRandom.nextInt(3) - 1;
            int pos3 = pRandom.nextInt(3) - 1;
            if (pos1 == 0 && pos2 == 0 && pos3 == 0){
                pos2 = 1;
            }
            BlockPos blockpos = pPos.offset(pos1, pos2, pos3);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.LivingBlocks(blockState)) {
                pLevel.removeBlock(blockpos, false);
                pLevel.setBlockAndUpdate(blockpos, ModBlocks.DEAD_BLOCK.get().defaultBlockState());
            }
        }
    }

    default void growHauntedCactus(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockState cactus = ModBlocks.HAUNTED_CACTUS.get().defaultBlockState();
            int random = pRandom.nextInt(256);
            if (random == 0) {
                BlockPos blockpos = pPos.above();
                BlockPos west = blockpos.west();
                BlockPos east = blockpos.east();
                BlockPos north = blockpos.north();
                BlockPos south = blockpos.south();
                if (!pLevel.isEmptyBlock(pPos) && cactus.canSurvive(pLevel, blockpos)) {
                    if ((pLevel.isEmptyBlock(blockpos) || pLevel.getBlockState(blockpos).getBlock() == ModBlocks.DEAD_PILE.get())
                            && pLevel.isEmptyBlock(west) && pLevel.isEmptyBlock(east)
                            && pLevel.isEmptyBlock(north) && pLevel.isEmptyBlock(south)) {
                        if (getSurroundingCactus(pLevel, pPos).isEmpty() && getTotalCactus(pLevel, pPos).size() < 6) {
                            pLevel.setBlockAndUpdate(blockpos, cactus);
                        }
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
        int radius = 64;
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

    default List<Block> getTotalDeadSands(World world, BlockPos currentTile) {
        List<Block> result = new ArrayList<>();
        int radius = SAND_RANGE;
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                currentTile.offset(-radius, -radius, -radius),
                currentTile.offset(radius, radius, radius));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = world.getBlockState(blockToCheck);
            if (blockState.getBlock().is(ModTags.Blocks.DEAD_SANDS)){
                result.add(blockState.getBlock());
            }
        }
        return result;
    }

    default void growIronFingers(ServerWorld pLevel, BlockPos pPos){
        if (MainConfig.DeadSandSpread.get()) {
            BlockState finger = ModBlocks.IRON_FINGER.get().defaultBlockState();
            if (!pLevel.isDay() || !pLevel.isRaining()) {
                BlockPos blockpos = pPos.above();
                BlockPos west = blockpos.west();
                BlockPos east = blockpos.east();
                BlockPos north = blockpos.north();
                BlockPos south = blockpos.south();
                if (!pLevel.isEmptyBlock(pPos) && finger.canSurvive(pLevel, blockpos))
                if ((pLevel.isEmptyBlock(blockpos) || pLevel.getBlockState(blockpos).getBlock() == ModBlocks.DEAD_PILE.get())
                        && pLevel.isEmptyBlock(west) && pLevel.isEmptyBlock(east)
                        && pLevel.isEmptyBlock(north) && pLevel.isEmptyBlock(south)) {
                    if (getSurroundingCactus(pLevel, pPos).size() >= 4 && getTotalFingers(pLevel, pPos).isEmpty()) {
                        pLevel.setBlockAndUpdate(blockpos, finger);
                    }
                }
            }
        }
    }

    default List<Block> getTotalFingers(World world, BlockPos currentTile) {
        List<Block> result = new ArrayList<>();
        int radius = 16;
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                currentTile.offset(-radius, -radius, -radius),
                currentTile.offset(radius, radius, radius));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockState blockState = world.getBlockState(blockToCheck);
            if (blockState.getBlock() == ModBlocks.IRON_FINGER.get()){
                result.add(blockState.getBlock());
            }
        }
        return result;
    }

    default void spreadSandOnly(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (!BlockFinder.isInRain(pLevel, pPos)) {
                if (getTotalDeadSands(pLevel, pPos).size() < SAND_LIMIT) {
                    sandSpread(pLevel, blockState, blockpos);
                } else {
                    if (pRandom.nextFloat() < 0.5F) {
                        sandSpread(pLevel, blockState, blockpos);
                    }
                }
            }
            for (int l1 = 0; l1 <= 1; ++l1){
                BlockPos blockpos2 = pPos.offset(0, l1, 0);
                BlockState blockState2 = pLevel.getBlockState(blockpos2);
                if (blockState2.getBlock() instanceof BushBlock && BlockFinder.validPlants(blockpos2, pLevel)) {
                    pLevel.removeBlock(blockpos2, false);
                    pLevel.setBlockAndUpdate(blockpos2, ModBlocks.HAUNTED_BUSH.get().defaultBlockState());
                }
                if (BlockFinder.LivingBlocks(blockState2)){
                    pLevel.removeBlock(blockpos2, false);
                    pLevel.setBlockAndUpdate(blockpos2, ModBlocks.DEAD_BLOCK.get().defaultBlockState());
                }
            }
        }
    }

    default void sandSpread(ServerWorld pLevel, BlockState blockState, BlockPos blockPos){
        if (BlockFinder.NotDeadSandImmune(blockState)
                && blockState.getMaterial() != Material.STONE
                && !blockState.is(BlockTags.LOGS)
                && !blockState.is(BlockTags.ICE)
                && !(blockState.getBlock() instanceof BushBlock)
                && !BlockFinder.LivingBlocks(blockState)) {
            pLevel.destroyBlock(blockPos, false);
            pLevel.playSound(null, blockPos, SoundEvents.SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            pLevel.setBlockAndUpdate(blockPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
        }
    }

    default void spreadHaunt(ServerWorld pLevel, BlockPos pPos, Random pRandom){
        if (MainConfig.DeadSandSpread.get()) {
            BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
            BlockState blockState = pLevel.getBlockState(blockpos);
            if (BlockFinder.NotDeadSandImmune(blockState)) {
                if (blockState.is(BlockTags.LOGS)) {
                    pLevel.destroyBlock(blockpos, false);
                    pLevel.setBlockAndUpdate(blockpos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
                }
                if (blockState.getBlock() instanceof LeavesBlock) {
                    pLevel.removeBlock(blockpos, false);
                }
            }
        }
    }

    default void dryUpWater(World pLevel, BlockPos pPos) {
        if (MainConfig.DeadSandDryWater.get()) {
            if (!BlockFinder.isInRain(pLevel, pPos)) {
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

                    if (i > 64) {
                        break;
                    }
                }

            }
        }
    }

    default void desiccateMobs(ServerWorld pLevel, BlockPos pPos){
        if (MainConfig.DeadSandDesiccate.get()) {
            List<Block> result = new ArrayList<>();
            List<Block> result2 = new ArrayList<>();
            Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                    pPos.offset(-16, -2, -16),
                    pPos.offset(16, 2, 16));
            for (BlockPos blockToCheck : blocksToCheck) {
                BlockState blockState = pLevel.getBlockState(blockToCheck);
                if (blockState.getBlock() instanceof IDeadBlock) {
                    result.add(blockState.getBlock());
                }
                if (blockState.getFluidState().is(FluidTags.WATER)) {
                    result2.add(blockState.getBlock());
                }
            }
            if (result.size() > 32 && result2.isEmpty()) {
                if (!BlockFinder.isInRain(pLevel, pPos)) {
                    for (LivingEntity livingEntity : pLevel.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(pPos.above()))) {
                        if (!RobeArmorFinder.FindNecroBootsofWander(livingEntity)){
                            desiccate(livingEntity);
                        }
                    }
                }
            }
        }
    }

    default void desiccate(LivingEntity livingEntity){
        int random = livingEntity.getRandom().nextInt(8);
        PotionEvent.PotionApplicableEvent event = new PotionEvent.PotionApplicableEvent(livingEntity, new EffectInstance(ModEffects.DESICCATE.get()));
        if (event.getResult() == Event.Result.ALLOW){
            livingEntity.hurt(ModDamageSource.DESICCATE, 1.0F);
        }
        if (livingEntity.hasEffect(ModEffects.DESICCATE.get())){
            if (random == 0){
                EffectsUtil.amplifyEffect(livingEntity, ModEffects.DESICCATE.get(), 1200);
            } else {
                EffectsUtil.resetDuration(livingEntity, ModEffects.DESICCATE.get(), 1200);
            }
        } else {
            livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
        }
    }

    default void blockSky(ServerWorld pLevel, BlockPos pPos){
        if (MainConfig.DeadSandDarkSky.get()) {
            List<Block> result = new ArrayList<>();
            Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                    pPos.offset(-8, -8, -8),
                    pPos.offset(8, 8, 8));
            for (BlockPos blockToCheck : blocksToCheck) {
                BlockState blockState = pLevel.getBlockState(blockToCheck);
                if (blockState.getBlock() instanceof IDeadBlock) {
                    result.add(blockState.getBlock());
                }
            }
            if (result.size() > 32) {
                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pPos.getX(), pPos.getY(), pPos.getZ());

                if (MainConfig.DeadSandDarkSkyNoOcclude.get()){
                    while (blockpos$mutable.getY() < 255) {
                        blockpos$mutable.move(Direction.UP);
                    }
                } else {
                    while (blockpos$mutable.getY() < 255 && (pLevel.getBlockState(blockpos$mutable).isAir() || pLevel.getBlockState(blockpos$mutable).getBlock() instanceof IDeadBlock)) {
                        blockpos$mutable.move(Direction.UP);
                    }
                }

                BlockState ceiling = pLevel.getBlockState(blockpos$mutable);
                if (ceiling.isAir() && blockpos$mutable.getY() > 254) {
                    pLevel.setBlockAndUpdate(blockpos$mutable, ModBlocks.DARK_CLOUD.get().defaultBlockState());
                }
            }
        }
    }
}
