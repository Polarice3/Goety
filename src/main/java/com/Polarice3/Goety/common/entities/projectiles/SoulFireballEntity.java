package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.ICultistMinion;
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
import net.minecraft.util.math.vector.Vector3d;
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

    public void tick() {
        super.tick();
        Vector3d vector3d = this.getDeltaMovement();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + level.random.nextDouble()/2, d1 + 0.5D, d2 + level.random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
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

    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.Mode.NONE);
            this.remove();
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (!(pEntity instanceof ICultistMinion) && this.getOwner() instanceof AbstractCultistEntity){
            return super.canHitEntity(pEntity);
        } else {
            return false;
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
        return ParticleTypes.SMOKE;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
