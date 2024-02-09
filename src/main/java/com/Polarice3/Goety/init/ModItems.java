package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.client.armors.*;
import com.Polarice3.Goety.common.entities.items.ModBoatEntity;
import com.Polarice3.Goety.common.fluid.ModFluids;
import com.Polarice3.Goety.common.items.*;
import com.Polarice3.Goety.common.items.curios.AmuletItem;
import com.Polarice3.Goety.common.items.curios.GraveGloveItem;
import com.Polarice3.Goety.common.items.curios.PendantOfHungerItem;
import com.Polarice3.Goety.common.items.curios.RingItem;
import com.Polarice3.Goety.common.items.equipment.*;
import com.Polarice3.Goety.common.items.magic.*;
import com.Polarice3.Goety.common.magic.spells.*;
import com.Polarice3.Goety.common.magic.spells.ender.AcidBreathSpell;
import com.Polarice3.Goety.common.magic.spells.ender.DragonFireballSpell;
import com.Polarice3.Goety.common.magic.spells.ender.TeleportSpell;
import com.Polarice3.Goety.common.magic.spells.fel.CreeperlingSpell;
import com.Polarice3.Goety.common.magic.spells.fel.PoisonBallSpell;
import com.Polarice3.Goety.common.magic.spells.fel.SpiderlingSpell;
import com.Polarice3.Goety.common.magic.spells.frost.FrostBreathSpell;
import com.Polarice3.Goety.common.magic.spells.frost.IceChunkSpell;
import com.Polarice3.Goety.common.magic.spells.frost.IceStormSpell;
import com.Polarice3.Goety.common.magic.spells.ill.FangSpell;
import com.Polarice3.Goety.common.magic.spells.ill.FeastSpell;
import com.Polarice3.Goety.common.magic.spells.ill.IllusionSpell;
import com.Polarice3.Goety.common.magic.spells.ill.VexSpell;
import com.Polarice3.Goety.common.magic.spells.necromancy.*;
import com.Polarice3.Goety.common.magic.spells.nether.FireballSpell;
import com.Polarice3.Goety.common.magic.spells.nether.LavaballSpell;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
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
    public static final RegistryObject<Item> DARK_SCROLL = ITEMS.register("dark_scroll", DarkScrollItem::new);
    public static final RegistryObject<PhilosophersStoneItem> PHILOSOPHERSSTONE = ITEMS.register("philosophers_stone", PhilosophersStoneItem::new);
    public static final RegistryObject<WitchBombItem> WITCH_BOMB = ITEMS.register("witchbomb", WitchBombItem::new);
    public static final RegistryObject<BurningPotionItem> BURNING_POTION = ITEMS.register("burning_potion", BurningPotionItem::new);
    public static final RegistryObject<Item> NETHER_BOOK = ITEMS.register("nether_book", NetherBookItem::new);
    public static final RegistryObject<Item> NETHER_BOOK_TRANSLATED = ITEMS.register("nether_book_translated", NetherBookTranslatedItem::new);
    public static final RegistryObject<Item> DEADBAT = ITEMS.register("deadbat", DeadBatItem::new);
    public static final RegistryObject<Item> MAGIC_EMERALD = ITEMS.register("magic_emerald", () -> new SimpleFoiledItem(new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> SOUL_EMERALD = ITEMS.register("soul_emerald", () -> new SimpleFoiledItem(new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> SCRYING_MIRROR = ITEMS.register("scrying_mirror", ScryingMirrorItem::new);
    public static final RegistryObject<Item> TREASURE_POUCH = ITEMS.register("treasure_pouch", TreasurePouchItem::new);
    public static final RegistryObject<Item> UNHOLY_BLOOD = ITEMS.register("apostle_blood_bottle", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.RARE).tab(Goety.TAB)));
    public static final RegistryObject<Item> UNDEATH_POTION = ITEMS.register("undeath_potion", UndeathPotionItem::new);
    public static final RegistryObject<ModBoatItem> HAUNTED_BOAT = ITEMS.register("haunted_boat", () -> new ModBoatItem(ModBoatEntity.Type.HAUNTED, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_TRANSPORTATION)));
    public static final RegistryObject<ModBoatItem> GLOOM_BOAT = ITEMS.register("gloom_boat", () -> new ModBoatItem(ModBoatEntity.Type.GLOOM, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_TRANSPORTATION)));
    public static final RegistryObject<ModBoatItem> MURK_BOAT = ITEMS.register("murk_boat", () -> new ModBoatItem(ModBoatEntity.Type.MURK, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_TRANSPORTATION)));
    public static final RegistryObject<SoulTransferItem> SOUL_TRANSFER = ITEMS.register("soul_transfer", SoulTransferItem::new);
    public static final RegistryObject<Item> FLAME_CAPTURE = ITEMS.register("flame_capture", FlameCaptureItem::new);
    public static final RegistryObject<Item> BRAIN = ITEMS.register("brain", BrainItem::new);
    public static final RegistryObject<Item> DEAD_SLIME_BALL = ITEMS.register("dead_slime_ball", DeadSlimeBallItem::new);
    public static final RegistryObject<Item> FORBIDDEN_SCROLL = ITEMS.register("forbidden_scroll", ForbiddenScrollItem::new);
    public static final RegistryObject<Item> SACRED_FISH_BUCKET = ITEMS.register("sacred_fish_bucket", () -> new FishBucketItem(ModEntityType.SACRED_FISH,() -> Fluids.WATER, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> QUICKSAND_BUCKET = ITEMS.register("quicksand_bucket", () -> new BucketItem(() -> ModFluids.QUICKSAND_SOURCE.get(), (new Item.Properties()).stacksTo(1).tab(Goety.TAB)));

    //Food
    public static final RegistryObject<SacredFishItem> SACRED_FISH = ITEMS.register("sacred_fish", SacredFishItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_STEAK_UNCOOKED = ITEMS.register("mutatedsteak_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_CHICKEN_UNCOOKED = ITEMS.register("mutatedchicken_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_MUTTON_UNCOOKED = ITEMS.register("mutatedmutton_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_PORKCHOP_UNCOOKED = ITEMS.register("mutatedporkchop_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_RABBIT_UNCOOKED = ITEMS.register("mutatedrabbit_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedFoodItem> MUTATED_STEAK = ITEMS.register("mutatedsteak", () -> new MutatedFoodItem(10, 1));
    public static final RegistryObject<MutatedFoodItem> MUTATED_CHICKEN = ITEMS.register("mutatedchicken", () -> new MutatedFoodItem(8, 0.8F));
    public static final RegistryObject<MutatedFoodItem> MUTATED_MUTTON = ITEMS.register("mutatedmutton", () -> new MutatedFoodItem(8, 1));
    public static final RegistryObject<MutatedFoodItem> MUTATED_PORKCHOP = ITEMS.register("mutatedporkchop", () -> new MutatedFoodItem(10, 1));
    public static final RegistryObject<MutatedFoodItem> MUTATED_RABBIT = ITEMS.register("mutatedrabbit", () -> new MutatedFoodItem(7, 0.8F));

    //Basic
    public static final RegistryObject<Item> SOUL_RUBY = ITEMS.register("soulruby", ItemBase::new);
    public static final RegistryObject<Item> BROKEN_BLAZE_CORE = ITEMS.register("brokenblazecore", ItemBase::new);
    public static final RegistryObject<Item> SPENT_TOTEM = ITEMS.register("spenttotem", ItemBase::new);
    public static final RegistryObject<Item> CURSED_INGOT = ITEMS.register("cursed_ingot", ItemBase::new);
    public static final RegistryObject<Item> SAVAGE_TOOTH = ITEMS.register("savagetooth", ItemBase::new);
    public static final RegistryObject<Item> EMPTY_CORE = ITEMS.register("emptycore", ItemBase::new);
    public static final RegistryObject<Item> ANIMALIS_CORE = ITEMS.register("animaliscore", AnimalisCoreItem::new);
    public static final RegistryObject<Item> HUNGER_CORE = ITEMS.register("hunger_core", ItemBase::new);
    public static final RegistryObject<Item> WIND_CORE = ITEMS.register("wind_core", ItemBase::new);
    public static final RegistryObject<Item> COLD_SHARD = ITEMS.register("cold_shard", ItemBase::new);
    public static final RegistryObject<Item> COLD_HEART = ITEMS.register("cold_heart", ItemBase::new);
    public static final RegistryObject<Item> FROST_ORE = ITEMS.register("frost_ore", ItemBase::new);
    public static final RegistryObject<Item> FROST_INGOT = ITEMS.register("frost_ingot", ItemBase::new);
    public static final RegistryObject<Item> FROST_NUGGET = ITEMS.register("frost_nugget", ItemBase::new);
    public static final RegistryObject<Item> DARK_FABRIC = ITEMS.register("darkfabric", ItemBase::new);
    public static final RegistryObject<Item> MAGIC_FABRIC = ITEMS.register("magicfabric", ItemBase::new);
    public static final RegistryObject<Item> OCCULT_FABRIC = ITEMS.register("occultfabric", ItemBase::new);
    public static final RegistryObject<Item> PEDESTAL_DUMMY = ITEMS.register("pedestal_dummy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRIED_REMNANT = ITEMS.register("dried_remnant", ItemBase::new);
    public static final RegistryObject<Item> DEAD_SAND_HEAP = ITEMS.register("dead_sand_heap", ItemBase::new);
    public static final RegistryObject<Item> FORBIDDEN_FRAGMENT = ITEMS.register("forbidden_fragment", ItemBase::new);
    public static final RegistryObject<Item> FORBIDDEN_PIECE = ITEMS.register("forbidden_piece", ItemBase::new);
    public static final RegistryObject<Item> BURNING_TUSK = ITEMS.register("burning_tusk", ItemBase::new);
    public static final RegistryObject<Item> LAPIS_STRING = ITEMS.register("lapis_string", ItemBase::new);
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm", ItemBase::new);

    //Discs
    public static final RegistryObject<Item> MUSIC_DISC_VIZIER = ITEMS.register("music_disc_vizier", () -> new MusicDiscItem(14, ModSounds.MUSIC_DISC_VIZIER, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_APOSTLE = ITEMS.register("music_disc_apostle", () -> new MusicDiscItem(15, ModSounds.MUSIC_DISC_APOSTLE, (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));

    //Curios
    public static final RegistryObject<GoldTotemItem> GOLDTOTEM = ITEMS.register("goldtotem", () -> new GoldTotemItem(ITotem.MAX_SOULS));
    public static final RegistryObject<Item> FOCUSBAG = ITEMS.register("focusbag", FocusBagItem::new);
    public static final RegistryObject<Item> EMERALD_AMULET = ITEMS.register("emerald_amulet", AmuletItem::new);
    public static final RegistryObject<Item> STAR_AMULET = ITEMS.register("star_amulet", AmuletItem::new);
    public static final RegistryObject<Item> PENDANT_OF_HUNGER = ITEMS.register("pendant_of_hunger", PendantOfHungerItem::new);
    public static final RegistryObject<Item> RING_OF_WANT = ITEMS.register("ring_of_want", RingItem::new);
    public static final RegistryObject<Item> GRAVE_GLOVE = ITEMS.register("grave_glove", GraveGloveItem::new);

    //Focuses
    public static final RegistryObject<Item> VEXING_FOCUS = ITEMS.register("vexingfocus", () -> new MagicFocusItem(new VexSpell()));
    public static final RegistryObject<Item> BITING_FOCUS = ITEMS.register("bitingfocus", () -> new MagicFocusItem(new FangSpell()));
    public static final RegistryObject<Item> ROARING_FOCUS = ITEMS.register("roaringfocus", () -> new MagicFocusItem(new RoarSpell()));
    public static final RegistryObject<Item> ROTTING_FOCUS = ITEMS.register("rottingfocus", () -> new MagicFocusItem(new ZombieSpell()));
    public static final RegistryObject<Item> OSSEOUS_FOCUS = ITEMS.register("osseousfocus", () -> new MagicFocusItem(new SkeletonSpell()));
    public static final RegistryObject<Item> RIGID_FOCUS = ITEMS.register("rigidfocus", () -> new MagicFocusItem(new DredenSpell()));
    public static final RegistryObject<Item> SPOOKY_FOCUS = ITEMS.register("spookyfocus", () -> new MagicFocusItem(new WraithSpell()));
    public static final RegistryObject<Item> WITCHGALE_FOCUS = ITEMS.register("witchgalefocus", () -> new MagicFocusItem(new WitchGaleSpell()));
    public static final RegistryObject<Item> SPIDERLING_FOCUS = ITEMS.register("spiderlingfocus", () -> new MagicFocusItem(new SpiderlingSpell()));
    public static final RegistryObject<Item> BRAINEATER_FOCUS = ITEMS.register("braineaterfocus", () -> new MagicFocusItem(new BrainEaterSpell()));
    public static final RegistryObject<Item> TELEPORT_FOCUS = ITEMS.register("teleportfocus", () -> new MagicFocusItem(new TeleportSpell()));
    public static final RegistryObject<Item> SOULSKULL_FOCUS = ITEMS.register("soulskullfocus", () -> new MagicFocusItem(new SoulSkullSpell()));
    public static final RegistryObject<Item> FEAST_FOCUS = ITEMS.register("feastfocus", () -> new MagicFocusItem(new FeastSpell()));
    public static final RegistryObject<Item> TEMPTING_FOCUS = ITEMS.register("temptingfocus", () -> new MagicFocusItem(new TemptingSpell()));
    public static final RegistryObject<Item> ENDERACID_FOCUS = ITEMS.register("enderacidfocus", () -> new MagicFocusItem(new AcidBreathSpell()));
    public static final RegistryObject<Item> DRAGONFIREBALL_FOCUS = ITEMS.register("dragonballfocus", () -> new MagicFocusItem(new DragonFireballSpell()));
    public static final RegistryObject<Item> CREEPERLING_FOCUS = ITEMS.register("creeperlingfocus", () -> new MagicFocusItem(new CreeperlingSpell()));
    public static final RegistryObject<Item> BREATH_FOCUS = ITEMS.register("airbreathfocus", () -> new MagicFocusItem(new BreathSpell()));
    public static final RegistryObject<Item> FIREBALL_FOCUS = ITEMS.register("fireballfocus", () -> new MagicFocusItem(new FireballSpell()));
    public static final RegistryObject<Item> LAVABALL_FOCUS = ITEMS.register("lavaballfocus", () -> new MagicFocusItem(new LavaballSpell()));
    public static final RegistryObject<Item> POISONBALL_FOCUS = ITEMS.register("poisonballfocus", () -> new MagicFocusItem(new PoisonBallSpell()));
    public static final RegistryObject<Item> ILLUSION_FOCUS = ITEMS.register("illusionfocus", () -> new MagicFocusItem(new IllusionSpell()));
    public static final RegistryObject<Item> FIREBREATH_FOCUS = ITEMS.register("firebreathfocus", () -> new MagicFocusItem(new FireBreathSpell()));
    public static final RegistryObject<Item> FROSTBREATH_FOCUS = ITEMS.register("frostbreathfocus", () -> new MagicFocusItem(new FrostBreathSpell()));
    public static final RegistryObject<Item> SOULLIGHT_FOCUS = ITEMS.register("soullightfocus", () -> new MagicFocusItem(new SoulLightSpell()));
    public static final RegistryObject<Item> GLOWLIGHT_FOCUS = ITEMS.register("glowlightfocus", () -> new MagicFocusItem(new GlowLightSpell()));
    public static final RegistryObject<Item> ICEOLOGY_FOCUS = ITEMS.register("iceologyfocus", () -> new MagicFocusItem(new IceChunkSpell()));
    public static final RegistryObject<Item> ICESTORM_FOCUS = ITEMS.register("icestormfocus", () -> new MagicFocusItem(new IceStormSpell()));
    public static final RegistryObject<Item> HOUNDING_FOCUS = ITEMS.register("houndingfocus", () -> new MagicFocusItem(new UndeadWolfSpell()));
    public static final RegistryObject<Item> PHANTASM_FOCUS = ITEMS.register("phantasmfocus", () -> new MagicFocusItem(new PhantomSpell()));
    public static final RegistryObject<Item> LAUNCH_FOCUS = ITEMS.register("launchfocus", () -> new MagicFocusItem(new LaunchSpell()));
    public static final RegistryObject<Item> SONICBOOM_FOCUS = ITEMS.register("sonicboomfocus", () -> new MagicFocusItem(new SonicBoomSpell()));
    public static final RegistryObject<Item> RECALL_FOCUS = ITEMS.register("recallfocus", RecallFocus::new);

    //Tools & Weapons
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> PITCHFORK = ITEMS.register("pitchfork", PitchforkItem::new);
    public static final RegistryObject<Item> DARK_SCYTHE = ITEMS.register("dark_scythe", () -> new DarkScytheItem(ItemTier.IRON));
    public static final RegistryObject<Item> FROST_SCYTHE = ITEMS.register("frost_scythe", FrostScytheItem::new);
    public static final RegistryObject<Item> DEATH_SCYTHE = ITEMS.register("death_scythe", DeathScytheItem::new);
    public static final RegistryObject<Item> FROST_SWORD = ITEMS.register("frost_sword", FrostSwordItem::new);
    public static final RegistryObject<Item> FROST_SHOVEL = ITEMS.register("frost_shovel", FrostShovelItem::new);
    public static final RegistryObject<Item> FROST_PICKAXE = ITEMS.register("frost_pickaxe", FrostPickaxeItem::new);
    public static final RegistryObject<Item> FROST_AXE = ITEMS.register("frost_axe", FrostAxeItem::new);
    public static final RegistryObject<Item> FROST_HOE = ITEMS.register("frost_hoe", FrostHoeItem::new);
    public static final RegistryObject<Item> WITCH_BOW = ITEMS.register("witch_bow", WitchBowItem::new);
    public static final RegistryObject<Item> NETHERITE_BOW = ITEMS.register("netherite_bow", NetheriteBowItem::new);
    public static final RegistryObject<Item> PHILOSOPHERS_MACE = ITEMS.register("philosophers_mace", PhilosophersMaceItem::new);
    public static final RegistryObject<Item> DARK_WAND = ITEMS.register("soulwand", SoulWand::new);
    public static final RegistryObject<Item> DARK_STAFF = ITEMS.register("soulstaff", SoulStaff::new);
    public static final RegistryObject<Item> CORRUPT_STAFF = ITEMS.register("corrupt_staff", CorruptStaff::new);
    public static final RegistryObject<Item> FROST_CHARGE = ITEMS.register("frost_charge", FrostChargeItem::new);

    //Armors
    public static final RegistryObject<Item> DARK_HELM = ITEMS.register("darkhelm", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_ROBE = ITEMS.register("darkrobe", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_LEGGINGS = ITEMS.register("darkleggings", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_BOOTS_OF_WANDER = ITEMS.register("darkbootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> NECRO_HELM = ITEMS.register("necrohelm", () ->
            new DarkRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECRO_ROBE = ITEMS.register("necrorobe", () ->
            new DarkRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECRO_BOOTS_OF_WANDER = ITEMS.register("necrobootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> FEL_HELM = ITEMS.register("felhelm", () ->
            new DarkRobeArmor(ModArmorMaterial.FELTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FEL_ROBE = ITEMS.register("felrobe", () ->
            new DarkRobeArmor(ModArmorMaterial.FELTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FEL_BOOTS_OF_WANDER = ITEMS.register("felbootsofwander", () ->
            new WanderBootsArmor(ModArmorMaterial.ARMOREDFELTURGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> ILLUSION_HELM = ITEMS.register("illusionhelm", () ->
            new IllusionRobeArmor(ModArmorMaterial.ILLUSION, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ILLUSION_ROBE = ITEMS.register("illusionrobe", () ->
            new IllusionRobeArmor(ModArmorMaterial.ILLUSION, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ILLUSION_LEGGINGS = ITEMS.register("illusionleggings", () ->
            new IllusionRobeArmor(ModArmorMaterial.ILLUSION, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> ILLUSION_BOOTS_OF_WANDER = ITEMS.register("illusionbootsofwander", () ->
            new IllusionBootsArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> FEL_ARMORED_HELM = ITEMS.register("felarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDFELTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FEL_ARMORED_ROBE = ITEMS.register("felarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDFELTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> DARK_ARMORED_HELM = ITEMS.register("darkarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_ARMORED_ROBE = ITEMS.register("darkarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_ARMORED_LEGGINGS = ITEMS.register("darkarmoredleggings", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> DARK_MASTER_HELM = ITEMS.register("darkmasterhelm", () ->
            new DarkMasterRobeArmor(ModArmorMaterial.DARKMASTERMAGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_MASTER_ROBE = ITEMS.register("darkmasterrobe", () ->
            new DarkMasterRobeArmor(ModArmorMaterial.DARKMASTERMAGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> DARK_MASTER_LEGGINGS = ITEMS.register("darkmasterleggings", () ->
            new DarkMasterRobeArmor(ModArmorMaterial.DARKMASTERMAGE, EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> NECRO_ARMORED_HELM = ITEMS.register("necroarmoredhelm", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> NECRO_ARMORED_ROBE = ITEMS.register("necroarmoredrobe", () ->
            new DarkArmoredRobeArmor(ModArmorMaterial.ARMOREDNECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));

    public static final RegistryObject<Item> CULTIST_HELM = ITEMS.register("cultisthelm", () ->
            new CultistRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> CULTIST_ROBE = ITEMS.register("cultistrobe", () ->
            new CultistRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties()));

    public static final RegistryObject<Item> CULTIST_ARMORED_HELM = ITEMS.register("cultistarmoredhelm", () ->
            new CultistArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> CULTIST_ARMORED_ROBE = ITEMS.register("cultistarmoredrobe", () ->
            new CultistArmoredRobeArmor(ModArmorMaterial.DARKARMOREDMAGE, EquipmentSlotType.CHEST, new Item.Properties()));

    public static final RegistryObject<Item> FROST_HELMET = ITEMS.register("frost_helmet", () ->
            new FrostArmor(EquipmentSlotType.HEAD, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FROST_CHESTPLATE = ITEMS.register("frost_chestplate", () ->
            new FrostArmor(EquipmentSlotType.CHEST, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FROST_LEGGINGS = ITEMS.register("frost_leggings", () ->
            new FrostArmor(EquipmentSlotType.LEGS, new Item.Properties().tab(Goety.TAB)));
    public static final RegistryObject<Item> FROST_BOOTS = ITEMS.register("frost_boots", () ->
            new FrostArmor(EquipmentSlotType.FEET, new Item.Properties().tab(Goety.TAB)));

    //JEI
    public static final RegistryObject<DummyItem> JEI_DUMMY_NONE = ITEMS.register(
            "jei_dummy/none", () -> new DummyItem(new Item.Properties()));
    public static final RegistryObject<DummyItem> JEI_DUMMY_REQUIRE_SACRIFICE = ITEMS.register(
            "jei_dummy/sacrifice", () -> new DummyItem(new Item.Properties()));
    public static final RegistryObject<DummyItem> JEI_DUMMY_REQUIRE_ITEM_USE = ITEMS.register(
            "jei_dummy/item", () -> new DummyItem(new Item.Properties()));
}
