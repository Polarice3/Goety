package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.FangTotemTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class FangTotemBlock extends TotemHeadBlock {

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new FangTotemTileEntity();
    }

}
