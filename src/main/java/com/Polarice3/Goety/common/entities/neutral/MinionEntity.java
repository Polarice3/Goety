package com.Polarice3.Goety.common.entities.neutral;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MinionEntity extends CreatureEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.defineId(MinionEntity.class, DataSerializers.BYTE);
    private LivingEntity owner;
    @Nullable
    private BlockPos boundOrigin;

    public MinionEntity(EntityType<? extends MinionEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigation = this.createNavigation(p_i50190_2_);
        this.moveControl = new MoveHelperController(this);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VEX_FLAGS, (byte)0);
    }

    private boolean getVexFlag(int mask) {
        int i = this.entityData.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.entityData.get(VEX_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(VEX_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setIsCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(MinionEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - MinionEntity.this.getX(), this.wantedY - MinionEntity.this.getY(), this.wantedZ - MinionEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < MinionEntity.this.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    MinionEntity.this.setDeltaMovement(MinionEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    MinionEntity.this.setDeltaMovement(MinionEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (MinionEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = MinionEntity.this.getDeltaMovement();
                        MinionEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        MinionEntity.this.yBodyRot = MinionEntity.this.yRot;
                    } else {
                        double d2 = MinionEntity.this.getTarget().getX() - MinionEntity.this.getX();
                        double d1 = MinionEntity.this.getTarget().getZ() - MinionEntity.this.getZ();
                        MinionEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        MinionEntity.this.yBodyRot = MinionEntity.this.yRot;
                    }
                }

            }
        }
    }

}
