package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.ModChestTileEntity;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.ChestBlock;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class ModChestBlock extends ChestBlock {
    public ModChestBlock(Properties p_i225757_1_, Supplier<TileEntityType<? extends ChestTileEntity>> p_i225757_2_) {
        super(p_i225757_1_, p_i225757_2_);
    }

    public ModChestBlock(Properties properties){
        this(properties, () -> {
           return ModTileEntityType.MOD_CHEST.get();
        });
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new ModChestTileEntity();
    }

}
