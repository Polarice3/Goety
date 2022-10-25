package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpikeGeyserEntity extends ThrowableEntity {

    public SpikeGeyserEntity(EntityType<? extends SpikeGeyserEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public SpikeGeyserEntity(EntityType<? extends ThrowableEntity> p_i48541_1_, double p_i50156_2_, double p_i50156_4_, double p_i50156_6_, World p_i50156_8_) {
        super(ModEntityType.SPIKE_GEYSER.get(), p_i50156_2_, p_i50156_4_, p_i50156_6_, p_i50156_8_);
    }

    public SpikeGeyserEntity(EntityType<? extends ThrowableEntity> p_i50157_1_, LivingEntity p_i50157_2_, World p_i50157_3_) {
        super(ModEntityType.SPIKE_GEYSER.get(), p_i50157_2_, p_i50157_3_);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.spawnParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(){
        Vector3d vector3d = this.getDeltaMovement();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.level.addParticle(ParticleTypes.ENCHANTED_HIT, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
    }

    protected void onHit(RayTraceResult pResult) {
        double d0 = this.getY();
        double d1 = this.getY() + 1.0D;
        boolean flag = false;
        if (this.getMobOwner() != null) {
            if (this.getMobOwner().isDeadOrDying()) {
                this.remove();
            }
            if ((this.getMobOwner().getTarget() != null && this.getY() <= this.getMobOwner().getTarget().getY()) || pResult.getType() == RayTraceResult.Type.ENTITY) {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag){
            super.onHit(pResult);
            if (!this.isInWall()) {
                this.createSpellEntity(this.getX(), this.getZ(), d0, d1, 1, 0);
            }
            this.remove();
        }
    }

    private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level, blockpos1, Direction.UP)) {
                if (!this.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level, blockpos);
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
            this.level.addFreshEntity(new SpikeEntity(this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, this.getMobOwner()));
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.THORNS_HIT, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
        }

    }

    public MobEntity getMobOwner(){
        if (this.getOwner() != null && this.getOwner() instanceof MobEntity){
            return (MobEntity) this.getOwner();
        } else {
            return null;
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null && this.getOwner().isAlliedTo(pEntity)){
            return false;
        } else {
            return super.canHitEntity(pEntity);
        }
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
