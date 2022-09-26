package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.states.IronFingerThickness;
import com.Polarice3.Goety.common.blocks.states.ModStateProperties;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class IronFingerBlock extends Block implements IPlantable {
    public static final EnumProperty<IronFingerThickness> THICKNESS = ModStateProperties.IRON_FINGER_THICKNESS;
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public IronFingerBlock() {
        super(Properties.of(Material.METAL)
                .noOcclusion()
                .randomTicks()
                .strength(1.5F, 3.0F)
                .sound(SoundType.METAL)
                .dynamicShape()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(THICKNESS, IronFingerThickness.TIP));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(THICKNESS);
    }

    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (!pState.canSurvive(pLevel, pCurrentPos)) {
            pLevel.getBlockTicks().scheduleTick(pCurrentPos, this, 1);
        }

        IronFingerThickness dripstonethickness = calculateFingerThickness(pLevel, pCurrentPos);
        return pState.setValue(THICKNESS, dripstonethickness);
    }

    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        BlockState blockstate1 = pLevel.getBlockState(pPos.below());
        return this.canSustainThis(blockstate1, pLevel, pPos, Direction.UP, this) && !pLevel.getBlockState(pPos.above()).getMaterial().isLiquid();
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (!pLevel.isDay() || pLevel.isRaining()) {
            growFingerIfPossible(pState, pLevel, pPos, pRandom);
        }
        if (isTip(pState)){
            if (pLevel.isThundering()){
                if (pRandom.nextFloat() < 0.1F){
                    LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(pLevel);
                    if (lightningboltentity != null){
                        lightningboltentity.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
                        pLevel.addFreshEntity(lightningboltentity);
                        if (canTipGrow(pLevel, pPos)){
                            grow(pLevel, pPos);
                        }
                    }
                }
            }
        }
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        IWorld levelaccessor = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        IronFingerThickness dripstonethickness = calculateFingerThickness(levelaccessor, blockpos);
        return dripstonethickness == null ? null : this.defaultBlockState().setValue(THICKNESS, dripstonethickness);
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return VoxelShapes.empty();
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        IronFingerThickness dripstonethickness = pState.getValue(THICKNESS);
        VoxelShape voxelshape;
        if (dripstonethickness == IronFingerThickness.TIP) {
            voxelshape = TIP_SHAPE_UP;
        } else if (dripstonethickness == IronFingerThickness.FRUSTUM) {
            voxelshape = FRUSTUM_SHAPE;
        } else if (dripstonethickness == IronFingerThickness.MIDDLE) {
            voxelshape = MIDDLE_SHAPE;
        } else {
            voxelshape = BASE_SHAPE;
        }

        Vector3d vec3 = pState.getOffset(pLevel, pPos);
        return voxelshape.move(vec3.x, 0.0D, vec3.z);
    }

    public AbstractBlock.OffsetType getOffsetType() {
        return AbstractBlock.OffsetType.XZ;
    }

    public void growFingerIfPossible(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        if (isTip(pState)) {
            if (canGrow(blockstate)) {
                int i;
                for (i = 1; pLevel.getBlockState(pPos.below(i)).is(this); ++i) {
                }

                if (i < 9) {
                    if (canTipGrow(pLevel, pPos)) {
                        if (pRandom.nextBoolean()) {
                            grow(pLevel, pPos);
                        }
                    }
                }
            }
        }
    }

    private static void grow(ServerWorld pServer, BlockPos pPos) {
        BlockPos blockpos = pPos.relative(Direction.UP);
        BlockState blockstate = pServer.getBlockState(blockpos);
        if (blockstate.isAir()) {
            createFinger(pServer, blockpos, IronFingerThickness.TIP);
        }
    }

    private static void createFinger(IWorld pLevel, BlockPos pPos, IronFingerThickness pThickness) {
        BlockState blockstate = ModBlocks.IRON_FINGER.get().defaultBlockState().setValue(THICKNESS, pThickness);
        pLevel.setBlock(pPos, blockstate, 3);
    }

    private static BlockPos findTip(BlockState blockState, IWorld world, BlockPos blockPos, int age) {
        if (isTip(blockState)) {
            return blockPos;
        } else {
            return findBlockVertical(world, blockPos, IronFingerBlock::isTip, age).orElse(null);
        }
    }

    private static IronFingerThickness calculateFingerThickness(IWorld world, BlockPos blockPos) {
        BlockState blockAbove = world.getBlockState(blockPos.relative(Direction.UP));
        BlockState blockBelow = world.getBlockState(blockPos.relative(Direction.DOWN));
        if (!blockAbove.is(ModBlocks.IRON_FINGER.get())) {
            return IronFingerThickness.TIP;
        } else {
            IronFingerThickness aboveThickness = blockAbove.getValue(THICKNESS);
            if (aboveThickness != IronFingerThickness.TIP) {
                return !blockBelow.is(ModBlocks.IRON_FINGER.get()) ? IronFingerThickness.BASE : IronFingerThickness.MIDDLE;
            } else {
                return IronFingerThickness.FRUSTUM;
            }
        }
    }

    private static boolean canTipGrow(ServerWorld pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.relative(Direction.UP);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        return blockstate.isAir();
    }

    private static boolean isTip(BlockState blockState) {
        if (!blockState.is(ModBlocks.IRON_FINGER.get())) {
            return false;
        } else {
            IronFingerThickness dripstonethickness = blockState.getValue(THICKNESS);
            return dripstonethickness == IronFingerThickness.TIP;
        }
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    private static boolean canGrow(BlockState blockState) {
        return blockState.is(ModBlocks.IRON_FINGER.get()) || blockState.is(ModTags.Blocks.DEAD_SANDS);
    }

    private static Optional<BlockPos> findBlockVertical(IWorld world, BlockPos blockPos, Predicate<BlockState> blockStatePredicate, int age) {
        BlockPos.Mutable blockpos$mutableblockpos = blockPos.mutable();

        for(int i = 1; i < age; ++i) {
            blockpos$mutableblockpos.move(Direction.UP);
            BlockState blockstate = world.getBlockState(blockpos$mutableblockpos);
            if (blockStatePredicate.test(blockstate)) {
                return Optional.of(blockpos$mutableblockpos.immutable());
            }

            if (blockpos$mutableblockpos.getY() > world.getMaxBuildHeight()) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return defaultBlockState();
    }

    public boolean canSustainThis(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(facing));
        if (plant.getBlock() == this.getBlock()){
            return state.is(ModBlocks.IRON_FINGER.get()) || state.is(ModTags.Blocks.DEAD_SANDS);
        } else {
            return false;
        }
    }

}
