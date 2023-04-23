package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.utils.InfamyHelper;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class InfamyRemoveEntity extends Entity {
    public PlayerEntity player;
    private UUID playerUniqueId;

    public InfamyRemoveEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.hasUUID("Player")) {
            this.playerUniqueId = compound.getUUID("Player");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        if (this.playerUniqueId != null) {
            compound.putUUID("Player", this.playerUniqueId);
        }
    }

    public void setPlayer(@Nullable PlayerEntity ownerIn) {
        this.player = ownerIn;
        this.playerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    @Nullable
    public PlayerEntity getPlayer() {
        if (this.player == null && this.playerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.playerUniqueId);
            if (entity instanceof PlayerEntity) {
                this.player = (PlayerEntity)entity;
            }
        }

        return this.player;
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            InfamyHelper.removeInfamy(this.player);
            if (this.tickCount % 20 == 0) {
                this.remove();
            }
        }
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }
}
