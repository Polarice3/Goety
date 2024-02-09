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

    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_AMBIENT = create("undead_wolf_ambient");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_HURT = create("undead_wolf_hurt");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_GROWL = create("undead_wolf_growl");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_HOWL = create("undead_wolf_howl");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_PANT = create("undead_wolf_pant");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_WHINE = create("undead_wolf_whine");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_STEP = create("undead_wolf_step");
    public static final RegistryObject<SoundEvent> UNDEAD_WOLF_DEATH = create("undead_wolf_death");

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

    public static final RegistryObject<SoundEvent> FALLEN_AMBIENT = create("fallen_ambient");
    public static final RegistryObject<SoundEvent> FALLEN_HURT = create("fallen_hurt");
    public static final RegistryObject<SoundEvent> FALLEN_ANGRY = create("fallen_angry");
    public static final RegistryObject<SoundEvent> FALLEN_DEATH = create("fallen_death");

    public static final RegistryObject<SoundEvent> DESICCATED_AMBIENT = create("desiccated_ambient");
    public static final RegistryObject<SoundEvent> DESICCATED_HURT = create("desiccated_hurt");
    public static final RegistryObject<SoundEvent> DESICCATED_SHOOT = create("desiccated_shoot");
    public static final RegistryObject<SoundEvent> DESICCATED_STEP = create("desiccated_step");
    public static final RegistryObject<SoundEvent> DESICCATED_DEATH = create("desiccated_death");

    public static final RegistryObject<SoundEvent> DUNE_SPIDER_AMBIENT = create("dune_spider_ambient");
    public static final RegistryObject<SoundEvent> DUNE_SPIDER_HURT = create("dune_spider_hurt");
    public static final RegistryObject<SoundEvent> DUNE_SPIDER_STEP = create("dune_spider_step");
    public static final RegistryObject<SoundEvent> DUNE_SPIDER_DEATH = create("dune_spider_death");

    public static final RegistryObject<SoundEvent> BLIGHT_AMBIENT = create("blight_ambient");
    public static final RegistryObject<SoundEvent> BLIGHT_HURT = create("blight_hurt");
    public static final RegistryObject<SoundEvent> BLIGHT_ATTACK = create("blight_attack");
    public static final RegistryObject<SoundEvent> BLIGHT_TELEPORT = create("blight_teleport");
    public static final RegistryObject<SoundEvent> BLIGHT_DEATH = create("blight_death");

    public static final RegistryObject<SoundEvent> MARCIRE_AMBIENT = create("marcire_ambient");
    public static final RegistryObject<SoundEvent> MARCIRE_HURT = create("marcire_hurt");
    public static final RegistryObject<SoundEvent> MARCIRE_ANGRY = create("marcire_angry");
    public static final RegistryObject<SoundEvent> MARCIRE_DEATH = create("marcire_death");

    public static final RegistryObject<SoundEvent> LOCUST_AMBIENT = create("locust_ambient");
    public static final RegistryObject<SoundEvent> LOCUST_HURT = create("locust_hurt");
    public static final RegistryObject<SoundEvent> LOCUST_DEATH = create("locust_death");

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

    public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_AMBIENT = create("frozen_zombie_ambient");
    public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_HURT = create("frozen_zombie_hurt");
    public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_DEATH = create("frozen_zombie_death");

    public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_AMBIENT = create("jungle_zombie_ambient");
    public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_HURT = create("jungle_zombie_hurt");
    public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_STEP = create("jungle_zombie_step");
    public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_DEATH = create("jungle_zombie_death");

    public static final RegistryObject<SoundEvent> MOSSY_SKELETON_AMBIENT = create("mossy_skeleton_ambient");
    public static final RegistryObject<SoundEvent> MOSSY_SKELETON_HURT = create("mossy_skeleton_hurt");
    public static final RegistryObject<SoundEvent> MOSSY_SKELETON_SHOOT = create("mossy_skeleton_shoot");
    public static final RegistryObject<SoundEvent> MOSSY_SKELETON_STEP = create("mossy_skeleton_step");
    public static final RegistryObject<SoundEvent> MOSSY_SKELETON_DEATH = create("mossy_skeleton_death");

    public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_AMBIENT = create("sunken_skeleton_ambient");
    public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_HURT = create("sunken_skeleton_hurt");
    public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_SHOOT = create("sunken_skeleton_shoot");
    public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_STEP = create("sunken_skeleton_step");
    public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_DEATH = create("sunken_skeleton_death");

    public static final RegistryObject<SoundEvent> HOGLORD_AMBIENT = create("hoglord_ambient");
    public static final RegistryObject<SoundEvent> HOGLORD_HURT = create("hoglord_hurt");
    public static final RegistryObject<SoundEvent> HOGLORD_RAGE = create("hoglord_rage");
    public static final RegistryObject<SoundEvent> HOGLORD_SUMMON = create("hoglord_summon");
    public static final RegistryObject<SoundEvent> HOGLORD_DEATH = create("hoglord_death");

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
    public static final RegistryObject<SoundEvent> FIRE_BREATH_START = create("fire_breath_start");
    public static final RegistryObject<SoundEvent> FIRE_BREATH = create("fire_breath");
    public static final RegistryObject<SoundEvent> PREPARE_SPELL = create("prepare_spell");
    public static final RegistryObject<SoundEvent> CAST_SPELL = create("cast_spell");
    public static final RegistryObject<SoundEvent> SUMMON_SPELL = create("summon_spell");
    public static final RegistryObject<SoundEvent> WIND = create("wind");
    public static final RegistryObject<SoundEvent> SOUL_EAT = create("soul_eat");
    public static final RegistryObject<SoundEvent> SONIC_CHARGE = create("sonic_charge");
    public static final RegistryObject<SoundEvent> FIRE_TORNADO_AMBIENT = create("fire_tornado_ambient");
    public static final RegistryObject<SoundEvent> RUMBLE = create("rumble");
    public static final RegistryObject<SoundEvent> SPELL_FAIL = create("spell_fail");
    public static final RegistryObject<SoundEvent> CORRUPT_EXPLOSION = create("corrupt_explosion");

    public static final RegistryObject<SoundEvent> SCYTHE_SWING = create("scythe_swing");
    public static final RegistryObject<SoundEvent> SCYTHE_HIT = create("scythe_hit");
    public static final RegistryObject<SoundEvent> SCYTHE_HIT_MEATY = create("scythe_hit_meaty");

    public static final RegistryObject<SoundEvent> HARPOON_HIT = create("harpoon_impact");
    public static final RegistryObject<SoundEvent> HARPOON_HIT_WATER = create("harpoon_impact_water");

    public static final RegistryObject<SoundEvent> FLAME_CAPTURE_CATCH = create("flame_capture_catch");
    public static final RegistryObject<SoundEvent> FLAME_CAPTURE_RELEASE = create("flame_capture_release");

    public static final RegistryObject<SoundEvent> FOCUS_PICK = create("focus_pick");

    public static final RegistryObject<SoundEvent> ALTAR_START = create("altar_start");
    public static final RegistryObject<SoundEvent> ALTAR_LOOP = create("altar_loop");
    public static final RegistryObject<SoundEvent> ALTAR_FINISH = create("altar_finish");

    public static final RegistryObject<SoundEvent> APOSTLE_THEME = create("apostle_theme");
    public static final RegistryObject<SoundEvent> APOSTLE_THEME_POST = create("apostle_theme_post");
    public static final RegistryObject<SoundEvent> VIZIER_THEME = create("vizier_theme");

    public static final RegistryObject<SoundEvent> BOSS_POST = create("boss_post");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_APOSTLE = create("apostle_theme_disc");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_VIZIER = create("vizier_theme_disc");

    static RegistryObject<SoundEvent> create(String name) {
        SoundEvent event = new SoundEvent(Goety.location(name));
        return SOUNDS.register(name, () -> event);
    }
}
