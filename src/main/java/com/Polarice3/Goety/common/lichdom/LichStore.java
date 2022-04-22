package com.Polarice3.Goety.common.lichdom;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class LichStore implements Capability.IStorage<ILichdom>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<ILichdom> capability, ILichdom instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("lichdom", instance.getLichdom());
        if (instance.getArcaBlock() != null) {
            compound.putInt("arcax", instance.getArcaBlock().getX());
            compound.putInt("arcay", instance.getArcaBlock().getY());
            compound.putInt("arcaz", instance.getArcaBlock().getZ());
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<ILichdom> capability, ILichdom instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setLichdom(compound.getBoolean("lichdom"));
        instance.setArcaBlock(new BlockPos(compound.getInt("arcax"), compound.getInt("arcay"), compound.getInt("arcaz")));
    }
}
