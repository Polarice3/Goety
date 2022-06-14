package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class DesiccatedSkullEntity extends DamagingProjectileEntity {
    private static final DataParameter<Boolean> DATA_DANGEROUS = EntityDataManager.defineId(DesiccatedSkullEntity.class, DataSerializers.BOOLEAN);

    public DesiccatedSkullEntity(EntityType<? extends DesiccatedSkullEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public DesiccatedSkullEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(ModEntityType.DESICCATEDSKULL.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public DesiccatedSkullEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(ModEntityType.DESICCATEDSKULL.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
    }

    public void tick() {
        super.tick();
        if (this.getOwner() instanceof IDeadMob) {
            int age = this.isDangerous() ? 100 : 40;
            if (this.tickCount % age == 0) {
                if (!this.level.isClientSide) {
                    DeadSandExplosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner()) ? DeadSandExplosion.Mode.SPREAD : DeadSandExplosion.Mode.NONE;
                    ExplosionUtil.deadSandExplode(this.level, this, this.getX(), this.getY(), this.getZ(), this.isDangerous() ? 1.75F : 1.0F, explosion$mode);
                    this.remove();
                }
            }
        }
        Vector3d vector3d = this.getDeltaMovement();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        IParticleData particleData = new BlockParticleData(ParticleTypes.BLOCK, ModBlocks.DEAD_SAND.get().defaultBlockState());
        this.level.addParticle(particleData, d0 + level.random.nextDouble()/2, d1 + 0.5D, d2 + level.random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
    }
    
    protected float getInertia() {
        return this.isDangerous() ? 0.73F : super.getInertia();
    }
    
    public boolean isOnFire() {
        return false;
    }
    
    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            boolean flag;
            if (!(entity instanceof IDeadMob)) {
                if (entity1 instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) entity1;
                    flag = entity.hurt(ModDamageSource.indirectDesiccate(this, livingentity), 6.0F);
                    if (flag) {
                        if (entity.isAlive()) {
                            this.doEnchantDamageEffects(livingentity, entity);
                        } else {
                            livingentity.heal(5.0F);
                        }
                    }
                } else {
                    flag = entity.hurt(ModDamageSource.DESICCATE, 5.0F);
                }

                if (flag && entity instanceof LivingEntity) {
                    int i;
                    if (this.level.getDifficulty() != Difficulty.HARD) {
                        i = 10;
                    } else {
                        i = 40;
                    }

                    ((LivingEntity) entity).addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 80 * i, 1));
                }
            } else {
                if (entity instanceof LivingEntity){
                    ((LivingEntity) entity).heal(5.0F);
                }
            }

        }
    }
    
    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            DeadSandExplosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner()) ? DeadSandExplosion.Mode.SPREAD : DeadSandExplosion.Mode.NONE;
            ExplosionUtil.deadSandExplode(this.level, this, this.getX(), this.getY(), this.getZ(), this.isDangerous() ? 1.75F : 1.0F, explosion$mode);
            this.remove();
        }

    }
    
    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }
    
    protected void defineSynchedData() {
        this.entityData.define(DATA_DANGEROUS, false);
    }
    
    public boolean isDangerous() {
        return this.entityData.get(DATA_DANGEROUS);
    }

    public void setDangerous(boolean pInvulnerable) {
        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
