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
    public static final RegistryObject<SoundEvent> APOSTLE_PREDEATH = create("apostle_predeath");
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

    public static final RegistryObject<SoundEvent> FANATIC_AMBIENT = create("fanatic_ambient");
    public static final RegistryObject<SoundEvent> FANATIC_HURT = create("fanatic_hurt");
    public static final RegistryObject<SoundEvent> FANATIC_CELEBRATE = create("fanatic_celebrate");
    public static final RegistryObject<SoundEvent> FANATIC_DEATH = create("fanatic_death");

    public static final RegistryObject<SoundEvent> ZEALOT_AMBIENT = create("zealot_ambient");
    public static final RegistryObject<SoundEvent> ZEALOT_HURT = create("zealot_hurt");
    public static final RegistryObject<SoundEvent> ZEALOT_CELEBRATE = create("zealot_celebrate");
    public static final RegistryObject<SoundEvent> ZEALOT_DEATH = create("zealot_death");

    public static final RegistryObject<SoundEvent> DISCIPLE_AMBIENT = create("disciple_ambient");
    public static final RegistryObject<SoundEvent> DISCIPLE_HURT = create("disciple_hurt");
    public static final RegistryObject<SoundEvent> DISCIPLE_CELEBRATE = create("disciple_celebrate");
    public static final RegistryObject<SoundEvent> DISCIPLE_DEATH = create("disciple_death");

    public static final RegistryObject<SoundEvent> CHANNELLER_AMBIENT = create("channeller_ambient");
    public static final RegistryObject<SoundEvent> CHANNELLER_HURT = create("channeller_hurt");
    public static final RegistryObject<SoundEvent> CHANNELLER_CELEBRATE = create("channeller_celebrate");
    public static final RegistryObject<SoundEvent> CHANNELLER_DEATH = create("channeller_death");

    public static final RegistryObject<SoundEvent> THUG_AMBIENT = create("thug_ambient");
    public static final RegistryObject<SoundEvent> THUG_HURT = create("thug_hurt");
    public static final RegistryObject<SoundEvent> THUG_STEP = create("thug_step");
    public static final RegistryObject<SoundEvent> THUG_CELEBRATE = create("thug_celebrate");
    public static final RegistryObject<SoundEvent> THUG_DEATH = create("thug_death");

    public static final RegistryObject<SoundEvent> CULTIST_PREPARE_SPELL = create("cultist_prepare_spell");
    public static final RegistryObject<SoundEvent> CULTIST_CAST_SPELL = create("cultist_cast_spell");

    public static final RegistryObject<SoundEvent> URBHADHACH_AMBIENT = create("urbhadhach_ambient");
    public static final RegistryObject<SoundEvent> URBHADHACH_HURT = create("urbhadhach_hurt");
    public static final RegistryObject<SoundEvent> URBHADHACH_ROAR = create("urbhadhach_roar");
    public static final RegistryObject<SoundEvent> URBHADHACH_STRONG_ROAR = create("urbhadhach_strong_roar");
    public static final RegistryObject<SoundEvent> URBHADHACH_ATTACK = create("urbhadhach_attack");
    public static final RegistryObject<SoundEvent> URBHADHACH_STEP = create("urbhadhach_step");
    public static final RegistryObject<SoundEvent> URBHADHACH_DEATH = create("urbhadhach_death");

    public static final RegistryObject<SoundEvent> FEL_FLY_LOOP = create("fel_fly_loop");
    public static final RegistryObject<SoundEvent> FEL_FLY_HURT = create("fel_fly_hurt");
    public static final RegistryObject<SoundEvent> FEL_FLY_DEATH = create("fel_fly_death");

    public static final RegistryObject<SoundEvent> ROT_TREE_AMBIENT = create("rot_tree_ambient");
    public static final RegistryObject<SoundEvent> ROT_TREE_ATTACK = create("rot_tree_attack");
    public static final RegistryObject<SoundEvent> ROT_TREE_HURT = create("rot_tree_hurt");
    public static final RegistryObject<SoundEvent> ROT_TREE_HEAVY_HURT = create("rot_tree_heavyhurt");
    public static final RegistryObject<SoundEvent> ROT_TREE_REPAIR = create("rot_tree_repair");
    public static final RegistryObject<SoundEvent> ROT_TREE_ROT_REPAIR = create("rot_tree_rot_repair");
    public static final RegistryObject<SoundEvent> ROT_TREE_ROOTS = create("rot_tree_roots");
    public static final RegistryObject<SoundEvent> ROT_TREE_STEP = create("rot_tree_step");
    public static final RegistryObject<SoundEvent> ROT_TREE_DEATH = create("rot_tree_death");
    public static final RegistryObject<SoundEvent> ROT_TREE_ENTER = create("rot_tree_enter");
    public static final RegistryObject<SoundEvent> ROT_TREE_EXIT = create("rot_tree_exit");

    public static final RegistryObject<SoundEvent> HUSKARL_AMBIENT = create("huskarl_ambient");
    public static final RegistryObject<SoundEvent> HUSKARL_HURT = create("huskarl_hurt");
    public static final RegistryObject<SoundEvent> HUSKARL_DEATH = create("huskarl_death");

    public static final RegistryObject<SoundEvent> DREDEN_AMBIENT = create("dreden_ambient");
    public static final RegistryObject<SoundEvent> DREDEN_HURT = create("dreden_hurt");
    public static final RegistryObject<SoundEvent> DREDEN_FLY = create("dreden_fly");
    public static final RegistryObject<SoundEvent> DREDEN_SHOOT = create("dreden_shoot");
    public static final RegistryObject<SoundEvent> DREDEN_DEATH = create("dreden_death");

    public static final RegistryObject<SoundEvent> WRAITH_AMBIENT = create("wraith_ambient");
    public static final RegistryObject<SoundEvent> WRAITH_HURT = create("wraith_hurt");
    public static final RegistryObject<SoundEvent> WRAITH_FLY = create("wraith_fly");
    public static final RegistryObject<SoundEvent> WRAITH_ATTACK = create("wraith_attack");
    public static final RegistryObject<SoundEvent> WRAITH_FIRE = create("wraith_fire");
    public static final RegistryObject<SoundEvent> WRAITH_TELEPORT = create("wraith_teleport");
    public static final RegistryObject<SoundEvent> WRAITH_DEATH = create("wraith_death");

    public static final RegistryObject<SoundEvent> PIT_WRAITH_AMBIENT = create("pit_wraith_ambient");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_HURT = create("pit_wraith_hurt");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_FLY = create("pit_wraith_fly");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_ATTACK = create("pit_wraith_attack");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_TELEPORT_IN = create("pit_wraith_teleport_in");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_TELEPORT_OUT = create("pit_wraith_teleport_out");
    public static final RegistryObject<SoundEvent> PIT_WRAITH_DEATH = create("pit_wraith_death");

    public static final RegistryObject<SoundEvent> VIZIER_AMBIENT = create("vizier_ambient");
    public static final RegistryObject<SoundEvent> VIZIER_HURT = create("vizier_hurt");
    public static final RegistryObject<SoundEvent> VIZIER_CONFUSE = create("vizier_confuse");
    public static final RegistryObject<SoundEvent> VIZIER_RAGE = create("vizier_rage");
    public static final RegistryObject<SoundEvent> VIZIER_CELEBRATE = create("vizier_celebrate");
    public static final RegistryObject<SoundEvent> VIZIER_SCREAM = create("vizier_scream");
    public static final RegistryObject<SoundEvent> VIZIER_DEATH = create("vizier_death");

    public static final RegistryObject<SoundEvent> ICE_CHUNK_IDLE = create("ice_chunk_idle");
    public static final RegistryObject<SoundEvent> ICE_CHUNK_SUMMON = create("ice_chunk_summon");
    public static final RegistryObject<SoundEvent> ICE_CHUNK_HIT = create("ice_chunk_hit");

    public static final RegistryObject<SoundEvent> EVIL_LAUGH = create("evil_laugh");
    public static final RegistryObject<SoundEvent> ROAR_SPELL = create("roar_spell");
    public static final RegistryObject<SoundEvent> FIRE_BREATH = create("fire_breath");
    public static final RegistryObject<SoundEvent> PREPARE_SPELL = create("prepare_spell");
    public static final RegistryObject<SoundEvent> CAST_SPELL = create("cast_spell");
    public static final RegistryObject<SoundEvent> SUMMON_SPELL = create("summon_spell");
    public static final RegistryObject<SoundEvent> SOUL_EAT = create("soul_eat");
    public static final RegistryObject<SoundEvent> SONIC_CHARGE = create("sonic_charge");
    public static final RegistryObject<SoundEvent> FIRE_TORNADO_AMBIENT = create("fire_tornado_ambient");
    public static final RegistryObject<SoundEvent> CORRUPT_EXPLOSION = create("corrupt_explosion");

    public static final RegistryObject<SoundEvent> APOSTLE_THEME = create("apostle_theme");
    public static final RegistryObject<SoundEvent> APOSTLE_THEME_POST = create("apostle_theme_post");
    public static final RegistryObject<SoundEvent> VIZIER_THEME = create("vizier_theme");

    public static final RegistryObject<SoundEvent> BOSS_POST = create("boss_post");

    static RegistryObject<SoundEvent> create(String name) {
        SoundEvent event = new SoundEvent(Goety.location(name));
        return SOUNDS.register(name, () -> event);
    }
}
