package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.GhostFireTrapTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class GhostFireTrapBlock extends ContainerBlock {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public GhostFireTrapBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(TRIGGERED, Boolean.FALSE).setValue(POWERED, Boolean.FALSE));
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof PlayerEntity){
            if (tileentity instanceof GhostFireTrapTileEntity){
                GhostFireTrapTileEntity ghostFireTrapTile = (GhostFireTrapTileEntity) tileentity;
                ghostFireTrapTile.setOwnerId(pPlacer.getUUID());
            }
        }
    }

    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean flag = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean flag1 = pState.getValue(TRIGGERED);
        if (flag && !flag1 && !pLevel.getBlockState(pPos.above()).isSolidRender(pLevel, pPos.above())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof GhostFireTrapTileEntity) {
                GhostFireTrapTileEntity ghostFireTrapTile = (GhostFireTrapTileEntity) tileentity;
                ghostFireTrapTile.fire();
            }
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
        } else if (!flag && flag1) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.FALSE), 4);
        }

    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TRIGGERED, POWERED);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new GhostFireTrapTileEntity();
    }
}
