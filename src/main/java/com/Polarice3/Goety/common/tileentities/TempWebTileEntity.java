package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class TempWebTileEntity extends TileEntity implements ITickableTileEntity {
    public int duration = 120;

    public TempWebTileEntity() {
        this(ModTileEntityType.TEMP_WEB.get());
    }

    public TempWebTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return TempWebTileEntity.this.level;
    }

    @Override
    public void tick() {
        if (this.level != null) {
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                if (this.duration > 0) {
                    --this.duration;
                } else {
                    this.level.destroyBlock(this.worldPosition, false);
                    this.level.removeBlockEntity(this.worldPosition);
                }
            }
        }
    }

    public void breakParticles(BlockState pState, ServerWorld pLevel, BlockPos pPos){
        double d0 = Math.min(0.2F / 15.0F, 2.5D);
        int i = (int)(150.0D * d0);
        pLevel.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, pState).setPos(pPos), pPos.getX(), pPos.getY()+ 1, pPos.getZ(), i, 0.0D, 0.0D, 0.0D, (double)0.15F);

    }

    public int getDuration(){
        return this.duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }
}
