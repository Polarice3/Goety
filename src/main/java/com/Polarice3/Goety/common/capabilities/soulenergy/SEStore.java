package com.Polarice3.Goety.common.capabilities.soulenergy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class SEStore implements Capability.IStorage<ISoulEnergy>{
    private static final Logger LOGGER = LogManager.getLogger();

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
            ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, instance.getArcaBlockDimension().location()).resultOrPartial(LOGGER::error).ifPresent(
                    (p_241148_1_) -> compound.put("dimension", p_241148_1_));
        }
        if (instance.cooldowns() != null){
            ListNBT listTag = new ListNBT();
            instance.cooldowns().save(listTag);
            compound.put("coolDowns", listTag);
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<ISoulEnergy> capability, ISoulEnergy instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setSEActive(compound.getBoolean("seActive"));
        instance.setArcaBlock(new BlockPos(compound.getInt("arcax"), compound.getInt("arcay"), compound.getInt("arcaz")));
        instance.setSoulEnergy(compound.getInt("soulEnergy"));
        instance.setArcaBlockDimension(World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, compound.get("dimension")).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD));
        if (compound.contains("coolDowns", Constants.NBT.TAG_LIST)){
            ListNBT listTag = (ListNBT) compound.get("coolDowns");
            instance.cooldowns().load(listTag);
        }
    }
}
