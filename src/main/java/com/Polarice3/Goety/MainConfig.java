package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

/**
 * Learned how to add Config from codes by @AlexModGuy
 */
public class MainConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxArcaSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderAcidCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> PoisonballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireBreathCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FrostBreathCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulLightCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> GlowLightCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderAcidInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireballInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> PoisonballInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireBreathInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FrostBreathInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomInfamyChance;

    public static final ForgeConfigSpec.ConfigValue<Integer> PillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> VindicatorInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> EvokerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnviokerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> InquillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> ConquillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> VizierInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> ScryingInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> PowerfulInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> OtherInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathLoseInfamy;

    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterXPCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithCooldown;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> AnthropodSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VillagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PiglinSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderDragonSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PlayerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> DefaultSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSoulEaterLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxWantingLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxPotencyLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRadiusLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRangeLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxDurationLevel;

    public static final ForgeConfigSpec.ConfigValue<Integer> CraftingSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpawnFreq;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpawnChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamyMax;
    public static final ForgeConfigSpec.ConfigValue<Integer> MRabbitMax;
    public static final ForgeConfigSpec.ConfigValue<Integer> WandVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> StaffVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadMinionHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TamedSpiderHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpellGive;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamyThreshold;

    public static final ForgeConfigSpec.ConfigValue<Integer> DarkArmoredRobeRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> EmeraldAmuletSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> NecroSoulSandSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> ItemsRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> DarkScytheSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchBowSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> ApostleDamageCap;
    public static final ForgeConfigSpec.ConfigValue<Integer> ApostleBowDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> VizierDamageCap;
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

    public static final ForgeConfigSpec.ConfigValue<Integer> DredenSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrbhadhachSpawnWeight;

    public static final ForgeConfigSpec.ConfigValue<Double> CreeperlingExplosionRadius;

    public static final ForgeConfigSpec.ConfigValue<Boolean> InfamySpawn;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SpecialBossBar;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandStoneSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDarkSky;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDarkSkyNoOcclude;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandQuickSand;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandDesiccate;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandMobs;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulRepair;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ArcaUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterTotem;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterBook;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullZombie;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullSkeleton;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullMinionWander;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VexTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ShowNum;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsAttackCreepers;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsMasterImmune;
    public static final ForgeConfigSpec.ConfigValue<Boolean> OwnerAttackCancel;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadMinionHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TamedSpiderHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RoyalSpiderMinions;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagueSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerSteal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> InfamySpell;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerRaid;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VillagerHate;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WitchConversion;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistPilgrimage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TallSkullDrops;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ApocalypseMode;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DarkManorGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PortalOutpostGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CursedGraveyardGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SalvagedFortGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DecrepitFortGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RuinedRitualGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GloomTreeGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MurkTreeGen;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VizierMinion;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UrbhadhachThrall;
    public static final ForgeConfigSpec.ConfigValue<Boolean> InterDimensionalMobs;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GoldenKingSpawn;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichNightVision;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichUndeadFriends;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LichPowerfulFoes;

    static {
        BUILDER.push("General");
        MaxSouls = BUILDER.comment("Totem Maximum Soul Count and Threshold to save the Player, Default: 10000")
                .defineInRange("maxSouls", 10000, 10, Integer.MAX_VALUE);
        MaxArcaSouls = BUILDER.comment("Arca Maximum Soul Count, Default: 100000")
                .defineInRange("maxArcaSouls", 100000, 10, Integer.MAX_VALUE);
        SoulRepair = BUILDER.comment("Certain Items repair themselves using Soul Energy, Default: true")
                .define("soulRepair", true);
        TotemUndying = BUILDER.comment("Totem of Souls will save the Player if full of Soul Energy, Default: true")
                .define("totemUndying", true);
        ArcaUndying = BUILDER.comment("Arca will save the Player if past Totem Maximum Soul Count, Default: true")
                .define("arcaUndying", true);
        StarterTotem = BUILDER.comment("Gives Players a Totem of Souls when first entering World, Default: false")
                .define("starterTotem", false);
        StarterBook = BUILDER.comment("Gives Players the Black Book when first entering World and Patchouli is loaded, Default: false")
                .define("starterBook", false);
        CraftingSouls = BUILDER.comment("How much Souls is consumed when crafting with Totem, Default: 1")
                .defineInRange("craftSouls", 1, 0, Integer.MAX_VALUE);
        ShowNum = BUILDER.comment("Show numerical amount of Souls on the Soul Energy Bar, Default: false")
                .define("showNumber", false);
        ApocalypseMode = BUILDER.comment("Nether Meteors deals environmental damage. WARNING: Causes lots of lag. Default: false")
                .define("apocalypseMode", false);
        SpecialBossBar = BUILDER.comment("Bosses from the Mod has custom looking Boss Bars. Default: true")
                .define("specialBossBar", true);
        BUILDER.pop();
        BUILDER.push("Blocks");
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
        BUILDER.push("Spell Costs");
        VexCost = BUILDER.comment("Vex Spell Cost, Default: 18")
                .defineInRange("vexCost", 18, 0, Integer.MAX_VALUE);
        FangCost = BUILDER.comment("Fang Spell Cost, Default: 8")
                .defineInRange("fangCost", 8, 0, Integer.MAX_VALUE);
        RoarCost = BUILDER.comment("Roaring Spell Cost, Default: 10")
                .defineInRange("biteCost", 10, 0, Integer.MAX_VALUE);
        ZombieCost = BUILDER.comment("Rotting Spell Cost, Default: 5")
                .defineInRange("zombieCost", 5, 0, Integer.MAX_VALUE);
        SkeletonCost = BUILDER.comment("Osseous Spell Cost, Default: 8")
                .defineInRange("skeletonCost", 8, 0, Integer.MAX_VALUE);
        DredenCost = BUILDER.comment("Rigid Spell Cost, Default: 16")
                .defineInRange("dredenCost", 16, 0, Integer.MAX_VALUE);
        UndeadWolfCost = BUILDER.comment("Hounding Spell Cost, Default: 2")
                .defineInRange("undeadWolfCost", 2, 0, Integer.MAX_VALUE);
        PhantomCost = BUILDER.comment("Phantasm Spell Cost, Default: 16")
                .defineInRange("phantomCost", 16, 0, Integer.MAX_VALUE);
        WraithCost = BUILDER.comment("Spooky Spell Cost, Default: 24")
                .defineInRange("wraithCost", 24, 0, Integer.MAX_VALUE);
        WitchGaleCost = BUILDER.comment("Witch's Gale Spell Cost, Default: 15")
                .defineInRange("crippleCost", 15, 0, Integer.MAX_VALUE);
        SpiderlingCost = BUILDER.comment("Spiderling Spell Cost per second, Default: 2")
                .defineInRange("spiderlingCost", 2, 0, Integer.MAX_VALUE);
        BrainEaterCost = BUILDER.comment("Brain Eater Spell Cost per second, Default: 5")
                .defineInRange("brainCost", 5, 0, Integer.MAX_VALUE);
        BrainEaterXPCost = BUILDER.comment("How much Experience the above spell Cost per heal, Default: 40")
                .defineInRange("brainXpCost", 40, 0, 1000);
        TeleportCost = BUILDER.comment("Teleport Spell Cost, Default: 5")
                .defineInRange("teleportCost", 5, 0, Integer.MAX_VALUE);
        SoulSkullCost = BUILDER.comment("Soul Skull Spell Cost, Default: 16")
                .defineInRange("soulSkullCost", 16, 0, Integer.MAX_VALUE);
        FeastCost = BUILDER.comment("Feasting Spell Cost, Default: 8")
                .defineInRange("feastCost", 8, 0, Integer.MAX_VALUE);
        TemptingCost = BUILDER.comment("Tempting Spell Cost, Default: 2")
                .defineInRange("temptingCost", 2, 0, Integer.MAX_VALUE);
        EnderAcidCost = BUILDER.comment("Ender Acid Breath Spell Cost, Default: 6")
                .defineInRange("enderAcidBreathCost", 6, 0, Integer.MAX_VALUE);
        DragonFireballCost = BUILDER.comment("Dragon Fireball Spell Cost, Default: 64")
                .defineInRange("dragonFireballCost", 64, 0, Integer.MAX_VALUE);
        CreeperlingCost = BUILDER.comment("Creeperling Spell Cost per second, Default: 8")
                .defineInRange("creeperlingCost", 8, 0, Integer.MAX_VALUE);
        BreathingCost = BUILDER.comment("Breathing Spell Cost per second, Default: 2")
                .defineInRange("breathingCost", 2, 0, Integer.MAX_VALUE);
        FireballCost = BUILDER.comment("Fireball Spell Cost, Default: 4")
                .defineInRange("fireballCost", 4, 0, Integer.MAX_VALUE);
        LavaballCost = BUILDER.comment("Lava Bomb Spell Cost, Default: 16")
                .defineInRange("lavaBombCost", 16, 0, Integer.MAX_VALUE);
        PoisonballCost = BUILDER.comment("Poison Ball Spell Cost, Default: 4")
                .defineInRange("poisonBallCost", 4, 0, Integer.MAX_VALUE);
        IllusionCost = BUILDER.comment("Illusion Spell Cost, Default: 20")
                .defineInRange("illusionCost", 20, 0, Integer.MAX_VALUE);
        FireBreathCost = BUILDER.comment("Fire Breath Spell Cost per second, Default: 2")
                .defineInRange("fireBreathCost", 2, 0, Integer.MAX_VALUE);
        FrostBreathCost = BUILDER.comment("Frost Breath Spell Cost per second, Default: 2")
                .defineInRange("frostBreathCost", 2, 0, Integer.MAX_VALUE);
        SoulLightCost = BUILDER.comment("Soul Light Spell Cost, Default: 2")
                .defineInRange("soulLightCost", 2, 0, Integer.MAX_VALUE);
        GlowLightCost = BUILDER.comment("Glow Light Spell Cost, Default: 4")
                .defineInRange("glowLightCost", 4, 0, Integer.MAX_VALUE);
        IceStormCost = BUILDER.comment("Ice Storm Spell Cost, Default: 16")
                .defineInRange("iceStormCost", 16, 0, Integer.MAX_VALUE);
        LaunchCost = BUILDER.comment("Launch Spell Cost, Default: 4")
                .defineInRange("launchCost", 4, 0, Integer.MAX_VALUE);
        SonicBoomCost = BUILDER.comment("Sonic Boom Spell Cost, Default: 16")
                .defineInRange("sonicBoomCost", 16, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Casting Time");
        VexDuration = BUILDER.comment("Time to cast Vex Spell, Default: 100")
                .defineInRange("vexTime", 100, 0, 72000);
        FangDuration = BUILDER.comment("Time to cast Fang Spell, Default: 40")
                .defineInRange("fangTime", 40, 0, 72000);
        RoarDuration = BUILDER.comment("Time to cast Roaring Spell, Default: 40")
                .defineInRange("roarTime", 40, 0, 72000);
        ZombieDuration = BUILDER.comment("Time to cast Rotting Spell, Default: 20")
                .defineInRange("zombieTime", 20, 0, 72000);
        SkeletonDuration = BUILDER.comment("Time to cast Osseous Spell, Default: 60")
                .defineInRange("skeletonTime", 60, 0, 72000);
        DredenDuration = BUILDER.comment("Time to cast Rigid Spell, Default: 60")
                .defineInRange("dredenTime", 60, 0, 72000);
        UndeadWolfDuration = BUILDER.comment("Time to cast Hounding Spell, Default: 20")
                .defineInRange("undeadWolfTime", 20, 0, 72000);
        PhantomDuration = BUILDER.comment("Time to cast Phantasm Spell, Default: 60")
                .defineInRange("phantomTime", 60, 0, 72000);
        WraithDuration = BUILDER.comment("Time to cast Wraith Spell, Default: 60")
                .defineInRange("wraithTime", 60, 0, 72000);
        WitchGaleDuration = BUILDER.comment("Time to cast Witch's Gale Spell, Default: 20")
                .defineInRange("crippleTime", 20, 0, 72000);
        SpiderlingDuration = BUILDER.comment("Time to cast Spiderling Spell per second, Default: 10")
                .defineInRange("spiderlingTime", 10, 0, 72000);
        BrainEaterDuration = BUILDER.comment("Time to cast Brain Eater Spell per second, Default: 1")
                .defineInRange("brainEaterTime", 1, 0, 72000);
        FeastDuration = BUILDER.comment("Time to cast Feasting Spell per second, Default: 20")
                .defineInRange("feastTime", 20, 0, 72000);
        TemptingDuration = BUILDER.comment("Time to cast Tempting Spell per second, Default: 20")
                .defineInRange("temptingTime", 20, 0, 72000);
        DragonFireballDuration = BUILDER.comment("Time to cast Dragon Fireball Spell, Default: 30")
                .defineInRange("dragonFireballTime", 30, 0, 72000);
        CreeperlingDuration = BUILDER.comment("Time to cast Creeperling Spell per second, Default: 10")
                .defineInRange("creeperlingTime", 10, 0, 72000);
        BreathingDuration = BUILDER.comment("Time to cast Breathing Spell per second, Default: 1")
                .defineInRange("breathingTime", 1, 0, 72000);
        LavaballDuration = BUILDER.comment("Time to cast Lava Bomb Spell, Default: 20")
                .defineInRange("lavaBombTime", 20, 0, 72000);
        IllusionDuration = BUILDER.comment("Time to cast Illusion Spell, Default: 40")
                .defineInRange("illusionTime", 40, 0, 72000);
        IceStormDuration = BUILDER.comment("Time to cast Ice Storm Spell, Default: 60")
                .defineInRange("iceStormTime", 60, 0, 72000);
        SonicBoomDuration = BUILDER.comment("Time to cast Sonic Boom Spell, Default: 60")
                .defineInRange("sonicBoomTime", 60, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Summon Down Duration");
        VexCooldown = BUILDER.comment("Vex Spell Cooldown, Default: 340")
                .defineInRange("vexCooldown", 340, 0, 72000);
        ZombieCooldown = BUILDER.comment("Rotting Spell Cooldown, Default: 120")
                .defineInRange("zombieCooldown", 120, 0, 72000);
        SkeletonCooldown = BUILDER.comment("Osseous Spell Cooldown, Default: 280")
                .defineInRange("skeletonCooldown", 280, 0, 72000);
        DredenCooldown = BUILDER.comment("Rigid Spell Cooldown, Default: 300")
                .defineInRange("dredenCooldown", 300, 0, 72000);
        UndeadWolfCooldown = BUILDER.comment("Hounding Spell Cooldown, Default: 30")
                .defineInRange("undeadWolfCooldown", 30, 0, 72000);
        PhantomCooldown = BUILDER.comment("Phantasm Spell Cooldown, Default: 300")
                .defineInRange("phantomCooldown", 300, 0, 72000);
        WraithCooldown = BUILDER.comment("Spooky Spell Cooldown, Default: 300")
                .defineInRange("wraithCooldown", 300, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Mobs");
        MRabbitMax = BUILDER.comment("Maximum amount of Mutant Rabbits can spawn, Default: 16")
                .defineInRange("mutatedRabbitMax", 16, 0, 100);
        VizierMinion = BUILDER.comment("Viziers spawn Vexes instead of Irks, Default: false")
                .define("vizierMinion", false);
        VizierDamageCap = BUILDER.comment("How much maximum Damage a Vizier can attain per hit, Default: 20")
                .defineInRange("vizierDamageCap", 20, 1, Integer.MAX_VALUE);
        ApostleDamageCap = BUILDER.comment("How much maximum Damage an Apostle can attain per hit, Default: 20")
                .defineInRange("apostleDamageCap", 20, 1, Integer.MAX_VALUE);
        ApostleBowDamage = BUILDER.comment("Multiplies Apostle's Bow damage, Default: 4")
                .defineInRange("apostleBowDamage", 4, 2, Integer.MAX_VALUE);
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
        TallSkullDrops = BUILDER.comment("Whether Mobs with Tall Heads(ie. Villagers, Illagers, etc.) will drop Tall Skulls, Default: true")
                .define("tallSkullDrop", true);
        GoldenKingSpawn = BUILDER.comment("Whether Golden King Fish Mob can spawn, Default: true")
                .define("goldenKingSpawn", true);
        BUILDER.pop();
        BUILDER.push("Infamy");
        InfamySpawn = BUILDER.comment("Special Illagers Spawning due to Infamy, Default: true")
                .define("infamySpawn", true);
        InfamySpawnFreq = BUILDER.comment("Spawn Frequency for Illagers Hunting the Player, Default: 12000")
                .defineInRange("infamySpawnFreq", 12000, 0, Integer.MAX_VALUE);
        InfamySpawnChance = BUILDER.comment("Spawn Chance for Illagers Hunting the Player every Infamy Spawn Frequency, the lower the more likelier, Default: 5")
                .defineInRange("infamySpawnChance", 5, 0, Integer.MAX_VALUE);
        InfamySpell = BUILDER.comment("Casting Spells have a chance of giving Player Infamy, Default: true")
                .define("infamySpell", true);
        InfamySpellGive = BUILDER.comment("How much Infamy is given when casting Spells, Default: 1")
                .defineInRange("infamySpellGive", 1, 0, Integer.MAX_VALUE);
        DeathLoseInfamy = BUILDER.comment("How much Infamy removed when Player is killed, Default: 10")
                .defineInRange("infamyDeathLoss", 10, 0, Integer.MAX_VALUE);
        InfamyMax = BUILDER.comment("Maximum amount of Infamy the Player can attained, Default: 500")
                .defineInRange("infamyMax", 500, 1, Integer.MAX_VALUE);
        InfamyThreshold = BUILDER.comment("How much Infamy is required for Special Illagers to spawn, Default: 25")
                .defineInRange("infamyThreshold", 25, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Infamy Illager");
        PillagerInfamy = BUILDER.comment("How much Infamy the Player gains killing Pillagers, Default: 1")
                .defineInRange("pillagerInfamyGain", 1, 0, Integer.MAX_VALUE);
        VindicatorInfamy = BUILDER.comment("How much Infamy the Player gains killing Vindicators, Default: 2")
                .defineInRange("vindicatorInfamyGain", 2, 0, Integer.MAX_VALUE);
        EvokerInfamy = BUILDER.comment("How much Infamy the Player gains killing Evokers, Default: 5")
                .defineInRange("evokerInfamyGain", 5, 0, Integer.MAX_VALUE);
        IllusionerInfamy = BUILDER.comment("How much Infamy the Player gains killing Illusioners, Default: 5")
                .defineInRange("illusionerInfamyGain", 5, 0, Integer.MAX_VALUE);
        EnviokerInfamy = BUILDER.comment("How much Infamy the Player gains killing Enviokers, Default: 5")
                .defineInRange("enviokerInfamyGain", 5, 0, Integer.MAX_VALUE);
        InquillagerInfamy = BUILDER.comment("How much Infamy the Player gains killing Inquillagers, Default: 5")
                .defineInRange("inquillagerInfamyGain", 5, 0, Integer.MAX_VALUE);
        ConquillagerInfamy = BUILDER.comment("How much Infamy the Player gains killing Conquillagers, Default: 5")
                .defineInRange("conquillagerInfamyGain", 5, 0, Integer.MAX_VALUE);
        VizierInfamy = BUILDER.comment("How much Infamy the Player gains killing Viziers, Default: 25")
                .defineInRange("vizierInfamyGain", 25, 0, Integer.MAX_VALUE);
        ScryingInfamy = BUILDER.comment("How much Infamy the Player gains scrying on a Cat with an Empty Map offhand in a Village, Default: 10")
                .defineInRange("scryingInfamyGain", 10, 0, Integer.MAX_VALUE);
        PowerfulInfamy = BUILDER.comment("How much Infamy the Player gains killing a Powerful Modded Illagers, Default: 5")
                .defineInRange("powerfulInfamyGain", 5, 0, Integer.MAX_VALUE);
        OtherInfamy = BUILDER.comment("How much Infamy the Player gains killing other types of Illagers, Default: 1")
                .defineInRange("otherInfamyGain", 1, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Infamy Chance");
        VexInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Vex Spell, Default: 1")
                .defineInRange("vexInfamyChance", 1, 0, Integer.MAX_VALUE);
        FangInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Fang Spell, Default: 4")
                .defineInRange("fangInfamyChance", 4, 0, Integer.MAX_VALUE);
        RoarInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Roaring Spell, Default: 4")
                .defineInRange("roarInfamyChance", 4, 0, Integer.MAX_VALUE);
        ZombieInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Rotting Spell, Default: 16")
                .defineInRange("zombieInfamyChance", 16, 0, Integer.MAX_VALUE);
        SkeletonInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Osseous Spell, Default: 16")
                .defineInRange("skeletonInfamyChance", 16, 0, Integer.MAX_VALUE);
        DredenInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Rigid Spell, Default: 8")
                .defineInRange("dredenInfamyChance", 8, 0, Integer.MAX_VALUE);
        UndeadWolfInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Hounding Spell, Default: 32")
                .defineInRange("undeadWolfInfamyChance", 32, 0, Integer.MAX_VALUE);
        PhantomInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Phantasm Spell, Default: 32")
                .defineInRange("phantomInfamyChance", 32, 0, Integer.MAX_VALUE);
        WraithInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Spooky Spell, Default: 32")
                .defineInRange("wraithChance", 32, 0, Integer.MAX_VALUE);
        WitchGaleInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Witch's Gale Spell, Default: 16")
                .defineInRange("crippleInfamyChance", 16, 0, Integer.MAX_VALUE);
        SpiderlingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Spiderling Spell per second, Default: 128")
                .defineInRange("spiderlingInfamyChance", 128, 0, Integer.MAX_VALUE);
        BrainEaterInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Brain Eater Spell per second, Default: 0")
                .defineInRange("brainEaterInfamyChance", 0, 0, Integer.MAX_VALUE);
        SoulSkullInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Soul Skull Spell per second, Default: 0")
                .defineInRange("soulSkullInfamyChance", 0, 0, Integer.MAX_VALUE);
        TeleportInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Teleport Spell per second, Default: 0")
                .defineInRange("teleportInfamyChance", 0, 0, Integer.MAX_VALUE);
        FeastInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Feasting Spell per second, Default: 8")
                .defineInRange("feastInfamyChance", 8, 0, Integer.MAX_VALUE);
        TemptingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Tempting Spell per second, Default: 0")
                .defineInRange("temptingInfamyChance", 0, 0, Integer.MAX_VALUE);
        EnderAcidInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Ender Acid Breath Spell, Default: 0")
                .defineInRange("enderAcidBreathInfamyChance", 0, 0, Integer.MAX_VALUE);
        DragonFireballInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Dragon Fireball Spell, Default: 0")
                .defineInRange("dragonFireballInfamyChance", 0, 0, Integer.MAX_VALUE);
        CreeperlingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Creeperling Spell per second, Default: 128")
                .defineInRange("creeperlingInfamyChance", 128, 0, Integer.MAX_VALUE);
        BreathingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Breathing Spell per second, Default: 0")
                .defineInRange("breathingInfamyChance", 0, 0, Integer.MAX_VALUE);
        FireballInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Fireball Spell, Default: 0")
                .defineInRange("fireballInfamyChance", 0, 0, Integer.MAX_VALUE);
        LavaballInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Lava Bomb Spell, Default: 0")
                .defineInRange("lavaBombInfamyChance", 0, 0, Integer.MAX_VALUE);
        PoisonballInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Poison Ball Spell, Default: 0")
                .defineInRange("poisonBallInfamyChance", 0, 0, Integer.MAX_VALUE);
        IllusionInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Illusion Spell, Default: 1")
                .defineInRange("illusionInfamyChance", 1, 0, Integer.MAX_VALUE);
        FireBreathInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Fire Breath Spell, Default: 0")
                .defineInRange("fireBreathInfamyChance", 0, 0, Integer.MAX_VALUE);
        FrostBreathInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Frost Breath Spell, Default: 0")
                .defineInRange("frostBreathInfamyChance", 0, 0, Integer.MAX_VALUE);
        IceStormInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Ice Storm Spell, Default: 8")
                .defineInRange("iceStormInfamyChance", 8, 0, Integer.MAX_VALUE);
        LaunchInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Launch Spell, Default: 0")
                .defineInRange("launchInfamyChance", 0, 0, Integer.MAX_VALUE);
        SonicBoomInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Sonic Boom Spell, Default: 0")
                .defineInRange("sonicBoomInfamyChance", 0, 0, Integer.MAX_VALUE);
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
        BUILDER.pop();
        BUILDER.push("World Generation");
        TotemGen = BUILDER.comment("Totems Generates in the World, Default: true")
                .define("totemGen", true);
        GloomTreeGen = BUILDER.comment("Gloom Trees Generates in the World, Default: true")
                .define("gloomTreeGen", true);
        MurkTreeGen = BUILDER.comment("Murk Trees Generates in the World, Default: true")
                .define("murkTreeGen", true);
        BUILDER.pop();
        BUILDER.push("Minions");
        UndeadTeleport = BUILDER.comment("Whether Undead Servants can teleport to Players, Default: false")
                .define("undeadTeleport", false);
        VexTeleport = BUILDER.comment("Whether Vex Servants can teleport to Players, Default: true")
                .define("vexTeleport", true);
        MinionsAttackCreepers = BUILDER.comment("Whether Servants can attack Creepers if Mob Griefing Rule is False, Default: true")
                .define("minionAttackCreepers", true);
        MinionsMasterImmune = BUILDER.comment("Whether Servants or their owner are immune to attacks made by other servants that are summoned by the same owner, Default: true")
                .define("minionMasterImmune", true);
        OwnerAttackCancel = BUILDER.comment("Owners can't attack their servants, Default: true")
                .define("ownerAttackCancel", true);
        UndeadMinionHeal = BUILDER.comment("Whether Undead Minions can heal if summoned while wearing Necro Robes, Default: true")
                .define("undeadMinionHeal", true);
        TamedSpiderHeal = BUILDER.comment("Whether Loyal Spiders can heal if wearing Fel Helm, Default: true")
                .define("loyalSpiderHeal", true);
        RoyalSpiderMinions = BUILDER.comment("Whether Spiders will follow Royal Spiders around, Default: true")
                .define("royalSpiderMinions", true);
        UndeadMinionHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Undead Minion to heal, Default: 1")
                .defineInRange("undeadMinionHealCost", 1, 0, Integer.MAX_VALUE);
        TamedSpiderHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Loyal Spider to heal, Default: 1")
                .defineInRange("loyalSpiderHealCost", 1, 0, Integer.MAX_VALUE);
        WandVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a wand, without instantly dying, around the player, Default: 8")
                .defineInRange("wandVexLimit", 8, 1, Integer.MAX_VALUE);
        StaffVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a staff, without instantly dying, around the player, Default: 16")
                .defineInRange("staffVexLimit", 16, 1, Integer.MAX_VALUE);
        ZombieLimit = BUILDER.comment("Number of Zombie Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("zombieLimit", 32, 1, Integer.MAX_VALUE);
        SkeletonLimit = BUILDER.comment("Number of Skeleton Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("skeletonLimit", 32, 1, Integer.MAX_VALUE);
        UndeadWolfLimit = BUILDER.comment("Number of Zombie Wolf that can exist around the player without instantly dying, Default: 32")
                .defineInRange("zombieWolfLimit", 32, 1, Integer.MAX_VALUE);
        CreeperlingExplosionRadius = BUILDER.comment("The radius of a Creeperling's Explosion when not powered, Default: 1.25")
                .defineInRange("creeperlingExplosionRadius", 1.25, 0.0, Double.MAX_VALUE);
        SoulSkullZombie = BUILDER.comment("Zombies killed by Soul Skull converts into a Servant, Default: true")
                .define("soulSkullZombies", true);
        SoulSkullSkeleton = BUILDER.comment("Skeletons killed by Soul Skull converts into a Servant, Default: true")
                .define("soulSkullSkeletons", true);
        SoulSkullMinionWander = BUILDER.comment("Servants converted by Soul Skulls will automatically be in Wander Mode, Default: true")
                .define("soulSkullServantWander", true);
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
        WitchBowSouls = BUILDER.comment("Amount of Soul Energy Witch's Bow required to shoot a tipped Arrow, Default: 25")
                .defineInRange("witchBowSouls", 25, 1, Integer.MAX_VALUE);
        ItemsRepairAmount = BUILDER.comment("Amount of Souls needed to repair certain Equipments per second, Default: 5")
                .defineInRange("darkArmoredRobeRepairSouls", 5, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Enchantments");
        MaxSoulEaterLevel = BUILDER.comment("Soul Eater Maximum Enchantment Level, Default: 5")
                .defineInRange("maxSoulEaterLevel", 5, 1, 10);
        MaxWantingLevel = BUILDER.comment("Wanting Maximum Enchantment Level, Default: 3")
                .defineInRange("maxWantingLevel", 3, 1, 10);
        MaxPotencyLevel = BUILDER.comment("Potency Maximum Enchantment Level, Default: 5")
                .defineInRange("maxPotencyLevel", 5, 1, 10);
        MaxRadiusLevel = BUILDER.comment("Radius Maximum Enchantment Level, Default: 2")
                .defineInRange("maxRadiusLevel", 2, 1, 10);
        MaxRangeLevel = BUILDER.comment("Range Maximum Enchantment Level, Default: 10")
                .defineInRange("maxRangeLevel", 10, 1, 10);
        MaxDurationLevel = BUILDER.comment("Duration Maximum Enchantment Level, Default: 3")
                .defineInRange("maxDurationLevel", 3, 1, 10);
        BUILDER.pop();
        BUILDER.push("Villagers");
        VillagerHate = BUILDER.comment("Wearing a Dark Helm and Robe, along with variants, causes Villagers around the Player to have a negative Reputation unless said Player has 100 or more reputation among them, Default: false")
                .define("villagerHate", false);
        VillagerHateSpells = BUILDER.comment("Casting Spell in the presence of Villagers will cause the Player to lose a number of Reputation, set 0 to disable, Default: 0")
                .defineInRange("villagerHateSpells", 0, 0, Integer.MAX_VALUE);
        CultistSpread = BUILDER.comment("Whether Villagers are able to become secret Cultists, Default: true")
                .define("cultistSpread", true);
        BUILDER.pop();
        BUILDER.push("Spoilers");
        LichHealCost = BUILDER.comment("How much Soul Energy is cost to heal the Player per second if they've become a Lich, Default: 1")
                .defineInRange("lichHealCost", 1, 0, Integer.MAX_VALUE);
        LichNightVision = BUILDER.comment("Enable to get infinite Night Vision when being a Lich. If set true, wearing Fel Helm will no longer give Blindness during day, Default: true")
                .define("lichNightVision", true);
        LichUndeadFriends = BUILDER.comment("Undead Mobs will not attack you if you're a Lich and will even defend you if you're attack by another mob and wearing the Necro Set, Default: true")
                .define("lichUndeadFriendly", true);
        LichPowerfulFoes = BUILDER.comment("If Lich Undead Friendly is set to true, Only undead that have lower than 50 Hearts are friendly, Default: true")
                .define("lichPowerfulHostile", true);
        BUILDER.pop();
        BUILDER.push("Misc");
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
