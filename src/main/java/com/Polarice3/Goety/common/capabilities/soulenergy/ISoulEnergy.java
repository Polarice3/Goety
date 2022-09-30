package com.Polarice3.Goety.common.capabilities.soulenergy;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISoulEnergy {
    BlockPos getArcaBlock();
    void setArcaBlock(BlockPos blockPos);
    RegistryKey<World> getArcaBlockDimension();
    void setArcaBlockDimension(RegistryKey<World> dimension);
    boolean getSEActive();
    void setSEActive(boolean seActive);
    int getSoulEnergy();
    void setSoulEnergy(int soulEnergy);
    boolean increaseSE(int increase);
    boolean decreaseSE(int decrease);
}
