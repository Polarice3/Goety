package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class WitchBombEntity extends ProjectileItemEntity {
    public WitchBombEntity(EntityType<? extends WitchBombEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public WitchBombEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntityType.WITCHBOMB.get(), throwerIn, worldIn);
    }

    public WitchBombEntity(World worldIn, double x, double y, double z) {
        super(ModEntityType.WITCHBOMB.get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return ModRegistry.WITCHBOMB.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        return ParticleTypes.SMOKE;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
    }

    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.Mode.NONE);
            this.remove();
        }

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
