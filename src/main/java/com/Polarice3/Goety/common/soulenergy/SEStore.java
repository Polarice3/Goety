package com.Polarice3.Goety.common.soulenergy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SEStore implements Capability.IStorage<ISoulEnergy>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISoulEnergy> capability, ISoulEnergy instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("seActive", instance.getSEActive());
        compound.putInt("soulEnergy", instance.getSoulEnergy());
        if (instance.getArcaBlock() != null) {
            compound.putInt("arcax", instance.getArcaBlock().getX());
            compound.putInt("arcay", instance.getArcaBlock().getY());
            compound.putInt("arcaz", instance.getArcaBlock().getZ());
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<ISoulEnergy> capability, ISoulEnergy instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setSEActive(compound.getBoolean("seActive"));
        instance.setArcaBlock(new BlockPos(compound.getInt("arcax"), compound.getInt("arcay"), compound.getInt("arcaz")));
        instance.setSoulEnergy(compound.getInt("soulEnergy"));
    }
}
