package com.Polarice3.Goety.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SonicBoomParticle extends LEParticle {

    protected SonicBoomParticle(ClientWorld pLevel, double pX, double pY, double pZ, double pQuadSizeMulitiplier, IAnimatedSprite pSprites) {
        super(pLevel, pX, pY, pZ, pQuadSizeMulitiplier, pSprites);
        this.lifetime = 16;
        this.quadSize = 1.5F;
        this.setSpriteFromAge(pSprites);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite p_234036_) {
            this.sprites = p_234036_;
        }

        public Particle createParticle(BasicParticleType pType, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SonicBoomParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites);
        }
    }
}