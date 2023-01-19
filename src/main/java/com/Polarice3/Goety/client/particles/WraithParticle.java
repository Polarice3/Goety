package com.Polarice3.Goety.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class WraithParticle extends SpriteTexturedParticle {
    private static final Random RANDOM = new Random();
    private final IAnimatedSprite sprites;

    private WraithParticle(ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, IAnimatedSprite pSprites) {
        super(pLevel, pX, pY, pZ, 0.5D - RANDOM.nextDouble(), pYSpeed, 0.5D - RANDOM.nextDouble());
        this.sprites = pSprites;
        this.yd *= (double)0.2F;
        if (pXSpeed == 0.0D && pZSpeed == 0.0D) {
            this.xd *= (double)0.1F;
            this.zd *= (double)0.1F;
        }

        this.quadSize *= 0.75F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.setSpriteFromAge(pSprites);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            this.yd += 0.004D;
            this.move(this.xd, this.yd, this.zd);
            if (this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= (double)0.96F;
            this.yd *= (double)0.96F;
            this.zd *= (double)0.96F;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

        }
    }

    public int getLightColor(float pPartialTick) {
        float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(pPartialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(BasicParticleType pType, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new WraithParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprite);
        }
    }
}
