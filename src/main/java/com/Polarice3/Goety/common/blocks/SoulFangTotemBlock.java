package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.SoulFangTotemTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class SoulFangTotemBlock extends ContainerBlock implements IForgeBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SoulFangTotemBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof PlayerEntity){
            if (tileentity instanceof SoulFangTotemTileEntity){
                SoulFangTotemTileEntity soulFangTotemTile = (SoulFangTotemTileEntity) tileentity;
                soulFangTotemTile.setOwnerId(pPlacer.getUUID());
            }
        }
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SoulFangTotemTileEntity();
    }
}
