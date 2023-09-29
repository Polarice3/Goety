package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class DeathArrowEntity extends ArrowEntity {

    public DeathArrowEntity(EntityType<? extends ArrowEntity> p_36721_, World p_36722_) {
        super(p_36721_, p_36722_);
    }

    public DeathArrowEntity(World p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityType.DEATH_ARROW.get();
    }

    protected void doPostHurtEffects(LivingEntity p_36873_) {
        super.doPostHurtEffects(p_36873_);

        if (p_36873_ instanceof ApostleEntity && p_36873_.level.dimension() == World.NETHER) {
            float voidDamage = p_36873_.getMaxHealth() * 0.05F;

            if (p_36873_.getHealth() > voidDamage + 1.0F) {
                p_36873_.heal(-voidDamage);
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult p_37260_) {
        super.onHit(p_37260_);
        if (!this.level.isClientSide) {
            ServerWorld serverLevel = (ServerWorld) this.level;
            if (!this.inGround) {
                for (int p = 0; p < 32; ++p) {
                    double d0 = (double) this.getX() + this.level.random.nextDouble();
                    double d1 = (double) this.getY() + this.level.random.nextDouble();
                    double d2 = (double) this.getZ() + this.level.random.nextDouble();
                    serverLevel.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 0.5F);
                }
            }
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null && (this.getOwner().isAlliedTo(pEntity) || pEntity.isAlliedTo(this.getOwner()))){
            return false;
        } else {
            return super.canHitEntity(pEntity);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
