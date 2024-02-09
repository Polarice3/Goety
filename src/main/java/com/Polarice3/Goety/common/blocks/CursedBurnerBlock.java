package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.client.inventory.crafting.CursedBurnerRecipes;
import com.Polarice3.Goety.common.blocks.tiles.CursedBurnerTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
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

public class CursedBurnerBlock extends ContainerBlock implements IWaterLoggable {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public CursedBurnerBlock() {
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
        if (tileentity instanceof CursedBurnerTileEntity) {
            CursedBurnerTileEntity burnerTileEntity = (CursedBurnerTileEntity)tileentity;
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Optional<CursedBurnerRecipes> optional = burnerTileEntity.getRecipes(itemstack);
            if (optional.isPresent()) {
                if (!pLevel.isClientSide && burnerTileEntity.placeItem(pPlayer.abilities.instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    return ActionResultType.SUCCESS;
                }

                return ActionResultType.CONSUME;
            }
            if (itemstack.isEmpty() || itemstack == burnerTileEntity.getItems().get(0)){
                if (!burnerTileEntity.getItems().isEmpty()){
                    InventoryHelper.dropContents(pLevel, pPlayer.blockPosition(), burnerTileEntity.getItems());
                    burnerTileEntity.getItems().clear();
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        return ActionResultType.PASS;
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof CursedBurnerTileEntity) {
                InventoryHelper.dropContents(pLevel, pPos, ((CursedBurnerTileEntity)tileentity).getItems());
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (!pEntity.fireImmune() && pState.getValue(LIT) && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {
            pEntity.hurt(DamageSource.IN_FIRE, 1.0F);
        }

        super.entityInside(pState, pLevel, pPos, pEntity);
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
        return new CursedBurnerTileEntity();
    }
}
