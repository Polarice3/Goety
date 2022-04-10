package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.tileentities.TempWebTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SoundUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class WebBallEntity extends ThrowableEntity {

    public WebBallEntity(EntityType<? extends WebBallEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public WebBallEntity(EntityType<? extends ThrowableEntity> p_i48541_1_, double p_i50156_2_, double p_i50156_4_, double p_i50156_6_, World p_i50156_8_) {
        super(ModEntityType.WEB_BALL.get(), p_i50156_2_, p_i50156_4_, p_i50156_6_, p_i50156_8_);
    }

    public WebBallEntity(EntityType<? extends ThrowableEntity> p_i50157_1_, LivingEntity p_i50157_2_, World p_i50157_3_) {
        super(ModEntityType.WEB_BALL.get(), p_i50157_2_, p_i50157_3_);
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            BlockPos blockPos = entity.blockPosition();
            BlockPos blockpos1 = blockPos.below();
            BlockState blockState = this.level.getBlockState(blockPos);
            BlockState blockstate1 = this.level.getBlockState(blockpos1);
            BlockState blockstate2 = ModBlocks.TEMP_WEB.get().defaultBlockState();
            blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.level, blockPos);
            if (this.canPlaceBlock(this.level, blockPos, blockState, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(this, net.minecraftforge.common.util.BlockSnapshot.create(this.level.dimension(), this.level, blockpos1), net.minecraft.util.Direction.UP)) {
                this.level.setBlock(blockPos, blockstate2, 3);
                new SoundUtil(blockPos, SoundEvents.WOOL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            TileEntity tileEntity = this.level.getBlockEntity(entity.blockPosition());
            if (tileEntity instanceof TempWebTileEntity){
                TempWebTileEntity tempWebTileEntity = (TempWebTileEntity) tileEntity;
                int duration;
                if (this.getOwner() instanceof LivingEntity) {
                    if (RobeArmorFinder.FindArachnoSet((LivingEntity) this.getOwner())) {
                        duration = 300;
                    } else {
                        duration = 120;
                    }
                } else {
                    duration = 120;
                }
                tempWebTileEntity.setDuration(duration);
            }
        }
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            BlockPos blockPos = this.blockPosition();
            BlockPos blockpos1 = blockPos.below();
            BlockState blockState = this.level.getBlockState(blockPos);
            BlockState blockstate1 = this.level.getBlockState(blockpos1);
            BlockState blockstate2 = ModBlocks.TEMP_WEB.get().defaultBlockState();
            blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.level, blockPos);
            if (this.canPlaceBlock(this.level, blockPos, blockState, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(this, net.minecraftforge.common.util.BlockSnapshot.create(this.level.dimension(), this.level, blockpos1), net.minecraft.util.Direction.UP)) {
                this.level.setBlock(blockPos, blockstate2, 3);
                new SoundUtil(blockPos, SoundEvents.WOOL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            TileEntity tileEntity = this.level.getBlockEntity(this.blockPosition());
            if (tileEntity instanceof TempWebTileEntity){
                TempWebTileEntity tempWebTileEntity = (TempWebTileEntity) tileEntity;
                int duration;
                if (this.getOwner() instanceof LivingEntity) {
                    if (RobeArmorFinder.FindArachnoSet((LivingEntity) this.getOwner())) {
                        duration = 300;
                    } else {
                        duration = 120;
                    }
                } else {
                    duration = 120;
                }
                tempWebTileEntity.setDuration(duration);
            }
            this.remove();
        }

    }

    private boolean canPlaceBlock(World p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
        return p_220836_4_.isAir(p_220836_1_, p_220836_2_) && !p_220836_5_.isAir(p_220836_1_, p_220836_6_) && p_220836_5_.isCollisionShapeFullBlock(p_220836_1_, p_220836_6_);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean ignoreExplosion(){return false;}

    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void defineSynchedData() {

    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((ServerWorld)this.level).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove();
        }

        return true;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.CLOUD;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
