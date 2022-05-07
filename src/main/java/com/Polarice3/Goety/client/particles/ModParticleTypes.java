package com.Polarice3.Goety.client.particles;

import com.Polarice3.Goety.Goety;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.LargeExplosionParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticleTypes {
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Goety.MOD_ID);

    public static final RegistryObject<BasicParticleType> TOTEM_EFFECT = PARTICLE_TYPES.register("totem_effect",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> PLAGUE_EFFECT = PARTICLE_TYPES.register("plague_effect",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> HEAL_EFFECT = PARTICLE_TYPES.register("heal",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> BULLET_EFFECT = PARTICLE_TYPES.register("bullet_effect",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> DEAD_SAND_EXPLOSION = PARTICLE_TYPES.register("deadsandsplosion",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> DEAD_SAND_EXPLOSION_EMITTER = PARTICLE_TYPES.register("deadsandsplosion_emitter",
            () -> new BasicParticleType(true));

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        ParticleManager particles = Minecraft.getInstance().particleEngine;

        particles.register(ModParticleTypes.TOTEM_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.PLAGUE_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.BULLET_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.HEAL_EFFECT.get(), HeartParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), LargeExplosionParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION_EMITTER.get(), new HugeDSEParticle.Factory());
    }
}
