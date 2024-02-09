package com.Polarice3.Goety.common.blocks.tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class RitualTileEntity extends TileEntity {
    public final List<BlockPos> first = Lists.newArrayList();
    public final List<BlockPos> second = Lists.newArrayList();
    public final List<BlockPos> third = Lists.newArrayList();

    public RitualTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        this.readNetwork(compound);
        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        this.writeNetwork(compound);
        return super.save(compound);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.writeNetwork(new CompoundNBT()));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.writeNetwork(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readNetwork(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.readNetwork(tag);
    }

    public void readNetwork(CompoundNBT compound) {
    }

    public CompoundNBT writeNetwork(CompoundNBT compound) {
        return compound;
    }

    public void markNetworkDirty() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

}
/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */