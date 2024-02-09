package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ModSignTileEntity extends SignTileEntity {
    public ModSignTileEntity() {
        super();
    }

    @Override
    public TileEntityType<?> getType() {
        return ModTileEntityType.SIGN_TILE_ENTITIES.get();
    }
}
