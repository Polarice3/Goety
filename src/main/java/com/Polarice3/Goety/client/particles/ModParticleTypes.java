package com.Polarice3.Goety.client.particles;

import com.Polarice3.Goety.Goety;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static final RegistryObject<BasicParticleType> SOUL_LIGHT_EFFECT = PARTICLE_TYPES.register("soul_light",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> GLOW_EFFECT = PARTICLE_TYPES.register("glow_trail",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> GLOW_LIGHT_EFFECT = PARTICLE_TYPES.register("glow_light",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> DEAD_SAND_EXPLOSION = PARTICLE_TYPES.register("deadsandsplosion",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> DEAD_SAND_EXPLOSION_EMITTER = PARTICLE_TYPES.register("deadsandsplosion_emitter",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> LASER_GATHER = PARTICLE_TYPES.register("laser",
            () -> new BasicParticleType(true));
}
