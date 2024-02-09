package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.WindTotemTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WindTotemBlock extends TotemHeadBlock {

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new WindTotemTileEntity();
    }

}
