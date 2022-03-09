package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.armors.*;
import com.Polarice3.Goety.common.blocks.*;
import com.Polarice3.Goety.common.items.*;
import com.Polarice3.Goety.common.potions.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRegistryHandler {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety.MOD_ID);
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Goety.MOD_ID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Goety.MOD_ID);
    public static KeyBinding[] keyBindings;

    public static void init(){
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        keyBindings = new KeyBinding[2];

        keyBindings[0] = new KeyBinding("key.goety.wand", 90, "key.goety.category");
        keyBindings[1] = new KeyBinding("key.goety.wandandbag", 88, "key.goety.category");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    //Items
    public static final RegistryObject<Item> BLAZE_CORE = ITEMS.register("blazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> BROKEN_BLAZE_CORE = ITEMS.register("brokenblazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> ROCKETBOOSTER = ITEMS.register("rocketbooster", RocketBoosterItem::new);
    public static final RegistryObject<DarkSteak> DARKSTEAK = ITEMS.register("darksteak", DarkSteak::new);
    public static final RegistryObject<FlameGunItem> FLAMEGUN = ITEMS.register("flamegun", FlameGunItem::new);
    public static final RegistryObject<SacredFishItem> SACRED_FISH = ITEMS.register("sacred_fish", SacredFishItem::new);
    public static final RegistryObject<Item> DARK_SCROLL = ITEMS.register("dark_scroll", DarkScrollItem::new);
    public static final RegistryObject<Item> SOULRUBY = ITEMS.register("soulruby", ItemBase::new);
    public static final RegistryObject<Item> RIFTSHARD = ITEMS.register("riftshard", ItemBase::new);
    public static final RegistryObject<PhilosophersStoneItem> PHILOSOPHERSSTONE = ITEMS.register("philosophersstone", PhilosophersStoneItem::new);
    public static final RegistryObject<WitchBombItem> WITCHBOMB = ITEMS.register("witchbomb", WitchBombItem::new);
    public static final RegistryObject<GoldTotemItem> GOLDTOTEM = ITEMS.register("goldtotem", GoldTotemItem::new);
    public static final RegistryObject<Item> SPENTTOTEM = ITEMS.register("spenttotem", ItemBase::new);
    public static final RegistryObject<Item> CURSED_INGOT = ITEMS.register("cursed_ingot", ItemBase::new);
    public static final RegistryObject<Item> SAVAGETOOTH = ITEMS.register("savagetooth", ItemBase::new);
    public static final RegistryObject<Item> EMPTYCORE = ITEMS.register("emptycore", ItemBase::new);
    public static final RegistryObject<Item> AIRYCORE = ITEMS.register("airycore", ItemBase::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_STEAK_UNCOOKED = ITEMS.register("mutatedsteak_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedSteakItem> MUTATED_STEAK = ITEMS.register("mutatedsteak", MutatedSteakItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_CHICKEN_UNCOOKED = ITEMS.register("mutatedchicken_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedChickenItem> MUTATED_CHICKEN = ITEMS.register("mutatedchicken", MutatedChickenItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_MUTTON_UNCOOKED = ITEMS.register("mutatedmutton_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedMuttonItem> MUTATED_MUTTON = ITEMS.register("mutatedmutton", MutatedMuttonItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_PORKCHOP_UNCOOKED = ITEMS.register("mutatedporkchop_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedPorkchopItem> MUTATED_PORKCHOP = ITEMS.register("mutatedporkchop", MutatedPorkchopItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_RABBIT_UNCOOKED = ITEMS.register("mutatedrabbit_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedRabbitItem> MUTATED_RABBIT = ITEMS.register("mutatedrabbit", MutatedRabbitItem::new);
    public static final RegistryObject<Item> DARKFABRIC = ITEMS.register("darkfabric", ItemBase::new);
    public static final RegistryObject<Item> MAGICFABRIC = ITEMS.register("magicfabric", ItemBase::new);
    public static final RegistryObject<Item> DEADBAT = ITEMS.register("deadbat", DeadBatItem::new);
    //Focuses
    public static final RegistryObject<Item> FOCUSBAG = ITEMS.register("focusbag", FocusBagItem::new);
    public static final RegistryObject<Item> VEXINGFOCUS = ITEMS.register("vexingfocus", () -> new MagicFocusItem(MainConfig.VexCost.get()));
    public static final RegistryObject<Item> BITINGFOCUS = ITEMS.register("bitingfocus", () -> new MagicFocusItem(MainConfig.FangCost.get()));
    public static final RegistryObject<Item> ROARINGFOCUS = ITEMS.register("roaringfocus", () -> new MagicFocusItem(MainConfig.RoarCost.get()));
    public static final RegistryObject<Item> NECROTURGYFOCUS = ITEMS.register("necroturgyfocus", () -> new MagicFocusItem(MainConfig.ZombieCost.get()));
    public static final RegistryObject<Item> OSSEOUSFOCUS = ITEMS.register("osseousfocus", () -> new MagicFocusItem(MainConfig.SkeletonCost.get()));
    public static final RegistryObject<Item> WITCHGALEFOCUS = ITEMS.register("witchgalefocus", () -> new MagicFocusItem(MainConfig.WitchGaleCost.get()));
    public static final RegistryObject<Item> SPIDERLINGFOCUS = ITEMS.register("spiderlingfocus", () -> new MagicFocusItem(MainConfig.SpiderlingCost.get()));
    public static final RegistryObject<Item> BRAINEATERFOCUS = ITEMS.register("braineaterfocus", () -> new MagicFocusItem(MainConfig.BrainEaterCost.get()));
    public static final RegistryObject<Item> TELEPORTFOCUS = ITEMS.register("teleportfocus", () -> new MagicFocusItem(MainConfig.TeleportCost.get()));
    public static final RegistryObject<Item> SOULSKULLFOCUS = ITEMS.register("soulskullfocus", () -> new MagicFocusItem(MainConfig.SoulSkullCost.get()));
    public static final RegistryObject<Item> FEASTFOCUS = ITEMS.register("feastfocus", () -> new MagicFocusItem(MainConfig.FeastCost.get()));
    public static final RegistryObject<Item> TEMPTINGFOCUS = ITEMS.register("temptingfocus", () -> new MagicFocusItem(MainConfig.TemptingCost.get()));
    public static final RegistryObject<Item> DRAGONFIREBALLFOCUS = ITEMS.register("dragonfireballfocus", () -> new MagicFocusItem(MainConfig.DragonFireballCost.get()));
    public static final RegistryObject<Item> CREEPERLINGFOCUS = ITEMS.register("creeperlingfocus", () -> new MagicFocusItem(MainConfig.CreeperlingCost.get()));

    //Tools
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> WOODEN_SPEAR = ITEMS.register("wooden_spear", () ->
            new SpearItem(ItemTier.WOOD, 2, -2.9F));
    public static final RegistryObject<Item> STONE_SPEAR = ITEMS.register("stone_spear", () ->
            new SpearItem(ItemTier.STONE, 2, -2.9F));
    public static final RegistryObject<Item> IRON_SPEAR = ITEMS.register("iron_spear", () ->
            new SpearItem(ItemTier.IRON, 2, -2.9F));
    public static final RegistryObject<Item> DIAMOND_SPEAR = ITEMS.register("diamond_spear", () ->
            new SpearItem(ItemTier.DIAMOND, 2, -2.9F));
    public static final RegistryObject<Item> NETHERITE_SPEAR = ITEMS.register("netherite_spear", () ->
            new SpearItem(ItemTier.NETHERITE, 2, -2.9F));
    public static final RegistryObject<Item> PHILOSOPHERS_MACE = ITEMS.register("philosophers_mace", PhilosophersMaceItem::new);
    public static final RegistryObject<Item> SOULWAND = ITEMS.register("soulwand", SoulWand::new);
    public static final RegistryObject<Item> SOULSTAFF = ITEMS.register("soulstaff", SoulStaff::new);
    //Armors
    public static final RegistryObject<Item> DARKHELM = ITEMS.register("darkhelm", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKROBE = ITEMS.register("darkrobe", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKLEGGINGS = ITEMS.register("darkleggings", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKBOOTSOFWANDER = ITEMS.register("darkbootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> NECROHELM = ITEMS.register("necrohelm", () ->
            new NecroRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROROBE = ITEMS.register("necrorobe", () ->
            new NecroRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROBOOTSOFWANDER = ITEMS.register("necrobootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> ARACHNOHELM = ITEMS.register("arachnohelm", () ->
            new ArachnoRobeArmor(ModArmorMaterial.ARACHNOTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOROBE = ITEMS.register("arachnorobe", () ->
            new ArachnoRobeArmor(ModArmorMaterial.ARACHNOTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOBOOTSOFWANDER = ITEMS.register("arachnobootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> ARACHNOARMOREDHELM = ITEMS.register("arachnoarmoredhelm", () ->
            new ArachnoArmoredRobeArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOARMOREDROBE = ITEMS.register("arachnoarmoredrobe", () ->
            new ArachnoArmoredRobeArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> DARKARMOREDHELM = ITEMS.register("darkarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKARMOREDROBE = ITEMS.register("darkarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKARMOREDLEGGINGS = ITEMS.register("darkarmoredleggings", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> NECROARMOREDHELM = ITEMS.register("necroarmoredhelm", () ->
            new NecroArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROARMOREDROBE = ITEMS.register("necroarmoredrobe", () ->
            new NecroArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> APOSTLEHELM = ITEMS.register("apostlehelm", () ->
            new ApostleRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> APOSTLEROBE = ITEMS.register("apostlerobe", () ->
            new ApostleRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties()));

    public static final RegistryObject<Item> APOSTLEARMOREDHELM = ITEMS.register("apostlearmoredhelm", () ->
            new ApostleArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> APOSTLEARMOREDROBE = ITEMS.register("apostlearmoredrobe", () ->
            new ApostleArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties()));
    //Blocks
    public static final RegistryObject<Block> BLAZE_CORE_BLOCK = BLOCKS.register("blazecoreblock", TankCoreBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BLOCK = BLOCKS.register("cursed_stone", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_BRICK_BLOCK = BLOCKS.register("cursed_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_CHISELED_BLOCK = BLOCKS.register("cursed_stone_chiseled", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_TILES_BLOCK = BLOCKS.register("cursed_tiles", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_CAGE_BLOCK = BLOCKS.register("cursed_cage", CursedCageBlock::new);
    public static final RegistryObject<Block> CURSED_TOTEM_BLOCK = BLOCKS.register("cursed_totem", CursedStoneBlock::new);
    public static final RegistryObject<Block> FANG_TOTEM = BLOCKS.register("fang_totem", FangTotemBlock::new);
    public static final RegistryObject<Block> MUTATE_TOTEM = BLOCKS.register("mutation_totem", MutateTotemBlock::new);
    public static final RegistryObject<Block> UNDEAD_TOTEM = BLOCKS.register("undead_totem", UndeadTotemBlock::new);
    public static final RegistryObject<Block> WIND_TOTEM = BLOCKS.register("wind_totem", WindTotemBlock::new);
    public static final RegistryObject<Block> OBELISK = BLOCKS.register("obelisk", ObeliskBlock::new);
//    public static final RegistryObject<Block> SOULFORGE = BLOCKS.register("soulforge", SoulForgeBlock::new);

    //Slabs
    public static final RegistryObject<Block> CURSED_STONE_SLAB_BLOCK = BLOCKS.register("cursed_stone_slab",
        () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_BRICK_SLAB_BLOCK = BLOCKS.register("cursed_bricks_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_TILES_SLAB_BLOCK = BLOCKS.register("cursed_tiles_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    //Stairs
    public static final RegistryObject<Block> CURSED_STONE_STAIRS_BLOCK = BLOCKS.register("cursed_stone_stairs",
            () -> new StairsBlock(CURSED_STONE_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_STONE_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_BRICK_STAIRS_BLOCK = BLOCKS.register("cursed_bricks_stairs",
            () -> new StairsBlock(CURSED_BRICK_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_TILES_STAIRS_BLOCK = BLOCKS.register("cursed_tiles_stairs",
            () -> new StairsBlock(CURSED_TILES_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_TILES_BLOCK.get())));
    //Walls
    public static final RegistryObject<Block> CURSED_BRICK_WALL_BLOCK = BLOCKS.register("cursed_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(CURSED_BRICK_BLOCK.get())));
    //Panes
    public static final RegistryObject<Block> CURSED_BARS_BLOCK = BLOCKS.register("cursed_bars",
            () -> new PaneBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.NONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    //Block Items
    public static final RegistryObject<Item> BLAZE_CORE_ITEM = ITEMS.register("blazecoreblock",
            () -> new BlockItemBase(BLAZE_CORE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_ITEM = ITEMS.register("cursed_stone",
            () -> new BlockItemBase(CURSED_STONE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_SLAB_ITEM = ITEMS.register("cursed_stone_slab",
            () -> new BlockItemBase(CURSED_STONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_STAIRS_ITEM = ITEMS.register("cursed_stone_stairs",
            () -> new BlockItemBase(CURSED_STONE_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_ITEM = ITEMS.register("cursed_bricks",
            () -> new BlockItemBase(CURSED_BRICK_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_SLAB_ITEM = ITEMS.register("cursed_bricks_slab",
            () -> new BlockItemBase(CURSED_BRICK_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_STAIRS_ITEM = ITEMS.register("cursed_bricks_stairs",
            () -> new BlockItemBase(CURSED_BRICK_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_WALL_ITEM = ITEMS.register("cursed_bricks_wall",
            () -> new BlockItemBase(CURSED_BRICK_WALL_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_CHISELED_ITEM = ITEMS.register("cursed_stone_chiseled",
            () -> new BlockItemBase(CURSED_STONE_CHISELED_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_ITEM = ITEMS.register("cursed_tiles",
            () -> new BlockItemBase(CURSED_TILES_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_SLAB_ITEM = ITEMS.register("cursed_tiles_slab",
            () -> new BlockItemBase(CURSED_TILES_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_STAIRS_ITEM = ITEMS.register("cursed_tiles_stairs",
            () -> new BlockItemBase(CURSED_TILES_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BARS_ITEM = ITEMS.register("cursed_bars",
            () -> new BlockItemBase(CURSED_BARS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_CAGE_ITEM = ITEMS.register("cursed_cage",
            () -> new BlockItemBase(CURSED_CAGE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TOTEM_ITEM = ITEMS.register("cursed_totem",
            () -> new BlockItemBase(CURSED_TOTEM_BLOCK.get()));
    public static final RegistryObject<Item> FANG_TOTEM_ITEM = ITEMS.register("fang_totem",
            () -> new BlockItemBase(FANG_TOTEM.get()));
    public static final RegistryObject<Item> MUTATE_TOTEM_ITEM = ITEMS.register("mutation_totem",
            () -> new BlockItemBase(MUTATE_TOTEM.get()));
    public static final RegistryObject<Item> UNDEAD_TOTEM_ITEM = ITEMS.register("undead_totem",
            () -> new BlockItemBase(UNDEAD_TOTEM.get()));
    public static final RegistryObject<Item> WIND_TOTEM_ITEM = ITEMS.register("wind_totem",
            () -> new BlockItemBase(WIND_TOTEM.get()));
    public static final RegistryObject<Item> OBELISK_ITEM = ITEMS.register("obelisk",
            () -> new BlockItemBase(OBELISK.get()));
    //Effects
    public static final RegistryObject<Effect> MINOR_HARM = EFFECTS.register("minorharm",
            MinorHarmEffect::new);
    public static final RegistryObject<Effect> DEATHPROTECT = EFFECTS.register("deathprotect",
            DeathProtectEffect::new);
    public static final RegistryObject<Effect> GOLDTOUCHED = EFFECTS.register("goldtouched",
            GoldTouchEffect::new);
    public static final RegistryObject<Effect> HOSTED = EFFECTS.register("hosted",
            HostedEffect::new);
    public static final RegistryObject<Effect> SUMMONDOWN = EFFECTS.register("summondown",
            SummonDownEffect::new);
    public static final RegistryObject<Effect> COSMIC = EFFECTS.register("cosmic",
            CosmicEffect::new);
    public static final RegistryObject<Effect> CURSED = EFFECTS.register("cursed",
            CursedEffect::new);
    public static final RegistryObject<Effect> ILLAGUE = EFFECTS.register("illague",
            IllagueEffect::new);
    public static final RegistryObject<Effect> NECROPOWER = EFFECTS.register("necropower",
            NecroPowerEffect::new);
}
