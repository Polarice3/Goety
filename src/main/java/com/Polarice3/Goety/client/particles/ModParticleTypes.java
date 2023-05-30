package com.Polarice3.Goety.client.particles;

import com.Polarice3.Goety.Goety;
import com.mojang.serialization.Codec;
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

    public static final RegistryObject<BasicParticleType> SONIC_GATHER = PARTICLE_TYPES.register("sonic",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> FLAME_GATHER = PARTICLE_TYPES.register("flame_gather",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> POISON = PARTICLE_TYPES.register("poison",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> BURNING = PARTICLE_TYPES.register("burning",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> CULT_SPELL = PARTICLE_TYPES.register("cult_spell",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> SONIC_BOOM = PARTICLE_TYPES.register("sonic_boom",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> CONFUSED = PARTICLE_TYPES.register("confused",
            () -> new BasicParticleType(true));

    public static final RegistryObject<BasicParticleType> WHITE_EFFECT = PARTICLE_TYPES.register("white_effect",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> WRAITH = PARTICLE_TYPES.register("wraith",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> WRAITH_BURST = PARTICLE_TYPES.register("wraith_burst",
            () -> new BasicParticleType(false));

    public static final RegistryObject<BasicParticleType> LEECH = PARTICLE_TYPES.register("leech",
            () -> new BasicParticleType(false));

    public static final RegistryObject<ParticleType<ShockwaveParticleOption>> SHOCKWAVE = PARTICLE_TYPES.register("shockwave",
            () -> new ParticleType<ShockwaveParticleOption>(false, ShockwaveParticleOption.DESERIALIZER) {
                @Override
                public Codec codec() {
                    return ShockwaveParticleOption.CODEC;
                }
            });
}
