package com.Polarice3.Goety.common.entities.utilities;

import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StormEntity extends Entity {

    public StormEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (this.tickCount % 20 == 0) {
                if (!serverWorld.isThundering()) {
                    serverWorld.setWeatherParameters(0, 6000, true, true);
                }
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
