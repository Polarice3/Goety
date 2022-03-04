package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SoulFireballEntity extends DamagingProjectileEntity {
    public SoulFireballEntity(EntityType<? extends DamagingProjectileEntity> type, World world) {
        super(type, world);
    }

    public SoulFireballEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.SOUL_FIREBALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public SoulFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.SOUL_FIREBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            if (!entity.fireImmune()) {
                Entity entity1 = this.getOwner();
                int i = entity.getRemainingFireTicks();
                entity.setSecondsOnFire(15);
                boolean flag = entity.hurt(DamageSource.indirectMagic(this, entity1), 6.0F);
                if (!flag) {
                    entity.setRemainingFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.doEnchantDamageEffects((LivingEntity)entity1, entity);
                }
            }

        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.Mode.NONE);
            this.remove();
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
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
