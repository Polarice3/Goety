package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ModChestTileEntity extends ChestTileEntity {
    protected ModChestTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public ModChestTileEntity() {
        super(ModTileEntityType.MOD_CHEST.get());
    }
}
