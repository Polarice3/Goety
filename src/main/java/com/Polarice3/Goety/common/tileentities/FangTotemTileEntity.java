package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FangTotemTileEntity extends TotemTileEntity {

    public FangTotemTileEntity() {
        this(ModTileEntityType.FANG_TOTEM.get());
    }

    public FangTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    @Nullable
    @Override
    public LivingEntity findExistingTarget() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        assert this.level != null;
        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))){
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (MobUtil.playerValidity(player, false)) {
                    return livingEntity;
                }
            } else {
                if (livingEntity.isOnGround()) {
                    return livingEntity;
                }
            }
        }
        return null;
    }

    @Override
    public void SpecialEffect() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK);
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))) {
            float f = (float) MathHelper.atan2(entity.getZ() - this.getBlockPos().getZ(), entity.getX() - this.getBlockPos().getX());
            if (entity instanceof PlayerEntity) {
                if (MobUtil.playerValidity((PlayerEntity) entity, false)) {
                    this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f, 1);
                }
            } else {
                if (entity.isOnGround()) {
                    this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f, 1);
                }
            }
        }
    }

    private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.getLevel().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.getLevel(), blockpos1, Direction.UP)) {
                if (!this.getLevel().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.getLevel().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.getLevel(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

        if (flag) {
            this.getLevel().addFreshEntity(new EvokerFangsEntity(this.getLevel(), p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, null));
        }

    }
}
