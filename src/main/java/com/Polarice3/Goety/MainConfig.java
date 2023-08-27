package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.util.List;

/**
 * Learned how to add Config from codes by @AlexModGuy
 */
public class MainConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxArcaSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulGuiHorizontal;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulGuiVertical;

    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerAssaultSpawnFreq;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerAssaultSpawnChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerAssaultSEThreshold;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerAssaultSELimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> AnthropodSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VillagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PiglinSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderDragonSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PlayerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> DefaultSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> CraftingSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> MRabbitMax;

    public static final ForgeConfigSpec.ConfigValue<Integer> DarkArmoredRobeRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> EmeraldAmuletSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> NecroSoulSandSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> ItemsRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> DarkScytheSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PendantOfHungerLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchBowSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> FanaticPitchforkChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FanaticWitchBombChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> VillagerHateSpells;
    public static final ForgeConfigSpec.ConfigValue<Integer> LichHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulKilnCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> DarkManorSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> DarkManorSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> PortalOutpostSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> PortalOutpostSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> CursedGraveyardSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> CursedGraveyardSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> SalvagedFortSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> SalvagedFortSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> DecrepitFortSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> DecrepitFortSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> RuinedRitualSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> RuinedRitualSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> CrimsonShrineSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> CrimsonShrineSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> WarpedShrineSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> WarpedShrineSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> ValleyShrineSpacing;
    public static final ForgeConfigSpec.ConfigValue<Integer> ValleyShrineSeperation;

    public static final ForgeConfigSpec.ConfigValue<Integer> DredenSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrbhadhachSpawnWeight;

    public static final ForgeConfigSpec.ConfigValue<Double> DarkStaffDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ScytheBaseDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ScytheAttackSpeed;
    public static final ForgeConfigSpec.ConfigValue<Double> DeathScytheDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathScytheDurability;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathScytheEnchantability;

    public static final ForgeConfigSpec.ConfigValue<Double> WarpedSpearDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> WarpedSpearDurability;
    public static final ForgeConfigSpec.ConfigValue<Integer> WarpedSpearEnchantability;

    public static final ForgeConfigSpec.ConfigValue<Double> FrostTierDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> FrostTierMiningSpeed;
    public static final ForgeConfigSpec.ConfigValue<Integer> FrostTierDurability;
    public static final ForgeConfigSpec.ConfigValue<Integer> FrostTierLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> FrostTierEnchantability;

    public static final ForgeConfigSpec.ConfigValue<Double> PhilosophersMaceDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhilosophersMaceDurability;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SpecialBossBar;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BossMusic;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandStoneSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDarkSky;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDarkSkyNoOcclude;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandQuickSand;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDesiccate;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandMobs;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> HookBellBlackList;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulRepair;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ArcaUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterTotem;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterBook;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulGuiShow;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ShowNum;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagueSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerSteal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerAssault;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulEnergyBadOmen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerRaid;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistRaid;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VillagerHate;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WitchConversion;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistPilgrimage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TallSkullDrops;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WraithAggressiveTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ApocalypseMode;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ScytheSlashBreaks;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DarkManorGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PortalOutpostGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CursedGraveyardGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SalvagedFortGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DecrepitFortGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RuinedRitualGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CrimsonShrineGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WarpedShrineGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ValleyShrineGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GloomTreeGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MurkTreeGen;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VizierMinion;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UrbhadhachThrall;
    public static final ForgeConfigSpec.ConfigValue<Boolean> InterDimensionalMobs;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GoldenKingSpawn;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichNightVision;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichDamageHelmet;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichUndeadFriends;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichMagicResist;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichPowerfulFoes;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichScrollRequirement;

    public static final ForgeConfigSpec.ConfigValue<Boolean> FancierApostleDeath;

    static {
        BUILDER.push("General");
        MaxSouls = BUILDER.comment("Totem Maximum Soul Count and Threshold to save the Player, Default: 10000")
                .defineInRange("maxSouls", 10000, 10, Integer.MAX_VALUE);
        MaxArcaSouls = BUILDER.comment("Arca Maximum Soul Count, Default: 100000")
                .defineInRange("maxArcaSouls", 100000, 10, Integer.MAX_VALUE);
        SoulRepair = BUILDER.comment("Certain Items repair themselves using Soul Energy, Default: true")
                .define("soulRepair", true);
        TotemUndying = BUILDER.comment("Totem of Souls will save the Player if full of Soul Energy and its in Curio Slot or inventory, Default: true")
                .define("totemUndying", true);
        ArcaUndying = BUILDER.comment("Arca will save the Player if past Totem Maximum Soul Count, Default: true")
                .define("arcaUndying", true);
        StarterTotem = BUILDER.comment("Gives Players a Totem of Souls when first entering World, Default: false")
                .define("starterTotem", false);
        StarterBook = BUILDER.comment("Gives Players the Black Book when first entering World and Patchouli is loaded, Default: false")
                .define("starterBook", false);
        CraftingSouls = BUILDER.comment("How much Souls is consumed when crafting with Totem, Default: 1")
                .defineInRange("craftSouls", 1, 0, Integer.MAX_VALUE);
        SoulGuiShow = BUILDER.comment("Show the Soul Energy Bar if Player has Totem of Souls/Arca, Default: true")
                .define("soulGuiShow", true);
        ShowNum = BUILDER.comment("Show numerical amount of Souls on the Soul Energy Bar, Default: false")
                .define("showNumber", false);
        SoulGuiHorizontal = BUILDER.comment("Horizontal Position of where the Soul Energy Bar is located, Default: 100")
                .defineInRange("soulGuiHorizontal", 100, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        SoulGuiVertical = BUILDER.comment("Vertical Position of where the Soul Energy Bar is located, Default: -5")
                .defineInRange("soulGuiVertical", -5, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        ApocalypseMode = BUILDER.comment("Nether Meteors deals environmental damage. WARNING: Causes lots of lag. Default: false")
                .define("apocalypseMode", false);
        SpecialBossBar = BUILDER.comment("Bosses from the Mod has custom looking Boss Bars. Default: true")
                .define("specialBossBar", true);
        BossMusic = BUILDER.comment("Bosses from the Mod has custom Music Playing. Default: true")
                .define("bossMusic", true);
        BUILDER.pop();
        BUILDER.push("Blocks");
        HookBellBlackList = BUILDER.comment(" Add mobs that Hook Bells don't work on. To do so, enter the namespace ID of the mob, like 'minecraft:zombie, minecraft:skeleton'")
                .defineList("hookBellBlackList", Lists.newArrayList(),
                        (itemRaw) -> itemRaw instanceof String);
        DeadSandSpread = BUILDER.comment("Dead Sand can Spread to other Blocks, Default: true")
                .define("deadSandSpread", true);
        DeadSandStoneSpread = BUILDER.comment("Dead Sandstone have a 5% chance of spreading itself instead of just Dead Sands, disable so that they only spread Dead Sands, Default: false")
                .define("deadSandstoneSpread", false);
        DeadSandDarkSky = BUILDER.comment("Dead Sand will produce a Dark Cloud at the height of the world if surrounded by other Dead Blocks, Default: false")
                .define("deadSandDarkSky", false);
        DeadSandDarkSkyNoOcclude = BUILDER.comment("Dark Cloud will be produced if the Dead Sand cannot see the sky, Default: true")
                .define("deadSandDarkSkyNoOcclude", true);
        DeadSandQuickSand = BUILDER.comment("Dead Sand will convert nearby Water Blocks into Quicksand Blocks, Default: true")
                .define("deadSandQuicksand", true);
        DeadSandDesiccate = BUILDER.comment("Dead Sands will desiccate mobs, slowly killing them, Default: true")
                .define("deadSandDesiccate", true);
        DeadSandMobs = BUILDER.comment("Allows special mobs to spawn/converted when there's Dead Sand around, Default: true")
                .define("deadSandMobs", true);
        SoulKilnCost = BUILDER.comment("The amount of Soul Energy used up to smelt items per second, Default: 1")
                .defineInRange("soulKilnFuel", 1, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Soul Taken");
        UndeadSouls = BUILDER.comment("Undead Killed, Default: 5")
                .defineInRange("undeadSouls", 5, 0, Integer.MAX_VALUE);
        AnthropodSouls = BUILDER.comment("Anthropods Killed, Default: 5")
                .defineInRange("anthropodSouls", 5, 0, Integer.MAX_VALUE);
        IllagerSouls = BUILDER.comment("Illagers, Witches, Cultists Killed, Default: 25")
                .defineInRange("illagerSouls", 25, 0, Integer.MAX_VALUE);
        VillagerSouls = BUILDER.comment("Villagers Killed, Default: 100")
                .defineInRange("villagerSouls", 100, 0, Integer.MAX_VALUE);
        PiglinSouls = BUILDER.comment("Non-Undead Piglin Killed, Default: 10")
                .defineInRange("piglinSouls", 10, 0, Integer.MAX_VALUE);
        EnderDragonSouls = BUILDER.comment("Ender Dragon Killed, Default: 1000")
                .defineInRange("enderDragonSouls", 1000, 0, Integer.MAX_VALUE);
        PlayerSouls = BUILDER.comment("Players Killed, Default: 100")
                .defineInRange("playerSouls", 100, 0, Integer.MAX_VALUE);
        DefaultSouls = BUILDER.comment("Others Killed, Default: 5")
                .defineInRange("otherSouls", 5, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Mobs");
        MRabbitMax = BUILDER.comment("Maximum amount of Mutant Rabbits can spawn, Default: 16")
                .defineInRange("mutatedRabbitMax", 16, 0, 100);
        VizierMinion = BUILDER.comment("Viziers spawn Vexes instead of Irks, Default: false")
                .define("vizierMinion", false);
        FancierApostleDeath = BUILDER.comment("Gives Apostle an even more fancier death animation, Default: false")
                .define("fancierApostleDeath", false);
        FanaticPitchforkChance = BUILDER.comment("Chance for the Fanatic mob to spawn with a Pitchfork, Default: 16")
                .defineInRange("fanaticPitchforkChance", 16, 0, Integer.MAX_VALUE);
        FanaticWitchBombChance = BUILDER.comment("Chance for the Fanatic mob to spawn with a Witch's Bomb, Default: 4")
                .defineInRange("fanaticWitchBombChance", 4, 0, Integer.MAX_VALUE);
        InterDimensionalMobs = BUILDER.comment("Whether Goety Mobs can spawn in Overworld-like modded dimensions, Default: false")
                .define("interDimensionalMobs", false);
        DredenSpawnWeight = BUILDER.comment("Spawn Weight for Dreden, Default: 20")
                .defineInRange("dredenSpawnWeight", 20, 0, Integer.MAX_VALUE);
        WraithSpawnWeight = BUILDER.comment("Spawn Weight for Wraith, Default: 20")
                .defineInRange("wraithSpawnWeight", 20, 0, Integer.MAX_VALUE);
        UrbhadhachSpawnWeight = BUILDER.comment("Spawn Weight for Urbhadhach, Default: 16")
                .defineInRange("urbhadhachSpawnWeight", 16, 0, Integer.MAX_VALUE);
        UrbhadhachThrall = BUILDER.comment("Whether Urbhadhachs try to enthrall baby mobs to it, Default: false")
                .define("urbhadhachEnthrall", false);
        WitchConversion = BUILDER.comment("Whether Witches will convert to Beldams if near Cultists, Default: true")
                .define("witchConversion", true);
        CultistPilgrimage = BUILDER.comment("Whether Cultists will occasionally spawn within the Nether in groups much akin to Patrols, Default: true")
                .define("cultistPilgrimage", true);
        CultistRaid = BUILDER.comment("Whether Cultists will appear in raids if players are nearby, Default: false")
                .define("cultistRaid", false);
        TallSkullDrops = BUILDER.comment("Whether Mobs with Tall Heads(ie. Villagers, Illagers, etc.) will drop Tall Skulls, Default: true")
                .define("tallSkullDrop", true);
        WraithAggressiveTeleport = BUILDER.comment("Whether Wraiths should teleport towards their targets if they can't see them instead of just teleporting away when they're near them, Default: true")
                .define("wraithAggressiveTeleport", true);
        GoldenKingSpawn = BUILDER.comment("Whether Golden King Fish Mob can spawn, Default: true")
                .define("goldenKingSpawn", true);
        BUILDER.pop();
        BUILDER.push("Structure Generation");
        DarkManorGen = BUILDER.comment("Dark Manor Generates in the World, Default: true")
                .define("darkManorGen", true);
        DarkManorSpacing = BUILDER.comment("Spacing for Dark Manors, Default: 60")
                .defineInRange("darkManorSpacing", 60, 0, Integer.MAX_VALUE);
        DarkManorSeperation = BUILDER.comment("Separation for Dark Manors, Default: 20")
                .defineInRange("darkManorSeparation", 20, 0, Integer.MAX_VALUE);
        PortalOutpostGen = BUILDER.comment("Portal Outpost Generates in the World, Default: true")
                .define("portalOutpostGen", true);
        PortalOutpostSpacing = BUILDER.comment("Spacing for Portal Outposts, Default: 40")
                .defineInRange("portalOutpostSpacing", 40, 0, Integer.MAX_VALUE);
        PortalOutpostSeperation = BUILDER.comment("Separation for Portal Outposts, Default: 20")
                .defineInRange("portalOutpostSeparation", 20, 0, Integer.MAX_VALUE);
        CursedGraveyardGen = BUILDER.comment("Cursed Graveyard Generates in the World, Default: true")
                .define("cursedGraveyardGen", true);
        CursedGraveyardSpacing = BUILDER.comment("Spacing for Cursed Graveyards, Default: 32")
                .defineInRange("cursedGraveyardSpacing", 32, 0, Integer.MAX_VALUE);
        CursedGraveyardSeperation = BUILDER.comment("Separation for Cursed Graveyards, Default: 5")
                .defineInRange("cursedGraveyardSeparation", 5, 0, Integer.MAX_VALUE);
        SalvagedFortGen = BUILDER.comment("Salvaged Fort Generates in the World, Default: true")
                .define("salvagedFortGen", true);
        SalvagedFortSpacing = BUILDER.comment("Spacing for Salvaged Forts, Default: 32")
                .defineInRange("salvagedFortSpacing", 32, 0, Integer.MAX_VALUE);
        SalvagedFortSeperation = BUILDER.comment("Separation for Salvaged Forts, Default: 8")
                .defineInRange("salvagedFortSeparation", 8, 0, Integer.MAX_VALUE);
        DecrepitFortGen = BUILDER.comment("Decrepit Fort Generates in the World, Default: true")
                .define("decrepitFortGen", true);
        DecrepitFortSpacing = BUILDER.comment("Spacing for Decrepit Forts, Default: 32")
                .defineInRange("decrepitFortSpacing", 32, 0, Integer.MAX_VALUE);
        DecrepitFortSeperation = BUILDER.comment("Separation for Decrepit Forts, Default: 8")
                .defineInRange("decrepitFortSeparation", 8, 0, Integer.MAX_VALUE);
        RuinedRitualGen = BUILDER.comment("Ruined Ritual Generates in the World, Default: true")
                .define("ruinedRitualGen", true);
        RuinedRitualSpacing = BUILDER.comment("Spacing for Ruined Rituals, Default: 45")
                .defineInRange("ruinedRitualSpacing", 45, 0, Integer.MAX_VALUE);
        RuinedRitualSeperation = BUILDER.comment("Separation for Ruined Rituals, Default: 10")
                .defineInRange("ruinedRitualSeparation", 10, 0, Integer.MAX_VALUE);
        CrimsonShrineGen = BUILDER.comment("Crimson Shrine Generates in the World, Default: true")
                .define("crimsonShrineGen", true);
        CrimsonShrineSpacing = BUILDER.comment("Spacing for Crimson Shrines, Default: 32")
                .defineInRange("crimsonShrineSpacing", 32, 0, Integer.MAX_VALUE);
        CrimsonShrineSeperation = BUILDER.comment("Separation for Crimson Shrines, Default: 10")
                .defineInRange("crimsonShrineSeparation", 10, 0, Integer.MAX_VALUE);
        WarpedShrineGen = BUILDER.comment("Warped Shrine Generates in the World, Default: true")
                .define("warpedShrineGen", true);
        WarpedShrineSpacing = BUILDER.comment("Spacing for Warped Shrines, Default: 32")
                .defineInRange("warpedShrineSpacing", 32, 0, Integer.MAX_VALUE);
        WarpedShrineSeperation = BUILDER.comment("Separation for Warped Shrines, Default: 10")
                .defineInRange("warpedShrineSeparation", 10, 0, Integer.MAX_VALUE);
        ValleyShrineGen = BUILDER.comment("Valley Shrine Generates in the World, Default: true")
                .define("valleyShrineGen", true);
        ValleyShrineSpacing = BUILDER.comment("Spacing for Valley Shrines, Default: 32")
                .defineInRange("valleyShrineSpacing", 32, 0, Integer.MAX_VALUE);
        ValleyShrineSeperation = BUILDER.comment("Separation for Valley Shrines, Default: 10")
                .defineInRange("valleyShrineSeparation", 10, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("World Generation");
        TotemGen = BUILDER.comment("Totems Generates in the World, Default: true")
                .define("totemGen", true);
        GloomTreeGen = BUILDER.comment("Gloom Trees Generates in the World, Default: true")
                .define("gloomTreeGen", true);
        MurkTreeGen = BUILDER.comment("Murk Trees Generates in the World, Default: true")
                .define("murkTreeGen", true);
        BUILDER.pop();
        BUILDER.push("Items");
        DarkArmoredRobeRepairAmount = BUILDER.comment("Amount of Souls needed to repair Dark Armored Robes per second, Default: 10")
                .defineInRange("darkArmoredRobeRepairSouls", 10, 1, Integer.MAX_VALUE);
        EmeraldAmuletSouls = BUILDER.comment("Amount of Soul Energy Emerald Amulet gives every 5 seconds, Default: 1")
                .defineInRange("emeraldAmuletSouls", 1, 1, Integer.MAX_VALUE);
        NecroSoulSandSouls = BUILDER.comment("Amount of Soul Energy Necro Boots of Wander gives when running on Soul Sand Blocks, Default: 1")
                .defineInRange("necroSoulSandSouls", 1, 1, Integer.MAX_VALUE);
        DarkScytheSouls = BUILDER.comment("Amount of Soul Energy Dark Scythe gives when hitting mob(s), Default: 1")
                .defineInRange("darkScytheSouls", 1, 1, Integer.MAX_VALUE);
        ScytheSlashBreaks = BUILDER.comment("Scythe Slashes from Death Scythe breaks blocks that regular Scythes easily breaks, Default: true")
                .define("scytheSlashBreaks", true);
        WitchBowSouls = BUILDER.comment("Amount of Soul Energy Witch's Bow required to shoot a tipped Arrow, Default: 25")
                .defineInRange("witchBowSouls", 25, 1, Integer.MAX_VALUE);
        ItemsRepairAmount = BUILDER.comment("Amount of Souls needed to repair certain Equipments per second, Default: 5")
                .defineInRange("darkArmoredRobeRepairSouls", 5, 1, Integer.MAX_VALUE);
        PendantOfHungerLimit = BUILDER.comment("The total amount of Rotten Flesh a Pendant of Hunger can hold, Default: 256")
                .defineInRange("pendantOfHungerLimit", 256, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Tools & Weapons");
        DarkStaffDamage = BUILDER.comment("How much base damage Dark Staffs deals, Default: 4.0")
                .defineInRange("darkStaffDamage", 4.0, 1.0, Double.MAX_VALUE);
        ScytheBaseDamage = BUILDER.comment("How much base damage Scythes deals, the damage added depends on material the scythe is made off (ie. Iron = 2.0), Default: 5.5")
                .defineInRange("scytheBaseDamage", 5.5, 1.0, Double.MAX_VALUE);
        ScytheAttackSpeed = BUILDER.comment("How fast it takes to fully swing a Scythe with item offhand and not wearing Grave Gloves. The lower the number the slower it takes to recharge, Default: 0.6")
                .defineInRange("scytheAttackSpeed", 0.6, 0.0, Double.MAX_VALUE);
        DeathScytheDamage = BUILDER.comment("How much damage Death Scythe deals, the configured number is added to Scythe Base Damage, Default: 4.0")
                .defineInRange("deathScytheDamage", 4.0, 1.0, Double.MAX_VALUE);
        DeathScytheDurability = BUILDER.comment("How many uses before Death Scythe breaks, Default: 444")
                .defineInRange("deathScytheDurability", 444, 1, Integer.MAX_VALUE);
        DeathScytheEnchantability = BUILDER.comment("Define the Enchantability for Death Scythe, higher number the better, Default: 22")
                .defineInRange("deathScytheEnchantability", 22, 1, Integer.MAX_VALUE);
        WarpedSpearDamage = BUILDER.comment("How much damage Warped Spear deals, Default: 10.0")
                .defineInRange("warpedSpearDamage", 10.0, 1.0, Double.MAX_VALUE);
        WarpedSpearDurability = BUILDER.comment("How many uses before Warped Spear breaks, Default: 250")
                .defineInRange("warpedSpearDurability", 250, 1, Integer.MAX_VALUE);
        WarpedSpearEnchantability = BUILDER.comment("Define the Enchantability for Warped Spear, higher number the better, Default: 1")
                .defineInRange("warpedSpearEnchantability", 1, 1, Integer.MAX_VALUE);
        PhilosophersMaceDamage = BUILDER.comment("How much damage Philosopher's Mace deals, Default: 9.0")
                .defineInRange("philosophersMaceDamage", 9.0, 1.0, Double.MAX_VALUE);
        PhilosophersMaceDurability = BUILDER.comment("How many uses before the Philosopher's Mace breaks, Default: 128")
                .defineInRange("philosophersMaceDurability", 128, 1, Integer.MAX_VALUE);
        FrostTierDamage = BUILDER.comment("How much damage Frost items deals, for swords the damage defined is added by 4.0 so by default, a Frost Sword would deal 6.5 Damage, Default: 2.5")
                .defineInRange("frostTierDamage", 2.5, 1.0, Double.MAX_VALUE);
        FrostTierMiningSpeed = BUILDER.comment("How fast Frost Items destroy blocks, higher number the faster (Hint: Iron has 6.0 Mining Speed), Default: 7.0")
                .defineInRange("frostTierMiningSpeed", 7.0, 1.0, Double.MAX_VALUE);
        FrostTierDurability = BUILDER.comment("How many uses before a Frost Item breaks, Default: 1000")
                .defineInRange("frostTierDurability", 1000, 1, Integer.MAX_VALUE);
        FrostTierLevel = BUILDER.comment("Define the Mining Level for Frost Tools (0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond, 4 = Netherite, 5+ = Above Netherite), Default: 3")
                .defineInRange("frostTierLevel", 3, 0, Integer.MAX_VALUE);
        FrostTierEnchantability = BUILDER.comment("Define the Enchantability for Frost Items, higher number the better, Default: 20")
                .defineInRange("frostTierEnchantability", 20, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Villagers");
        VillagerHate = BUILDER.comment("Wearing a Dark Helm and Robe, along with variants, causes Villagers around the Player to have a negative Reputation unless said Player has 100 or more reputation among them, Default: false")
                .define("villagerHate", false);
        VillagerHateSpells = BUILDER.comment("Casting Spell in the presence of Villagers will cause the Player to lose a number of Reputation, set 0 to disable, Default: 0")
                .defineInRange("villagerHateSpells", 0, 0, Integer.MAX_VALUE);
        CultistSpread = BUILDER.comment("Whether Villagers are able to become secret Cultists, Default: true")
                .define("cultistSpread", true);
        BUILDER.pop();
        BUILDER.push("Lich");
        LichHealCost = BUILDER.comment("How much Soul Energy is cost to heal the Player per second if they've become a Lich, Default: 1")
                .defineInRange("lichHealCost", 1, 0, Integer.MAX_VALUE);
        LichNightVision = BUILDER.comment("Enable to get infinite Night Vision when being a Lich. If set true, wearing Fel Helm will no longer give Blindness during day, Default: true")
                .define("lichNightVision", true);
        LichDamageHelmet = BUILDER.comment("Wearing Helmet in Sunlight as a Lich periodically damages it, Default: true")
                .define("lichDamageHelmet", true);
        LichUndeadFriends = BUILDER.comment("Undead Mobs will not attack you if you're a Lich and will even defend you if you're attack by another mob and wearing the Necro Set, Default: true")
                .define("lichUndeadFriendly", true);
        LichMagicResist = BUILDER.comment("Enable to make Liches 85% more resistant to Magic Attacks, Default: false")
                .define("lichMagicResist", false);
        LichPowerfulFoes = BUILDER.comment("If Lich Undead Friendly is set to true, Only undead that have lower than 50 Hearts are friendly, Default: true")
                .define("lichPowerfulHostile", true);
        LichScrollRequirement = BUILDER.comment("Whether the player needs to read a Forbidden Scroll to start the Potion of Transformation ritual, Default: true")
                .define("lichScrollRequirement", true);
        BUILDER.pop();
        BUILDER.push("Illagers");
        IllagerAssault = BUILDER.comment("Special Illagers Spawning based of Player's Soul Energy amount, Default: true")
                .define("illagerAssault", true);
        IllagerAssaultSpawnFreq = BUILDER.comment("Spawn Frequency for Illagers Hunting the Player, Default: 12000")
                .defineInRange("illagerAssaultSpawnFreq", 12000, 0, Integer.MAX_VALUE);
        IllagerAssaultSpawnChance = BUILDER.comment("Spawn Chance for Illagers Hunting the Player every Infamy Spawn Frequency, the lower the more likelier, Default: 5")
                .defineInRange("illagerAssaultSpawnChance", 5, 0, Integer.MAX_VALUE);
        IllagerAssaultSEThreshold = BUILDER.comment("How much Soul Energy the Player has is required for Special Illagers to spawn, Default: 2500")
                .defineInRange("illagerAssaultThreshold", 2500, 0, Integer.MAX_VALUE);
        IllagerAssaultSELimit = BUILDER.comment("The maximum amount of Soul Energy the Player has that is taken consideration for the Assaults, Default: 30000")
                .defineInRange("illagerAssaultLimit", 30000, 0, Integer.MAX_VALUE);
        SoulEnergyBadOmen = BUILDER.comment("Hitting the Illager Assault Limit of Soul Energy have a chance of giving Player Bad Omen effect, Default: true")
                .define("soulEnergyBadOmen", true);
        IllagueSpread = BUILDER.comment("Whether Illague Effect can spread from non Conquillagers that has the effect, Default: true")
                .define("illagueSpread", true);
        IllagerSteal = BUILDER.comment("Whether Enviokers, Inquillagers and Conquillagers can steal Totems of Souls or Totems of Undying, Default: true")
                .define("illagerSteal", true);
        IllagerRaid = BUILDER.comment("Whether Enviokers, Inquillagers and Conquillagers can join Raids, Default: true")
                .define("specialIllagerRaid", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        config.setConfig(file);
    }

}
