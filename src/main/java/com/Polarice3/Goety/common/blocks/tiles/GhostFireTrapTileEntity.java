package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.common.blocks.GhostFireTrapBlock;
import com.Polarice3.Goety.common.entities.projectiles.GhostFireEntity;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.UUID;

public class GhostFireTrapTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    public int ticks;
    public boolean firing;
    private UUID ownerUUID;

    public GhostFireTrapTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public GhostFireTrapTileEntity(){
        this(ModTileEntityType.GHOST_FIRE_TRAP.get());
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        UUID uuid;
        if (nbt.hasUUID("Owner")) {
            uuid = nbt.getUUID("Owner");
        } else {
            String s = nbt.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.level.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }

        return compound;
    }

    public void fire(){
        if (!this.firing) {
            this.playSound(ModSounds.SUMMON_SPELL.get());
            this.ticks = 0;
            this.firing = true;
        }
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (this.firing) {
                ++this.ticks;
            }
            if (this.ticks == 1) {
                this.activated = 20;
                BlockPos blockPos = this.getBlockPos().above();
                GhostFireEntity ghostFire = new GhostFireEntity(this.level, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, this.getTrueOwner());
                ghostFire.setSoulEating(true);
                this.level.addFreshEntity(ghostFire);
            }
            if (this.ticks >= 70) {
                this.firing = false;
                this.ticks = 0;
            }
            if (this.activated != 0) {
                --this.activated;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(GhostFireTrapBlock.POWERED, true), 3);
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(GhostFireTrapBlock.POWERED, false), 3);
            }
        }
    }

    public void playSound(SoundEvent sound) {
        this.level.playSound(null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void setRemoved() {
        super.setRemoved();
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
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
}
