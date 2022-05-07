package com.Polarice3.Goety.client.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.MetaParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HugeDSEParticle extends MetaParticle {
    private int life;
    private final int lifeTime = 8;

    private HugeDSEParticle(ClientWorld p_i232398_1_, double p_i232398_2_, double p_i232398_4_, double p_i232398_6_) {
        super(p_i232398_1_, p_i232398_2_, p_i232398_4_, p_i232398_6_, 0.0D, 0.0D, 0.0D);
    }

    public void tick() {
        for(int i = 0; i < 6; ++i) {
            double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            this.level.addParticle(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), d0, d1, d2, (double)((float)this.life / (float)this.lifeTime), 0.0D, 0.0D);
        }

        ++this.life;
        if (this.life == this.lifeTime) {
            this.remove();
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        public Particle createParticle(BasicParticleType pType, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new HugeDSEParticle(pLevel, pX, pY, pZ);
        }
    }
}
