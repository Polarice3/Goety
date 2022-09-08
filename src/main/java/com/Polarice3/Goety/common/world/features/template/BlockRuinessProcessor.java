package com.Polarice3.Goety.common.world.features.template;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRuinessProcessor extends StructureProcessor {
    public static final Codec<BlockRuinessProcessor> CODEC = Codec.FLOAT.fieldOf("ruiness").xmap(BlockRuinessProcessor::new, (p_237064_0_) -> {
        return p_237064_0_.ruiness;
    }).codec();
    private final float ruiness;

    public BlockRuinessProcessor(float p_i232115_1_) {
        this.ruiness = p_i232115_1_;
    }

    @Nullable
    public Template.BlockInfo processBlock(IWorldReader pLevel, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings pSettings) {
        Random random = pSettings.getRandom(p_230386_5_.pos);
        BlockState blockstate = p_230386_5_.state;
        BlockPos blockpos = p_230386_5_.pos;
        BlockState blockstate1 = null;
        if (!blockstate.is(Blocks.STONE_BRICKS) && !blockstate.is(Blocks.STONE) && !blockstate.is(Blocks.CHISELED_STONE_BRICKS)) {
            if (blockstate.is(BlockTags.STAIRS)) {
                blockstate1 = this.maybeReplaceStairs(random, p_230386_5_.state);
            } else if (blockstate.is(BlockTags.SLABS)) {
                blockstate1 = this.maybeReplaceSlab(random);
            } else if (blockstate.is(BlockTags.WALLS)) {
                blockstate1 = this.maybeReplaceWall(random);
            } else if (blockstate.is(Blocks.OBSIDIAN)) {
                blockstate1 = this.maybeReplaceObsidian(random);
            }
        } else {
            blockstate1 = this.maybeReplaceFullStoneBlock(random);
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, p_230386_5_.nbt) : p_230386_5_;
    }

    @Nullable
    private BlockState maybeReplaceFullStoneBlock(Random pRandom) {
        if (pRandom.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] ablockstate = new BlockState[]{Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(pRandom, Blocks.STONE_BRICK_STAIRS)};
            BlockState[] ablockstate1 = new BlockState[]{Blocks.COBBLESTONE.defaultBlockState(), getRandomFacingStairs(pRandom, Blocks.COBBLESTONE_STAIRS)};
            return this.getRandomBlock(pRandom, ablockstate, ablockstate1);
        }
    }

    @Nullable
    private BlockState maybeReplaceStairs(Random pRandom, BlockState pState) {
        Direction direction = pState.getValue(StairsBlock.FACING);
        Half half = pState.getValue(StairsBlock.HALF);
        if (pRandom.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] ablockstate = new BlockState[]{Blocks.STONE_SLAB.defaultBlockState(), Blocks.STONE_BRICK_SLAB.defaultBlockState()};
            BlockState[] ablockstate1 = new BlockState[]{Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, direction).setValue(StairsBlock.HALF, half), Blocks.COBBLESTONE_SLAB.defaultBlockState()};
            return this.getRandomBlock(pRandom, ablockstate, ablockstate1);
        }
    }

    @Nullable
    private BlockState maybeReplaceSlab(Random pRandom) {
        return pRandom.nextFloat() < this.ruiness ? Blocks.COBBLESTONE_SLAB.defaultBlockState() : null;
    }

    @Nullable
    private BlockState maybeReplaceWall(Random pRandom) {
        if (pRandom.nextFloat() < this.ruiness){
            if (pRandom.nextBoolean()){
                return Blocks.COBBLESTONE_WALL.defaultBlockState();
            } else {
                return Blocks.COBBLESTONE_SLAB.defaultBlockState();
            }
        } else {
            return null;
        }
    }

    @Nullable
    private BlockState maybeReplaceObsidian(Random pRandom) {
        return pRandom.nextFloat() < 0.25F ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : null;
    }

    private static BlockState getRandomFacingStairs(Random pRandom, Block pBlock) {
        return pBlock.defaultBlockState().setValue(StairsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(pRandom)).setValue(StairsBlock.HALF, Half.values()[pRandom.nextInt(Half.values().length)]);
    }

    private BlockState getRandomBlock(Random pRandom, BlockState[] p_237069_2_, BlockState[] p_237069_3_) {
        return pRandom.nextFloat() < this.ruiness ? getRandomBlock(pRandom, p_237069_3_) : getRandomBlock(pRandom, p_237069_2_);
    }

    private static BlockState getRandomBlock(Random pRandom, BlockState[] pPossibleStates) {
        return pPossibleStates[pRandom.nextInt(pPossibleStates.length)];
    }

    protected IStructureProcessorType<?> getType() {
        return IStructureProcessorType.BLOCK_AGE;
    }
}
