package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.client.inventory.crafting.SoulAbsorberRecipes;
import com.Polarice3.Goety.common.blocks.tiles.SoulAbsorberTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class SoulAbsorberBlock extends ContainerBlock implements IWaterLoggable {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SoulAbsorberBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.5F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .lightLevel(litBlockEmission())
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE).setValue(LIT, Boolean.FALSE));
    }

    private static ToIntFunction<BlockState> litBlockEmission() {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? 10 : 0;
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof SoulAbsorberTileEntity) {
            SoulAbsorberTileEntity burnerTileEntity = (SoulAbsorberTileEntity)tileentity;
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Optional<SoulAbsorberRecipes> optional = burnerTileEntity.getRecipes(itemstack);
            if (optional.isPresent()) {
                if (!pLevel.isClientSide && burnerTileEntity.placeItem(pPlayer.abilities.instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    return ActionResultType.SUCCESS;
                }

                return ActionResultType.CONSUME;
            }
            if (itemstack.getItem() instanceof ITotem){
                if (ITotem.currentSouls(itemstack) > 0){
                    if (!pLevel.isClientSide && burnerTileEntity.placeItem(pPlayer.abilities.instabuild ? itemstack.copy() : itemstack, 9999)) {
                        return ActionResultType.SUCCESS;
                    }
                }

                return ActionResultType.CONSUME;
            }
            if (itemstack.isEmpty() || itemstack == burnerTileEntity.getItem(0)){
                if (!burnerTileEntity.getItem(0).isEmpty()){
                    dropItemStack(pLevel, pPlayer.blockPosition(), burnerTileEntity.getItem(0));
                    burnerTileEntity.removeItem(0, 1);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        return ActionResultType.PASS;
    }

    public static void dropItemStack(World pLevel, BlockPos pPos, ItemStack pStack) {
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(pPos.getX()) + pLevel.random.nextDouble() * d1 + d2;
        double d4 = Math.floor(pPos.getY()) + pLevel.random.nextDouble() * d1;
        double d5 = Math.floor(pPos.getZ()) + pLevel.random.nextDouble() * d1 + d2;

        while(!pStack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(pLevel, d3, d4, d5, pStack.split(pLevel.random.nextInt(21) + 10));
            float f = 0.05F;
            itementity.setDeltaMovement(pLevel.random.nextGaussian() * (double)f, pLevel.random.nextGaussian() * (double)f + (double)0.2F, pLevel.random.nextGaussian() * (double)f);
            pLevel.addFreshEntity(itementity);
        }

    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof SoulAbsorberTileEntity) {
                dropItemStack(pLevel, pPos, ((SoulAbsorberTileEntity) tileentity).getItem(1));
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        IWorld iworld = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        boolean flag = iworld.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public boolean placeLiquid(IWorld pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!pState.getValue(BlockStateProperties.WATERLOGGED) && pFluidState.getType() == Fluids.WATER) {
            pLevel.setBlock(pPos, pState.setValue(WATERLOGGED, Boolean.TRUE), 3);
            pLevel.getLiquidTicks().scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            return true;
        } else {
            return false;
        }
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED, LIT);
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SoulAbsorberTileEntity();
    }
}
