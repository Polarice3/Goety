package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

public class GrandLavaballEntity extends ExplosiveProjectileEntity{
    public float explosionPower = 1.0F;

    public GrandLavaballEntity(EntityType<? extends ExplosiveProjectileEntity> type, World world) {
        super(type, world);
    }

    public GrandLavaballEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.GRAND_LAVABALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public GrandLavaballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.GRAND_LAVABALL.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    public void tick() {
        super.tick();
        Vector3d vector3d = this.getDeltaMovement();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.level.addParticle(ParticleTypes.FLAME, d0 + level.random.nextDouble()/2, d1 + 0.5D, d2 + level.random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
    }

    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            boolean flag = this.isDangerous();
            Explosion.Mode mode = flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionPower, flag, mode);
            this.remove();
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(ModDamageSource.modFireball(this.getOwner(), this.level), 6.0F);
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }

        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() instanceof OwnedEntity){
            OwnedEntity owner = (OwnedEntity) this.getOwner();
            if (pEntity instanceof OwnedEntity){
                OwnedEntity entity = (OwnedEntity) pEntity;
                if (owner.getTrueOwner() == entity.getTrueOwner()){
                    return false;
                }
            }
            if (owner.getTrueOwner() == pEntity){
                return false;
            }
        }
        if (pEntity instanceof OwnedEntity && ((OwnedEntity) pEntity).getTrueOwner() == this.getOwner()){
            return false;
        }
        return super.canHitEntity(pEntity);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean ignoreExplosion(){
        return true;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.LARGE_SMOKE;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void setExplosionPower(float pExplosionPower) {
        this.explosionPower = pExplosionPower;
    }

    @Override
    public float getExplosionPower() {
        return this.explosionPower;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
