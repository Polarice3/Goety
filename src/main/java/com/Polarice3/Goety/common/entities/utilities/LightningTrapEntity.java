package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class LightningTrapEntity extends AbstractTrapEntity {

    public LightningTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ParticleTypes.CLOUD);
    }

    public LightningTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.LIGHTNINGTRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        if (this.tickCount >= this.getDuration()) {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, level);
            lightning.setPos(this.getX(),this.getY(),this.getZ());
            level.addFreshEntity(lightning);
            this.remove();
        }
    }
}
