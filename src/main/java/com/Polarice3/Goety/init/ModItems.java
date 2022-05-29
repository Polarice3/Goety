package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.armors.*;
import com.Polarice3.Goety.common.entities.items.ModBoatEntity;
import com.Polarice3.Goety.common.items.*;
import com.Polarice3.Goety.common.items.curios.AmuletItem;
import com.Polarice3.Goety.common.items.curios.RingItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.SimpleFoiledItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety.MOD_ID);

    public static void init(){
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Items
    public static final RegistryObject<Item> BLAZE_CORE = ITEMS.register("blazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> BROKEN_BLAZE_CORE = ITEMS.register("brokenblazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> ROCKETBOOSTER = ITEMS.register("rocketbooster", RocketBoosterItem::new);
    public static final RegistryObject<DarkSteak> DARKSTEAK = ITEMS.register("darksteak", DarkSteak::new);
    public static final RegistryObject<FlameGunItem> FLAMEGUN = ITEMS.register("flamegun", FlameGunItem::new);
    public static final RegistryObject<SacredFishItem> SACRED_FISH = ITEMS.register("sacred_fish", SacredFishItem::new);
    public static final RegistryObject<Item> DARK_SCROLL = ITEMS.register("dark_scroll", DarkScrollItem::new);
    public static final RegistryObject<PhilosophersStoneItem> PHILOSOPHERSSTONE = ITEMS.register("philosophersstone", PhilosophersStoneItem::new);
    public static final RegistryObject<WitchBombItem> WITCHBOMB = ITEMS.register("witchbomb", WitchBombItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_STEAK_UNCOOKED = ITEMS.register("mutatedsteak_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_STEAK = ITEMS.register("mutatedsteak", () -> new MutatedFoodItem(10, 1));
    public static final RegistryObject<UncookedMutatedItem> MUTATED_CHICKEN_UNCOOKED = ITEMS.register("mutatedchicken_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_CHICKEN = ITEMS.register("mutatedchicken", () -> new MutatedFoodItem(8, 0.8F));
    public static final RegistryObject<UncookedMutatedItem> MUTATED_MUTTON_UNCOOKED = ITEMS.register("mutatedmutton_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_MUTTON = ITEMS.register("mutatedmutton", () -> new MutatedFoodItem(8, 1));
    public static final RegistryObject<UncookedMutatedItem> MUTATED_PORKCHOP_UNCOOKED = ITEMS.register("mutatedporkchop_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_PORKCHOP = ITEMS.register("mutatedporkchop", () -> new MutatedFoodItem(10, 1));
    public static final RegistryObject<UncookedMutatedItem> MUTATED_RABBIT_UNCOOKED = ITEMS.register("mutatedrabbit_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_RABBIT = ITEMS.register("mutatedrabbit", () -> new MutatedFoodItem(7, 0.8F));
    public static final RegistryObject<Item> NETHER_BOOK = ITEMS.register("nether_book", NetherBookRawItem::new);
    public static final RegistryObject<Item> NETHER_BOOK_TRANSLATED = ITEMS.register("nether_book_translated", NetherBookItem::new);
    public static final RegistryObject<Item> DEADBAT = ITEMS.register("deadbat", DeadBatItem::new);
    public static final RegistryObject<Item> MAGIC_EMERALD = ITEMS.register("magic_emerald", () -> new SimpleFoiledItem(new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> SOUL_EMERALD = ITEMS.register("soul_emerald", () -> new SimpleFoiledItem(new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> SCRYING_MIRROR = ITEMS.register("scrying_mirror", ScryingMirrorItem::new);
    public static final RegistryObject<Item> TREASURE_POUCH = ITEMS.register("treasure_pouch", TreasurePouchItem::new);
    public static final RegistryObject<Item> APOSTLE_BLOOD = ITEMS.register("apostle_blood_bottle", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.RARE).tab(Goety.TAB)));
    public static final RegistryObject<Item> UNDEATH_POTION = ITEMS.register("undeath_potion", UndeathPotionItem::new);
    public static final RegistryObject<ModBoatItem> HAUNTED_BOAT = ITEMS.register("haunted_boat", () -> new ModBoatItem(ModBoatEntity.Type.HAUNTED, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_TRANSPORTATION)));
    public static final RegistryObject<SoulTransferItem> SOUL_TRANSFER = ITEMS.register("soul_transfer", SoulTransferItem::new);
    public static final RegistryObject<Item> BRAIN = ITEMS.register("brain", BrainItem::new);

    //Basic
    public static final RegistryObject<Item> SOULRUBY = ITEMS.register("soulruby", ItemBase::new);
    public static final RegistryObject<Item> RIFTSHARD = ITEMS.register("riftshard", ItemBase::new);
    public static final RegistryObject<Item> SPENTTOTEM = ITEMS.register("spenttotem", ItemBase::new);
    public static final RegistryObject<Item> CURSED_INGOT = ITEMS.register("cursed_ingot", ItemBase::new);
    public static final RegistryObject<Item> SAVAGETOOTH = ITEMS.register("savagetooth", ItemBase::new);
    public static final RegistryObject<Item> EMPTYCORE = ITEMS.register("emptycore", ItemBase::new);
    public static final RegistryObject<Item> ANIMALISCORE = ITEMS.register("animaliscore", ItemBase::new);
    public static final RegistryObject<Item> DARKFABRIC = ITEMS.register("darkfabric", ItemBase::new);
    public static final RegistryObject<Item> MAGICFABRIC = ITEMS.register("magicfabric", ItemBase::new);
    public static final RegistryObject<Item> PEDESTAL_DUMMY = ITEMS.register("pedestal_dummy", ItemBase::new);

    //Curios
    public static final RegistryObject<GoldTotemItem> GOLDTOTEM = ITEMS.register("goldtotem", GoldTotemItem::new);
    public static final RegistryObject<Item> FOCUSBAG = ITEMS.register("focusbag", FocusBagItem::new);
    public static final RegistryObject<Item> EMERALD_AMULET = ITEMS.register("emerald_amulet", AmuletItem::new);
    public static final RegistryObject<Item> VAMPIRIC_AMULET = ITEMS.register("vampiric_amulet", AmuletItem::new);
    public static final RegistryObject<Item> SKULL_AMULET = ITEMS.register("skull_amulet", AmuletItem::new);
    public static final RegistryObject<Item> RING_OF_WANT_1 = ITEMS.register("ring_of_want_1", RingItem::new);
    public static final RegistryObject<Item> RING_OF_WANT_2 = ITEMS.register("ring_of_want_2", RingItem::new);
    public static final RegistryObject<Item> RING_OF_WANT_3 = ITEMS.register("ring_of_want_3", RingItem::new);

    //Focuses
    public static final RegistryObject<Item> VEXINGFOCUS = ITEMS.register("vexingfocus", () -> new MagicFocusItem(MainConfig.VexCost.get()));
    public static final RegistryObject<Item> BITINGFOCUS = ITEMS.register("bitingfocus", () -> new MagicFocusItem(MainConfig.FangCost.get()));
    public static final RegistryObject<Item> ROARINGFOCUS = ITEMS.register("roaringfocus", () -> new MagicFocusItem(MainConfig.RoarCost.get()));
    public static final RegistryObject<Item> ROTTINGFOCUS = ITEMS.register("rottingfocus", () -> new MagicFocusItem(MainConfig.ZombieCost.get()));
    public static final RegistryObject<Item> OSSEOUSFOCUS = ITEMS.register("osseousfocus", () -> new MagicFocusItem(MainConfig.SkeletonCost.get()));
    public static final RegistryObject<Item> WITCHGALEFOCUS = ITEMS.register("witchgalefocus", () -> new MagicFocusItem(MainConfig.WitchGaleCost.get()));
    public static final RegistryObject<Item> SPIDERLINGFOCUS = ITEMS.register("spiderlingfocus", () -> new MagicFocusItem(MainConfig.SpiderlingCost.get()));
    public static final RegistryObject<Item> BRAINEATERFOCUS = ITEMS.register("braineaterfocus", () -> new MagicFocusItem(MainConfig.BrainEaterCost.get()));
    public static final RegistryObject<Item> TELEPORTFOCUS = ITEMS.register("teleportfocus", () -> new MagicFocusItem(MainConfig.TeleportCost.get()));
    public static final RegistryObject<Item> SOULSKULLFOCUS = ITEMS.register("soulskullfocus", () -> new MagicFocusItem(MainConfig.SoulSkullCost.get()));
    public static final RegistryObject<Item> FEASTFOCUS = ITEMS.register("feastfocus", () -> new MagicFocusItem(MainConfig.FeastCost.get()));
    public static final RegistryObject<Item> TEMPTINGFOCUS = ITEMS.register("temptingfocus", () -> new MagicFocusItem(MainConfig.TemptingCost.get()));
    public static final RegistryObject<Item> DRAGONFIREBALLFOCUS = ITEMS.register("dragonballfocus", () -> new MagicFocusItem(MainConfig.DragonFireballCost.get()));
    public static final RegistryObject<Item> CREEPERLINGFOCUS = ITEMS.register("creeperlingfocus", () -> new MagicFocusItem(MainConfig.CreeperlingCost.get()));
    public static final RegistryObject<Item> BREATHFOCUS = ITEMS.register("airbreathfocus", () -> new MagicFocusItem(MainConfig.BreathingCost.get()));
    public static final RegistryObject<Item> FIREBALLFOCUS = ITEMS.register("fireballfocus", () -> new MagicFocusItem(MainConfig.FireballCost.get()));
    public static final RegistryObject<Item> LAVABALLFOCUS = ITEMS.register("lavaballfocus", () -> new MagicFocusItem(MainConfig.LavaballCost.get()));
    public static final RegistryObject<Item> POISONBALLFOCUS = ITEMS.register("poisonballfocus", () -> new MagicFocusItem(MainConfig.PoisonballCost.get()));
    public static final RegistryObject<Item> ILLUSIONFOCUS = ITEMS.register("illusionfocus", () -> new MagicFocusItem(MainConfig.IllusionCost.get()));
    public static final RegistryObject<Item> SOULSHIELDFOCUS = ITEMS.register("soulshieldfocus", () -> new MagicFocusItem(MainConfig.SoulShieldCost.get()));
    public static final RegistryObject<Item> FIREBREATHFOCUS = ITEMS.register("firebreathfocus", () -> new MagicFocusItem(MainConfig.FireBreathCost.get()));

    //Tools
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> PITCHFORK = ITEMS.register("pitchfork", PitchforkItem::new);
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
            new DarkRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROROBE = ITEMS.register("necrorobe", () ->
            new DarkRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROBOOTSOFWANDER = ITEMS.register("necrobootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> ARACHNOHELM = ITEMS.register("arachnohelm", () ->
            new DarkRobeArmor(ModArmorMaterial.ARACHNOTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOROBE = ITEMS.register("arachnorobe", () ->
            new DarkRobeArmor(ModArmorMaterial.ARACHNOTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOBOOTSOFWANDER = ITEMS.register("arachnobootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> ARACHNOARMOREDHELM = ITEMS.register("arachnoarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ARACHNOARMOREDROBE = ITEMS.register("arachnoarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDARACHNOTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> DARKARMOREDHELM = ITEMS.register("darkarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKARMOREDROBE = ITEMS.register("darkarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARKARMOREDLEGGINGS = ITEMS.register("darkarmoredleggings", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> NECROARMOREDHELM = ITEMS.register("necroarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECROARMOREDROBE = ITEMS.register("necroarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> CULTISTHELM = ITEMS.register("cultisthelm", () ->
            new CultistRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> CULTISTROBE = ITEMS.register("cultistrobe", () ->
            new CultistRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties()));

    public static final RegistryObject<Item> CULTISTARMOREDHELM = ITEMS.register("cultistarmoredhelm", () ->
            new CultistArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> CULTISTARMOREDROBE = ITEMS.register("cultistarmoredrobe", () ->
            new CultistArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties()));

    //JEI
    public static final RegistryObject<DummyItem> JEI_DUMMY_NONE = ITEMS.register(
            "jei_dummy/none", () -> new DummyItem(new Item.Properties()));
    public static final RegistryObject<DummyItem> JEI_DUMMY_REQUIRE_SACRIFICE = ITEMS.register(
            "jei_dummy/sacrifice", () -> new DummyItem(new Item.Properties()));
    public static final RegistryObject<DummyItem> JEI_DUMMY_REQUIRE_ITEM_USE = ITEMS.register(
            "jei_dummy/item", () -> new DummyItem(new Item.Properties()));

    public static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Goety.TAB);
    }
}
