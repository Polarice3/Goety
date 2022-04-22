package com.Polarice3.Goety.common.lichdom;

import net.minecraft.util.math.BlockPos;

public class LichImp implements ILichdom{

    private boolean lichdom;
    private boolean dead;
    private BlockPos ArcaBlock = new BlockPos(0, 0, 0);

    @Override
    public boolean getLichdom() {
        return this.lichdom;
    }

    @Override
    public void setLichdom(boolean lichdom) {
        this.lichdom = lichdom;
    }

    @Override
    public BlockPos getArcaBlock() {
        return this.ArcaBlock;
    }

    @Override
    public void setArcaBlock(BlockPos blockPos) {
        this.ArcaBlock = blockPos;
    }

}
