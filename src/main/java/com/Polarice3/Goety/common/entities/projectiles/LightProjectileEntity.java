package com.Polarice3.Goety.common.entities.projectiles;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class LightProjectileEntity extends ArrowEntity {
    public LightProjectileEntity(EntityType<? extends LightProjectileEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public LightProjectileEntity(final World world, final double x, final double y, final double z) {
        super(world, x, y, z);
    }

    public LightProjectileEntity(final World world, final LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public void tick() {
        Vector3d vector3d = this.getDeltaMovement();

        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();

        if (this.tickCount % 600 == 0){
            this.remove();
        }

        if (this.inGround) {
            this.inGround = false;
            this.setDeltaMovement(this.getDeltaMovement());
        }

        Vector3d vector3d2 = this.position();
        Vector3d vector3d3 = vector3d2.add(vector3d);

        RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            vector3d3 = raytraceresult.getLocation();
        }

        EntityRayTraceResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }

        if (raytraceresult instanceof EntityRayTraceResult) {
            Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
            Entity entity1 = this.getOwner();
            if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity)entity1).canHarmPlayer((PlayerEntity)entity)) {
                raytraceresult = null;
            }
        }

        if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.hasImpulse = true;
        }

        double x = this.getX() + vector3d.x;
        double y = this.getY() + vector3d.y;
        double z = this.getZ() + vector3d.z;

        this.setPos(x,y,z);

        if(level.isClientSide) {
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            this.level.addParticle(sourceParticle(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            this.level.addParticle(trailParticle(), d0 + level.random.nextDouble()/2, d1 + 0.5D, d2 + level.random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        if (!this.level.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();
            if (owner instanceof LivingEntity) {
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        livingTarget.addEffect(new EffectInstance(Effects.GLOWING, 200));
                    }
                }
            } else {
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        livingTarget.addEffect(new EffectInstance(Effects.GLOWING, 200));
                    }
                }
            }
        }
        this.remove();
    }

    protected void onHitBlock(BlockRayTraceResult pResult) {
        super.onHitBlock(pResult);
        BlockPos pos = pResult.getBlockPos().relative(pResult.getDirection());
        if (this.getOwner() != null) {
            if (this.level.getBlockState(pos).getMaterial().isReplaceable() && this.level.isUnobstructed(LightBlock().defaultBlockState(), pos, ISelectionContext.of(this.getOwner()))) {
                this.level.setBlockAndUpdate(pos, LightBlock().defaultBlockState());
            }
        }
        this.remove();
    }

    public void shootFromRotation(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, velocity, inaccuracy);
        Vector3d vec3d = entityThrower.getLookAngle();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, vec3d.y, vec3d.z));
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vec3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setDeltaMovement(vec3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec3d));
        this.yRot = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.xRot = (float)(MathHelper.atan2(vec3d.y, f) * (double)(180F / (float)Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;

    }

    public abstract IParticleData sourceParticle();

    public abstract IParticleData trailParticle();

    public abstract Block LightBlock();

    @Override
    public abstract EntityType<?> getType();

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return null;
    }

    @Override
    public boolean isNoGravity() {
        return true;
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
}
