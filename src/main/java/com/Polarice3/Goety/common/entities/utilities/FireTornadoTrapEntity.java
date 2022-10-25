package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.common.entities.projectiles.FireTornadoEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class FireTornadoTrapEntity extends AbstractTrapEntity {

    public FireTornadoTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ParticleTypes.LARGE_SMOKE);
    }

    public FireTornadoTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.FIRETORNADOTRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        if (this.tickCount >= this.getDuration()) {
            if (this.owner != null) {
                if (this.owner instanceof MobEntity) {
                    MobEntity owner = (MobEntity) this.owner;
                    LivingEntity livingentity = owner.getTarget();
                    if (livingentity != null) {
                        double d1 = livingentity.getX() - this.getX();
                        double d2 = livingentity.getY(0.5D) - this.getY(0.5D);
                        double d3 = livingentity.getZ() - this.getZ();
                        FireTornadoEntity fireTornadoEntity = new FireTornadoEntity(this.level, this.owner, d1, d2, d3);
                        fireTornadoEntity.setOwnerId(this.owner.getUUID());
                        fireTornadoEntity.setTotallife(1200);
                        fireTornadoEntity.setPos(this.getX(), this.getY(), this.getZ());
                        this.level.addFreshEntity(fireTornadoEntity);
                        this.remove();
                    } else {
                        this.remove();
                    }
                } else {
                    FireTornadoEntity fireTornadoEntity = new FireTornadoEntity(this.level, this.owner, 0, 0, 0);
                    fireTornadoEntity.setOwnerId(this.owner.getUUID());
                    fireTornadoEntity.setTotallife(1200);
                    fireTornadoEntity.setPos(this.getX(), this.getY(), this.getZ());
                    this.level.addFreshEntity(fireTornadoEntity);
                    this.remove();
                }
            } else {
                FireTornadoEntity fireTornadoEntity = new FireTornadoEntity(ModEntityType.FIRETORNADO.get(), this.level);
                fireTornadoEntity.setTotallife(1200);
                fireTornadoEntity.setPos(this.getX(), this.getY(), this.getZ());
                this.level.addFreshEntity(fireTornadoEntity);
                this.remove();
            }
        }
    }

}