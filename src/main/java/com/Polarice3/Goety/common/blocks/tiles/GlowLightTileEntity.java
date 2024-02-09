package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class GlowLightTileEntity extends TileEntity implements ITickableTileEntity {
    public int tickCount;

    public GlowLightTileEntity() {
        this(ModTileEntityType.GLOW_LIGHT.get());
    }

    public GlowLightTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public void tick(){
        ++this.tickCount;
        if (this.tickCount >= 1200){
            this.tickCount = 0;
        }
        if (this.level.isClientSide) {
            if (this.tickCount % 8 == 0) {
                this.spawnParticles();
            }
        }
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return GlowLightTileEntity.this.level;
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(){
        double d0 = (double) this.worldPosition.getX() + 0.5D;
        double d1 = (double) this.worldPosition.getY() + 0.5D;
        double d2 = (double) this.worldPosition.getZ() + 0.5D;
        new ParticleUtil(this.getLevel(), ModParticleTypes.GLOW_LIGHT_EFFECT.get(), d0, d1, d2, 0, 0, 0);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }
}
