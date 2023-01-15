package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MinionEntity extends OwnedEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.defineId(MinionEntity.class, DataSerializers.BYTE);
    @Nullable
    private BlockPos boundOrigin;

    public MinionEntity(EntityType<? extends MinionEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigation = this.createNavigation(p_i50190_2_);
        this.moveControl = new MobUtil.MinionMovementController(this);
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

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

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

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    public void playChargeCry(){
        this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
    }

    public class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !MinionEntity.this.getMoveControl().hasWanted()
                    && MinionEntity.this.random.nextInt(7) == 0
                    && !MinionEntity.this.isCharging();
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = MinionEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = MinionEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(MinionEntity.this.random.nextInt(8) - 4, MinionEntity.this.random.nextInt(6) - 2, MinionEntity.this.random.nextInt(8) - 4);
                if (MinionEntity.this.level.isEmptyBlock(blockpos1)) {
                    MinionEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (MinionEntity.this.getTarget() == null) {
                        MinionEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    public class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (MinionEntity.this.getTarget() != null
                    && !MinionEntity.this.getTarget().isAlliedTo(MinionEntity.this)
                    && !MinionEntity.this.getMoveControl().hasWanted()
                    && MinionEntity.this.random.nextInt(7) == 0) {
                return MinionEntity.this.distanceToSqr(MinionEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return MinionEntity.this.getMoveControl().hasWanted()
                    && MinionEntity.this.isCharging()
                    && MinionEntity.this.getTarget() != null
                    && MinionEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = MinionEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            MinionEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            MinionEntity.this.setIsCharging(true);
            MinionEntity.this.playChargeCry();
        }

        public void stop() {
            MinionEntity.this.setIsCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = MinionEntity.this.getTarget();
            assert livingentity != null;
            if (MinionEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                MinionEntity.this.doHurtTarget(livingentity);
                MinionEntity.this.setIsCharging(false);
            } else {
                double d0 = MinionEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    MinionEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }
}
