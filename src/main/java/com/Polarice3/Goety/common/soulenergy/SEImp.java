package com.Polarice3.Goety.common.soulenergy;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SEImp implements ISoulEnergy{
    private boolean seActive;
    private int soulEnergy = 0;
    private RegistryKey<World> dimension = World.OVERWORLD;
    private BlockPos ArcaBlock = new BlockPos(0, 0, 0);

    @Override
    public BlockPos getArcaBlock() {
        return this.ArcaBlock;
    }

    @Override
    public void setArcaBlock(BlockPos blockPos) {
        this.ArcaBlock = blockPos;
    }

    @Override
    public RegistryKey<World> getArcaBlockDimension() {
        return this.dimension;
    }

    @Override
    public void setArcaBlockDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean getSEActive() {
        return this.seActive;
    }

    @Override
    public void setSEActive(boolean seActive) {
        this.seActive = seActive;
    }

    @Override
    public int getSoulEnergy() {
        return this.soulEnergy;
    }

    @Override
    public void setSoulEnergy(int soulEnergy) {
        this.soulEnergy = soulEnergy;
    }

    @Override
    public boolean increaseSE(int increase) {
        if (this.soulEnergy >= MainConfig.MaxArcaSouls.get()) {
            return false;
        }
        this.soulEnergy = Math.min(this.soulEnergy + increase, MainConfig.MaxArcaSouls.get());
        return true;
    }

    @Override
    public boolean decreaseSE(int decrease) {
        if (this.soulEnergy == 0) {
            return false;
        }
        this.soulEnergy = Math.max(this.soulEnergy - decrease, 0);
        return true;
    }
}
