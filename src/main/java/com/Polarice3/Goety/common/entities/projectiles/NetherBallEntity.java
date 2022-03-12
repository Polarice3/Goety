package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.entities.hostile.NethernalEntity;
import com.Polarice3.Goety.common.entities.hostile.ScorchEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.*;
import net.minecraftforge.fml.network.NetworkHooks;

public class NetherBallEntity extends DamagingProjectileEntity {
    public NetherBallEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public NetherBallEntity(World world, LivingEntity livingEntity, double d1, double d2, double d3) {
        super(ModEntityType.NETHERBALL.get(), livingEntity, d1, d2, d3, world);
    }

    public void tick() {
        super.tick();
        this.level.explode(this, this.getX() + this.random.nextDouble(), this.getY(), this.getZ() + this.random.nextDouble(), 0.5F, Explosion.Mode.NONE);
    }

    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (result.getType() != RayTraceResult.Type.ENTITY) {
            if (!this.level.isClientSide) {
                if (this.level.getDifficulty() == Difficulty.HARD) {
                    for (int i = 0; i < 3; ++i) {
                        ScorchEntity scorchEntity = new ScorchEntity(ModEntityType.SCORCH.get(), this.level);
                        BlockPos blockpos = this.blockPosition().offset(-2 + this.random.nextInt(5), 1, -2 + this.random.nextInt(5));
                        scorchEntity.moveTo(blockpos, 0.0F, 0.0F);
                        scorchEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                        scorchEntity.setBoundOrigin(blockpos);
                        scorchEntity.setLimitedLife(20 * (30 + this.random.nextInt(90)));
                        this.level.addFreshEntity(scorchEntity);
                    }
                }
                NethernalEntity nethernalEntity = new NethernalEntity(ModEntityType.NETHERNAL.get(), this.level);
                BlockPos blockPos = this.blockPosition();
                nethernalEntity.moveTo(blockPos, 0.0F, 0.0F);
                nethernalEntity.setLimitedLife(3600);
                this.level.addFreshEntity(nethernalEntity);
                boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), 8.0F, flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
                this.remove();
            }

        }
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean ignoreExplosion(){return true;}

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.FLAME;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
