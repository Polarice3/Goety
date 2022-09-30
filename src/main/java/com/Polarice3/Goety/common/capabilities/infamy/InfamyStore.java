package com.Polarice3.Goety.common.capabilities.infamy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class InfamyStore implements Capability.IStorage<IInfamy> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IInfamy> capability, IInfamy instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("infamy", instance.getInfamy());
        return compound;
    }

    @Override
    public void readNBT(Capability<IInfamy> capability, IInfamy instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setInfamy(compound.getInt("infamy"));
    }
}
