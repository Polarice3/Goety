package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.FangTotemTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class FangTotemBlock extends TotemHeadBlock {

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new FangTotemTileEntity();
    }

}
