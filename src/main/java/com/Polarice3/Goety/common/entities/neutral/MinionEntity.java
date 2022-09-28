package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MinionEntity extends OwnedEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.defineId(MinionEntity.class, DataSerializers.BYTE);

    public MinionEntity(EntityType<? extends MinionEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigation = this.createNavigation(p_i50190_2_);
        this.moveControl = new MobUtil.MoveHelperController(this);
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

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != ModEffects.GOLDTOUCHED.get() && super.canBeAffected(pPotioneffect);
    }

}
