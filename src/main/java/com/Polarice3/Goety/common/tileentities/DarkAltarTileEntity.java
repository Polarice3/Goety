package com.Polarice3.Goety.common.tileentities;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DarkAltarTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[1];
    private final int[] cookingTime = new int[1];
    private final List<BlockPos> ladders = Lists.newArrayList();
    private final List<BlockPos> rails = Lists.newArrayList();
    private final List<BlockPos> pumpkin = Lists.newArrayList();

    public DarkAltarTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void clearContent() {

    }

    @Override
    public void tick() {

    }

    private boolean checkAnimaFocus() {
        for(int j1 = -3; j1 <= 3; ++j1) {
            for(int k1 = -3; k1 <= 3; ++k1) {
                for(int l1 = -3; l1 <= 3; ++l1) {
                    BlockPos blockpos1 = this.worldPosition.offset(j1, k1, l1);
                    assert this.level != null;
                    BlockState blockstate = this.level.getBlockState(blockpos1);

                    if (blockstate.getBlock() == Blocks.LADDER) {
                        this.ladders.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.RAIL) {
                        this.rails.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.CARVED_PUMPKIN) {
                        this.pumpkin.add(blockpos1);
                    }
                }
            }
        }

        return this.ladders.size() >= 15 && this.rails.size() >= 15 && this.pumpkin.size() >= 1;
    }
}
