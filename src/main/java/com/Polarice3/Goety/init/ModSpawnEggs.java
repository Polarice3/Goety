package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSpawnEggs {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety.MOD_ID);

    public static final RegistryObject<ModSpawnEggItem> CHANNELLER_SPAWN_EGG = ITEMS.register("channeller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CHANNELLER, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FANATIC_SPAWN_EGG = ITEMS.register("fanatic_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FANATIC, 0x5a0b0b, 0xc7b46b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZEALOT_SPAWN_EGG = ITEMS.register("zealot_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZEALOT, 0x940f0f, 0x240505, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> THUG_SPAWN_EGG = ITEMS.register("thug_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.THUG, 0x241c1c, 0x141010, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CRIMSON_SPIDER_SPAWN_EGG = ITEMS.register("crimson_spider_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CRIMSON_SPIDER, 0x9a0000, 0xe6c550, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DISCIPLE_SPAWN_EGG = ITEMS.register("disciple_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DISCIPLE, 0x5a0b0b, 0x917186, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BELDAM_SPAWN_EGG = ITEMS.register("beldam_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BELDAM, 0x340000, 0x161616, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> APOSTLE_SPAWN_EGG = ITEMS.register("apostle_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.APOSTLE, 0x080808, 0xf5da2a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HOGLORD_SPAWN_EGG = ITEMS.register("hoglord_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HOGLORD, 0x802a2d, 0xdac88c, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ENVIOKER_SPAWN_EGG = ITEMS.register("envioker_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ENVIOKER, 0x1e1c1a, 0xca272a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> INQUILLAGER_SPAWN_EGG = ITEMS.register("inquillager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.INQUILLAGER, 0xbca341, 0x5c121b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CONQUILLAGER_SPAWN_EGG = ITEMS.register("conquillager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CONQUILLAGER, 0xd3d3d3, 0x0f0f0f, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TORMENTOR_SPAWN_EGG = ITEMS.register("tormentor_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TORMENTOR, 0x89a1b8, 0x657787, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BOOMER_SPAWN_EGG = ITEMS.register("boomer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BOOMER, 0x423b42, 0x616061, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DUNE_SPIDER_SPAWN_EGG = ITEMS.register("dune_spider_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DUNE_SPIDER, 0x525144, 0x6d6d5a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FALLEN_SPAWN_EGG = ITEMS.register("fallen_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FALLEN, 0x9b8a7d, 0x422230, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DESICCATED_SPAWN_EGG = ITEMS.register("desiccated_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DESICCATED, 0x652f76, 0x3e2625, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BLIGHT_SPAWN_EGG = ITEMS.register("blight_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BLIGHT, 0x1f1f1f, 0x372637, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> MARCIRE_SPAWN_EGG = ITEMS.register("marcire_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.MARCIRE, 0x652f76, 0x786659, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> LOCUST_SPAWN_EGG = ITEMS.register("locust_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.LOCUST, 0x563c5a, 0x300e28, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> VIZIER_SPAWN_EGG = ITEMS.register("vizier_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.VIZIER, 0x1e1c1a, 0x440a67, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SACRED_FISH_SPAWN_EGG = ITEMS.register("sacredfish_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SACRED_FISH, 0xfacb32, 0xfff34d, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PARASITE_SPAWN_EGG = ITEMS.register("parasite_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PARASITE, 0xffc975, 0xffb541, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> IRK_SPAWN_EGG = ITEMS.register("irk_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.IRK, 8032420, 8032420, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SCORCH_SPAWN_EGG = ITEMS.register("scorch_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SCORCH, 0x3b1414, 0xf48522, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SKULL_LORD_SPAWN_EGG = ITEMS.register("skull_lord_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SKULL_LORD, 0xd3d3d3, 0x74f1f5, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SENTINEL_SPAWN_EGG = ITEMS.register("sentinel_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SENTINEL, 0x100606, 0xfaeb72, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZOMBIE_MINION_SPAWN_EGG = ITEMS.register("zombie_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZOMBIE_MINION, 0x192927, 0x737885, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HUSK_MINION_SPAWN_EGG = ITEMS.register("husk_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HUSK_MINION, 0x322921, 0x64492a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DROWNED_MINION_SPAWN_EGG = ITEMS.register("drowned_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DROWNED_MINION, 0x182d37, 0x2f8209, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FARMER_MINION_SPAWN_EGG = ITEMS.register("farmer_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FARMER_MINION, 0x5e4236, 0xdbc549, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SKELETON_MINION_SPAWN_EGG = ITEMS.register("skeleton_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SKELETON_MINION, 0x1f1f1f, 0x6e6473, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> STRAY_MINION_SPAWN_EGG = ITEMS.register("stray_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.STRAY_MINION, 0x495959, 0xb3d4e3, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DREDEN_MINION_SPAWN_EGG = ITEMS.register("dreden_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DREDEN_MINION, 0x4288ae, 0x182022, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> WRAITH_MINION_SPAWN_EGG = ITEMS.register("wraith_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.WRAITH_MINION, 0x0e0d36, 0x2586d9, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZPIGLIN_MINION_SPAWN_EGG = ITEMS.register("zpiglin_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZPIGLIN_MINION, 0x594036, 0xf5da2a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZPIGLIN_BRUTE_MINION_SPAWN_EGG = ITEMS.register("zpiglin_brute_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZPIGLIN_BRUTE_MINION, 0x1c1c1c, 0xf5da2a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> UNDEAD_WOLF_SPAWN_EGG = ITEMS.register("undead_wolf_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.UNDEAD_WOLF_MINION, 0xbdbdbd, 0x832527, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SPIDERLING_MINION_SPAWN_EGG = ITEMS.register("spiderling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SPIDERLING_MINION, 0xc18a34, 0x3c0202, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CREEPERLING_MINION_SPAWN_EGG = ITEMS.register("creeperling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CREEPERLING_MINION, 0x1c4c15, 0x000000, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FEL_FLY_SPAWN_EGG = ITEMS.register("fel_fly_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FEL_FLY, 0x242415, 0x3f4223, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HUSKARL_SPAWN_EGG = ITEMS.register("huskarl_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HUSKARL, 0x61a8ad, 0x446466, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SHADE_SPAWN_EGG = ITEMS.register("shade_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SHADE, 0x002e2e, 0xc5d6d5, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DREDEN_SPAWN_EGG = ITEMS.register("dreden_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DREDEN, 0x3f6170, 0x7bb8d4, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> WRAITH_SPAWN_EGG = ITEMS.register("wraith_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.WRAITH, 0x16215c, 0x82d8f8, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> URBHADHACH_SPAWN_EGG = ITEMS.register("urbhadhach_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.URBHADHACH, 0x484848, 0x1c302f, new Item.Properties().tab(Goety.TAB)));
}
