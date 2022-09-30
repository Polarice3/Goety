package com.Polarice3.Goety.common.capabilities.spider;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SpiderLevelsStore implements Capability.IStorage<ISpiderLevels> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISpiderLevels> capability, ISpiderLevels instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("spiderLevels", instance.getSpiderLevel());
        return compound;
    }

    @Override
    public void readNBT(Capability<ISpiderLevels> capability, ISpiderLevels instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setSpiderLevel(compound.getInt("spiderLevels"));
    }
}
