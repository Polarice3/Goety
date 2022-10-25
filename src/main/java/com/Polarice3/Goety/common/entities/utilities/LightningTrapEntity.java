package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class LightningTrapEntity extends AbstractTrapEntity {

    public LightningTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ParticleTypes.CLOUD);
    }

    public LightningTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.LIGHTNINGTRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    public float radius() {
        return 1.5F;
    }

    public void tick() {
        super.tick();
        if (this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            float f = 1.5F;
            float f5 = (float) Math.PI * f * f;
            for (int k1 = 0; (float) k1 < f5; ++k1) {
                float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;
                serverWorld.sendParticles(this.getParticle(), this.getX() + (double) f8, this.getY() + 0.5F, this.getZ() + (double) f9, 1, 0, 0, 0, 0);
            }
        }
        if (this.tickCount >= this.getDuration()) {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, level);
            lightning.setPos(this.getX(),this.getY(),this.getZ());
            level.addFreshEntity(lightning);
            this.remove();
        }
    }
}
