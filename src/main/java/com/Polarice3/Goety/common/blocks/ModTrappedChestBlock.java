package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.ModTrappedChestTileEntity;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public class ModTrappedChestBlock extends ModChestBlock {
    public ModTrappedChestBlock(Properties p_i225757_1_) {
        super(p_i225757_1_, () -> {
            return ModTileEntityType.MOD_TRAPPED_CHEST.get();
        });
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new ModTrappedChestTileEntity();
    }

    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return MathHelper.clamp(ChestTileEntity.getOpenCount(blockAccess, pos), 0, 15);
    }

    public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return side == Direction.UP ? blockState.getDirectSignal(blockAccess, pos, side) : 0;
    }

}
