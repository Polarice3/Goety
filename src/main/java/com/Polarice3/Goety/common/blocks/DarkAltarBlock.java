package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.client.inventory.crafting.DarkAltarRecipes;
import com.Polarice3.Goety.common.tileentities.DarkAltarTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;
import java.util.Optional;

public class DarkAltarBlock extends ContainerBlock implements IForgeBlock {
    public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public DarkAltarBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof DarkAltarTileEntity) {
            DarkAltarTileEntity darkAltarTile = (DarkAltarTileEntity)tileentity;
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Optional<DarkAltarRecipes> optional = darkAltarTile.getRecipes(itemstack);
            if (optional.isPresent()) {
                if (!pLevel.isClientSide && darkAltarTile.placeItem(pPlayer.abilities.instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    return ActionResultType.SUCCESS;
                }

                return ActionResultType.CONSUME;
            }
            if (itemstack.isEmpty() || itemstack == darkAltarTile.getItems().get(0)){
                if (!darkAltarTile.getItems().isEmpty()){
                    InventoryHelper.dropContents(pLevel, pPlayer.blockPosition(), darkAltarTile.getItems());
                    darkAltarTile.getItems().clear();
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
            return ActionResultType.CONSUME;
        }

        return ActionResultType.PASS;
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof DarkAltarTileEntity) {
                InventoryHelper.dropContents(pLevel, pPos, ((DarkAltarTileEntity)tileentity).getItems());
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new DarkAltarTileEntity();
    }
}
