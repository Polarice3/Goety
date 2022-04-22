package com.Polarice3.Goety.common.lichdom;

import net.minecraft.util.math.BlockPos;

public interface ILichdom {
    boolean getLichdom();
    void setLichdom(boolean lichdom);
    BlockPos getArcaBlock();
    void setArcaBlock(BlockPos blockPos);
}
