package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class ArcaTileEntity extends TileEntity implements ITickableTileEntity {
    public int tickCount;
    private float activeRotation;
    private UUID ownerUUID;

    public ArcaTileEntity() {
        super(ModTileEntityType.ARCA.get());
    }

    @Override
    public void tick() {
        ++this.tickCount;
        ++this.activeRotation;
        if (this.level instanceof ServerWorld) {
            ChunkPos chunkPos = this.level.getChunkAt(this.worldPosition).getPos();
            ServerWorld world = (ServerWorld) this.level;
            if (!world.getForcedChunks().contains(chunkPos.toLong())) {
                world.setChunkForced(chunkPos.x, chunkPos.z, true);
                if (!world.isAreaLoaded(this.worldPosition, 2)) {
                    world.getChunkAt(this.worldPosition).setLoaded(true);
                }
            }
        }
    }

    public float getActiveRotation(float p_205036_1_) {
        return (this.activeRotation + p_205036_1_) * -0.0375F;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.level.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = super.save(tag);
        if (this.getOwnerId() != null) {
            tag.putUUID("Owner", this.getOwnerId());
        }
        return tag;
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.ownerUUID;
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.ownerUUID = p_184754_1_;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public PlayerEntity getPlayer(){
        return (PlayerEntity) this.getTrueOwner();
    }

    public void makeWorkParticles() {
        if (SEHelper.getSESouls(getPlayer()) <= 0){
            return;
        }
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            for (int p = 0; p < 4; ++p) {
                new ParticleUtil(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0, 0, 0);
                new ParticleUtil(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        load(this.getBlockState(), tag);
    }
}
