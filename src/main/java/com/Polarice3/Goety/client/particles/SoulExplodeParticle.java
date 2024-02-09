package com.Polarice3.Goety.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class SoulExplodeParticle extends SpriteTexturedParticle {
    SoulExplodeParticle(ClientWorld p_105733_, double p_105734_, double p_105735_, double p_105736_, double p_105737_, double p_105738_, double p_105739_) {
        super(p_105733_, p_105734_, p_105735_, p_105736_);
        this.gravity = -0.25F;
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 1.2F + 0.2F;
        this.xd = p_105737_ * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.yd = p_105738_ * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.zd = p_105739_ * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.lifetime = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void tick() {
        super.tick();
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getLightColor(float p_106821_) {
        return 255;
    }

    public static class Provider implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Provider(IAnimatedSprite p_105753_) {
            this.sprite = p_105753_;
        }

        public Particle createParticle(BasicParticleType p_105764_, ClientWorld p_105765_, double p_105766_, double p_105767_, double p_105768_, double p_105769_, double p_105770_, double p_105771_) {
            SoulExplodeParticle bubblecolumnupparticle = new SoulExplodeParticle(p_105765_, p_105766_, p_105767_, p_105768_, p_105769_, p_105770_, p_105771_);
            bubblecolumnupparticle.pickSprite(this.sprite);
            return bubblecolumnupparticle;
        }
    }

    public static class SummonProvider implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public SummonProvider(IAnimatedSprite p_105753_) {
            this.sprite = p_105753_;
        }

        public Particle createParticle(BasicParticleType p_105764_, ClientWorld p_105765_, double p_105766_, double p_105767_, double p_105768_, double p_105769_, double p_105770_, double p_105771_) {
            SoulExplodeParticle bubblecolumnupparticle = new SoulExplodeParticle(p_105765_, p_105766_, p_105767_, p_105768_, p_105769_, p_105770_, p_105771_);
            bubblecolumnupparticle.pickSprite(this.sprite);
            bubblecolumnupparticle.scale(0.5F);
            bubblecolumnupparticle.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
            return bubblecolumnupparticle;
        }
    }
}
