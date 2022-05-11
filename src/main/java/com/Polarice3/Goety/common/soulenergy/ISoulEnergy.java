package com.Polarice3.Goety.common.soulenergy;

import net.minecraft.util.math.BlockPos;

public interface ISoulEnergy {
    BlockPos getArcaBlock();
    void setArcaBlock(BlockPos blockPos);
    boolean getSEActive();
    void setSEActive(boolean seActive);
    int getSoulEnergy();
    void setSoulEnergy(int soulEnergy);
    boolean increaseSE(int increase);
    boolean decreaseSE(int decrease);
}
