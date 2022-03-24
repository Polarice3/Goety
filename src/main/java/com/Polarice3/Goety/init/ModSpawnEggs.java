package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSpawnEggs {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety.MOD_ID);

    public static final RegistryObject<ModSpawnEggItem> TANK_SPAWN_EGG = ITEMS.register("tank_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TANK, 0x777777, 0x3c3b3b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CHANNELLER_SPAWN_EGG = ITEMS.register("channeller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CHANNELLER, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FANATIC_SPAWN_EGG = ITEMS.register("fanatic_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FANATIC, 0x5a0b0b, 0xc7b46b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZEALOT_SPAWN_EGG = ITEMS.register("zealot_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZEALOT, 0x5a0b0b, 0x240505, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> THUG_SPAWN_EGG = ITEMS.register("thug_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.THUG, 0x5a0b0b, 0x241c1c, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CRIMSON_SPIDER_SPAWN_EGG = ITEMS.register("crimson_spider_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CRIMSON_SPIDER, 0x9a0000, 0xe6c550, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DISCIPLE_SPAWN_EGG = ITEMS.register("disciple_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DISCIPLE, 0x5a0b0b, 0x917186, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> APOSTLE_SPAWN_EGG = ITEMS.register("apostle_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.APOSTLE, 0x5a0b0b, 0x66505f, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ENVIOKER_SPAWN_EGG = ITEMS.register("envioker_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ENVIOKER, 0x1e1c1a, 0xca272a, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> INQUILLAGER_SPAWN_EGG = ITEMS.register("inquillager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.INQUILLAGER, 0x5c121b, 0x5c121b, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CONQUILLAGER_SPAWN_EGG = ITEMS.register("conquillager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CONQUILLAGER, 0xd3d3d3, 0x0f0f0f, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TORMENTOR_SPAWN_EGG = ITEMS.register("tormentor_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TORMENTOR, 0x89a1b8, 0x657787, new Item.Properties().tab(Goety.TAB)));

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

    public static final RegistryObject<ModSpawnEggItem> NETHERNAL_SPAWN_EGG = ITEMS.register("nethernal_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.NETHERNAL, 0x100606, 0xfaeb72, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZOMBIE_MINION_SPAWN_EGG = ITEMS.register("zombie_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZOMBIE_MINION, 0x192927, 0x737885, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SKELETON_MINION_SPAWN_EGG = ITEMS.register("skeleton_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SKELETON_MINION, 0x1f1f1f, 0x6e6473, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SPIDERLING_MINION_SPAWN_EGG = ITEMS.register("spiderling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SPIDERLING_MINION, 0xc18a34, 0x3c0202, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CREEPERLING_MINION_SPAWN_EGG = ITEMS.register("creeperling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CREEPERLING_MINION, 0x1c4c15, 0x000000, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HUSKARL_SPAWN_EGG = ITEMS.register("huskarl_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HUSKARL, 0x61a8ad, 0x446466, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PENANCE_SPAWN_EGG = ITEMS.register("penance_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PENANCE, 0x2c2c2c, 8032420, new Item.Properties().tab(Goety.TAB)));

}
