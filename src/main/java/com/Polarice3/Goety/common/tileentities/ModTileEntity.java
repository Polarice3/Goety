package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ModTileEntity extends TileEntity {
    public final List<BlockPos> ladders = Lists.newArrayList();
    public final List<BlockPos> rails = Lists.newArrayList();
    public final List<BlockPos> pumpkin = Lists.newArrayList();
    public final List<BlockPos> pedestals = Lists.newArrayList();
    public final List<TileEntity> pedestalitem = Lists.newArrayList();

    public ModTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public void findAnimaStructure() {
        for(int j1 = -4; j1 <= 4; ++j1) {
            for(int k1 = -4; k1 <= 4; ++k1) {
                for(int l1 = -4; l1 <= 4; ++l1) {
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
    }

    public void findPedestals() {
        for(int j1 = -4; j1 <= 4; ++j1) {
            for(int k1 = -4; k1 <= 4; ++k1) {
                for(int l1 = -4; l1 <= 4; ++l1) {
                    BlockPos blockpos1 = this.worldPosition.offset(j1, k1, l1);
                    assert this.level != null;
                    BlockState blockstate = this.level.getBlockState(blockpos1);

                    if (blockstate.getBlock() == ModRegistry.PEDESTAL.get()) {
                        if (blockstate.hasTileEntity()){
                            TileEntity tileEntity = this.level.getBlockEntity(blockpos1);
                            if (tileEntity instanceof PedestalTileEntity){
                                PedestalTileEntity pedestalTileEntity = (PedestalTileEntity) tileEntity;
                                if (!pedestalTileEntity.getItem().isEmpty()){
                                    this.pedestalitem.add(pedestalTileEntity);
                                    this.pedestals.add(blockpos1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean checkAnimaRequirements(){
        return this.ladders.size() >= 15 && this.rails.size() >= 15 && this.pumpkin.size() >= 1;
    }

    public boolean checkPedestals(){
        return !this.pedestals.isEmpty();
    }

}
