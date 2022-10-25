package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class NetherMeteorEntity extends DamagingProjectileEntity {
    public NetherMeteorEntity(EntityType<? extends DamagingProjectileEntity> type, World world) {
        super(type, world);
    }

    public NetherMeteorEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.NETHER_METEOR.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public NetherMeteorEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.NETHER_METEOR.get(), shooter, accelX, accelY, accelZ, worldIn);
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
            boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MainConfig.ApocalypseMode.get();
            Explosion.Mode mode = flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, flag, mode);
            this.remove();
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (pEntity instanceof OwnedEntity && ((OwnedEntity) pEntity).getTrueOwner() == this.getOwner()){
            return false;
        } else {
            return super.canHitEntity(pEntity);
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
        return ParticleTypes.LARGE_SMOKE;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
