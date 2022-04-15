package com.Polarice3.Goety.common.lichdom;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class LichStore implements Capability.IStorage<ILichdom>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<ILichdom> capability, ILichdom instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("lichdom", instance.getLichdom());
        return compound;
    }

    @Override
    public void readNBT(Capability<ILichdom> capability, ILichdom instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setLichdom(compound.getBoolean("lichdom"));
    }
}
