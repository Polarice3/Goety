package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.tileentity.TileEntityType;

public class ModTrappedChestTileEntity extends ModChestTileEntity {
    protected ModTrappedChestTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public ModTrappedChestTileEntity() {
        super(ModTileEntityType.MOD_TRAPPED_CHEST.get());
    }

    protected void signalOpenCount() {
        super.signalOpenCount();
        if(this.level != null) {
            this.level.updateNeighborsAt(this.worldPosition.below(), this.getBlockState().getBlock());
        }
    }
}
