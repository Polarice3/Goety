package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Goety.MOD_ID);

    public static void init(){
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> APOSTLE_AMBIENT = create("apostle_ambient");
    public static final RegistryObject<SoundEvent> APOSTLE_HURT = create("apostle_hurt");
    public static final RegistryObject<SoundEvent> APOSTLE_PREPARE_SPELL = create("apostle_prepare_spell");
    public static final RegistryObject<SoundEvent> APOSTLE_PREPARE_SUMMON = create("apostle_prepare_summon");
    public static final RegistryObject<SoundEvent> APOSTLE_CAST_SPELL = create("apostle_cast_spell");
    public static final RegistryObject<SoundEvent> APOSTLE_TELEPORT = create("apostle_teleport");
    public static final RegistryObject<SoundEvent> APOSTLE_DEATH = create("apostle_death");

    public static final RegistryObject<SoundEvent> SKULL_LORD_AMBIENT = create("skull_lord_ambient");
    public static final RegistryObject<SoundEvent> SKULL_LORD_HURT = create("skull_lord_hurt");
    public static final RegistryObject<SoundEvent> SKULL_LORD_CHARGE = create("skull_lord_charge");
    public static final RegistryObject<SoundEvent> SKULL_LORD_SHOOT = create("skull_lord_shoot");
    public static final RegistryObject<SoundEvent> SKULL_LORD_LASER_BEGIN = create("skull_lord_laser_begin");
    public static final RegistryObject<SoundEvent> SKULL_LORD_LASER_START = create("skull_lord_laser_start");
    public static final RegistryObject<SoundEvent> SKULL_LORD_DEATH = create("skull_lord_death");

    public static final RegistryObject<SoundEvent> TORMENTOR_AMBIENT = create("tormentor_ambient");
    public static final RegistryObject<SoundEvent> TORMENTOR_HURT = create("tormentor_hurt");
    public static final RegistryObject<SoundEvent> TORMENTOR_CHARGE = create("tormentor_charge");
    public static final RegistryObject<SoundEvent> TORMENTOR_CELEBRATE = create("tormentor_celebrate");
    public static final RegistryObject<SoundEvent> TORMENTOR_DEATH = create("tormentor_death");

    public static final RegistryObject<SoundEvent> INQUILLAGER_AMBIENT = create("inquillager_ambient");
    public static final RegistryObject<SoundEvent> INQUILLAGER_HURT = create("inquillager_hurt");
    public static final RegistryObject<SoundEvent> INQUILLAGER_CELEBRATE = create("inquillager_celebrate");
    public static final RegistryObject<SoundEvent> INQUILLAGER_DEATH = create("inquillager_death");

    public static final RegistryObject<SoundEvent> CONQUILLAGER_AMBIENT = create("conquillager_ambient");
    public static final RegistryObject<SoundEvent> CONQUILLAGER_HURT = create("conquillager_hurt");
    public static final RegistryObject<SoundEvent> CONQUILLAGER_CELEBRATE = create("conquillager_celebrate");
    public static final RegistryObject<SoundEvent> CONQUILLAGER_DEATH = create("conquillager_death");

    public static final RegistryObject<SoundEvent> URBHADHACH_AMBIENT = create("urbhadhach_ambient");
    public static final RegistryObject<SoundEvent> URBHADHACH_HURT = create("urbhadhach_hurt");
    public static final RegistryObject<SoundEvent> URBHADHACH_ROAR = create("urbhadhach_roar");
    public static final RegistryObject<SoundEvent> URBHADHACH_STRONG_ROAR = create("urbhadhach_strong_roar");
    public static final RegistryObject<SoundEvent> URBHADHACH_ATTACK = create("urbhadhach_attack");
    public static final RegistryObject<SoundEvent> URBHADHACH_STEP = create("urbhadhach_step");
    public static final RegistryObject<SoundEvent> URBHADHACH_DEATH = create("urbhadhach_death");

    public static final RegistryObject<SoundEvent> ROAR_SPELL = create("roar_spell");
    public static final RegistryObject<SoundEvent> FIRE_BREATH = create("fire_breath");
    public static final RegistryObject<SoundEvent> SONIC_CHARGE = create("sonic_charge");
    public static final RegistryObject<SoundEvent> FIRE_TORNADO_AMBIENT = create("fire_tornado_ambient");
    public static final RegistryObject<SoundEvent> CORRUPT_EXPLOSION = create("corrupt_explosion");

    static RegistryObject<SoundEvent> create(String name) {
        SoundEvent event = new SoundEvent(Goety.location(name));
        return SOUNDS.register(name, () -> event);
    }
}
