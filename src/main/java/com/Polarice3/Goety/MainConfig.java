package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class MainConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingInfamyChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingInfamyChance;

    public static final ForgeConfigSpec.ConfigValue<Integer> PillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> VindicatorInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> EvokerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnviokerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> InquillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> ConquillagerInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> VizierInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> ScryingInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> OtherInfamy;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathLoseInfamy;

    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterXPCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCooldown;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> AnthropodSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VillagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PiglinSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderDragonSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PlayerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> DefaultSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxEnchant;

    public static final ForgeConfigSpec.ConfigValue<Integer> CraftingSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpawnFreq;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpawnChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> MRabbitMax;
    public static final ForgeConfigSpec.ConfigValue<Integer> DarkArmoredRobeRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> NecroArmoredRobeRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> WanderBootsRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> WandVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> StaffVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadMinionHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TamedSpiderHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamySpellGive;
    public static final ForgeConfigSpec.ConfigValue<Integer> InfamyThreshold;

    public static final ForgeConfigSpec.ConfigValue<Integer> FanaticPitchforkChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> FanaticWitchBombChance;

    public static final ForgeConfigSpec.ConfigValue<Double> CreeperlingExplosionRadius;

    public static final ForgeConfigSpec.ConfigValue<Boolean> InfamySpawn;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DeadSandSpread;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulRepair;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterTotem;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullFire;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VexTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ShowNum;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsAttackCreepers;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsMasterImmune;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadMinionHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TamedSpiderHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagueSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> InfamySpell;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DarkManorGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PortalOutpostGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CursedGraveyardGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SalvagedFortGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DecrepitFortGen;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VizierMinion;

    static {
        BUILDER.push("General");
        MaxSouls = BUILDER.comment("Totem Maximum Soul Count, Default: 10000")
                .defineInRange("maxSouls", 10000, 100, Integer.MAX_VALUE);
        SoulRepair = BUILDER.comment("Dark and Necro Robes repair themselves using Soul Energy, Default: true")
                .define("soulRepair", true);
        TotemUndying = BUILDER.comment("Totem of Souls will save the Player if full of Soul Energy, Default: true")
                .define("totemUndying", true);
        MaxEnchant = BUILDER.comment("Soul Eater Maximum Enchantment Level, Default: 3")
                .defineInRange("maxEnchant", 3, 1, 10);
        StarterTotem = BUILDER.comment("Gives Players a Totem of Souls when first entering World, Default: false")
                .define("starterTotem", false);
        CraftingSouls = BUILDER.comment("How much Souls is consumed when crafting with Totem, Default: 20")
                .defineInRange("craftSouls", 20, 0, Integer.MAX_VALUE);
        ShowNum = BUILDER.comment("Show numerical amount of Souls on the Soul Energy Bar, Default: false")
                .define("showNumber", false);
        BUILDER.pop();
        BUILDER.push("Blocks");
        DeadSandSpread = BUILDER.comment("Dead Sand can Spread to other Blocks, Default: true")
                .define("deadSandSpread", true);
        BUILDER.pop();
        BUILDER.push("Soul Taken");
        UndeadSouls = BUILDER.comment("Undead Killed, Default: 5")
                .defineInRange("undeadSouls", 5, 0, Integer.MAX_VALUE);
        AnthropodSouls = BUILDER.comment("Anthropods Killed, Default: 5")
                .defineInRange("anthropodSouls", 5, 0, Integer.MAX_VALUE);
        IllagerSouls = BUILDER.comment("Illagers, Witches, Cultists, Protectors Killed, Default: 25")
                .defineInRange("illagerSouls", 25, 0, Integer.MAX_VALUE);
        VillagerSouls = BUILDER.comment("Villagers Killed, Default: 50")
                .defineInRange("villagerSouls", 50, 0, Integer.MAX_VALUE);
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
        ZombieCost = BUILDER.comment("Necroturgy Spell Cost, Default: 5")
                .defineInRange("zombieCost", 5, 0, Integer.MAX_VALUE);
        SkeletonCost = BUILDER.comment("Osseous Spell Cost, Default: 8")
                .defineInRange("skeletonCost", 8, 0, Integer.MAX_VALUE);
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
        DragonFireballCost = BUILDER.comment("Dragon Fireball Spell Cost, Default: 64")
                .defineInRange("dragonFireballCost", 64, 0, Integer.MAX_VALUE);
        CreeperlingCost = BUILDER.comment("Creeperling Spell Cost per second, Default: 8")
                .defineInRange("creeperlingCost", 8, 0, Integer.MAX_VALUE);
        BreathingCost = BUILDER.comment("Breathing Spell Cost per second, Default: 2")
                .defineInRange("breathingCost", 2, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Casting Time");
        VexDuration = BUILDER.comment("Time to cast Vex Spell, Default: 100")
                .defineInRange("vexTime", 100, 0, 72000);
        FangDuration = BUILDER.comment("Time to cast Fang Spell, Default: 80")
                .defineInRange("fangTime", 80, 0, 72000);
        RoarDuration = BUILDER.comment("Time to cast Roaring Spell, Default: 40")
                .defineInRange("roarTime", 40, 0, 72000);
        ZombieDuration = BUILDER.comment("Time to cast Necroturgy Spell, Default: 60")
                .defineInRange("zombieTime", 60, 0, 72000);
        SkeletonDuration = BUILDER.comment("Time to cast Osseous Spell, Default: 60")
                .defineInRange("skeletonTime", 60, 0, 72000);
        WitchGaleDuration = BUILDER.comment("Time to cast Witch's Gale Spell, Default: 20")
                .defineInRange("crippleTime", 20, 0, 72000);
        SpiderlingDuration = BUILDER.comment("Time to cast Spiderling Spell per second, Default: 10")
                .defineInRange("spiderlingTime", 10, 0, 72000);
        BrainEaterDuration = BUILDER.comment("Time to cast Brain Eater Spell per second, Default: 5")
                .defineInRange("brainEaterTime", 5, 0, 72000);
        FeastDuration = BUILDER.comment("Time to cast Feasting Spell per second, Default: 20")
                .defineInRange("feastTime", 20, 0, 72000);
        TemptingDuration = BUILDER.comment("Time to cast Tempting Spell per second, Default: 20")
                .defineInRange("temptingTime", 20, 0, 72000);
        DragonFireballDuration = BUILDER.comment("Time to cast Dragon Fireball Spell, Default: 30")
                .defineInRange("dragonFireballTime", 30, 0, 72000);
        CreeperlingDuration = BUILDER.comment("Time to cast Creeperling Spell per second, Default: 10")
                .defineInRange("creeperlingTime", 10, 0, 72000);
        BreathingDuration = BUILDER.comment("Time to cast Breathing Spell per second, Default: 10")
                .defineInRange("breathingTime", 10, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Summon Down Duration");
        VexCooldown = BUILDER.comment("Vex Spell Cooldown, Default: 340")
                .defineInRange("vexCooldown", 340, 0, 72000);
        ZombieCooldown = BUILDER.comment("Necroturgy Spell Cooldown, Default: 250")
                .defineInRange("zombieCooldown", 250, 0, 72000);
        SkeletonCooldown = BUILDER.comment("Osseous Spell Cooldown, Default: 280")
                .defineInRange("skeletonCooldown", 280, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Mobs");
        MRabbitMax = BUILDER.comment("Maximum amount of Mutant Rabbits can spawn, Default: 16")
                .defineInRange("mutatedRabbitMax", 16, 0, 100);
        VizierMinion = BUILDER.comment("Viziers spawn Vexes instead of Irks, Default: false")
                .define("vizierMinion", false);
        FanaticPitchforkChance = BUILDER.comment("Chance for the Fanatic mob to spawn with a Pitchfork, Default: 4")
                .defineInRange("fanaticPitchforkChance", 4, 0, Integer.MAX_VALUE);
        FanaticWitchBombChance = BUILDER.comment("Chance for the Fanatic mob to spawn with a Witch's Bomb, Default: 4")
                .defineInRange("fanaticWitchBombChance", 4, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Infamy");
        InfamySpawn = BUILDER.comment("Special Illagers Spawning due to Infamy, Default: true")
                .define("infamySpawn", true);
        InfamySpawnFreq = BUILDER.comment("Spawn Frequency for Illagers Hunting the Player, Default: 12000")
                .defineInRange("infamySpawnFreq", 12000, 0, 72000);
        InfamySpawnChance = BUILDER.comment("Spawn Chance for Illagers Hunting the Player every Infamy Spawn Frequency, the lower the more likelier, Default: 5")
                .defineInRange("infamySpawnChance", 5, 0, Integer.MAX_VALUE);
        InfamySpell = BUILDER.comment("Casting Spells have a chance of giving Player Infamy, Default: true")
                .define("infamySpell", true);
        InfamySpellGive = BUILDER.comment("How much Infamy is given when casting Spells, Default: 1")
                .defineInRange("infamySpellGive", 1, 0, Integer.MAX_VALUE);
        DeathLoseInfamy = BUILDER.comment("How much Infamy removed when Player is killed, Default: 1")
                .defineInRange("infamyDeathLoss", 1, 0, Integer.MAX_VALUE);
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
        ZombieInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Necroturgy Spell, Default: 16")
                .defineInRange("zombieInfamyChance", 16, 0, Integer.MAX_VALUE);
        SkeletonInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Osseous Spell, Default: 16")
                .defineInRange("skeletonInfamyChance", 16, 0, Integer.MAX_VALUE);
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
        DragonFireballInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Dragon Fireball Spell, Default: 0")
                .defineInRange("dragonFireballInfamyChance", 0, 0, Integer.MAX_VALUE);
        CreeperlingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Creeperling Spell per second, Default: 128")
                .defineInRange("creeperlingInfamyChance", 128, 0, Integer.MAX_VALUE);
        BreathingInfamyChance = BUILDER.comment("Chance of Gaining Infamy when casting the Breathing Spell per second, Default: 0")
                .defineInRange("breathingInfamyChance", 0, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Structure Generation");
        DarkManorGen = BUILDER.comment("Dark Manor Generates in the World, Default: true")
                .define("darkManorGen", true);
        PortalOutpostGen = BUILDER.comment("Portal Outpost Generates in the World, Default: true")
                .define("portalOutpostGen", true);
        CursedGraveyardGen = BUILDER.comment("Cursed Graveyard Generates in the World, Default: true")
                .define("cursedGraveyardGen", true);
        SalvagedFortGen = BUILDER.comment("Salvaged Fort Generates in the World, Default: true")
                .define("salvagedFortGen", true);
        DecrepitFortGen = BUILDER.comment("Decrepit Fort Generates in the World, Default: true")
                .define("decrepitFortGen", true);
        TotemGen = BUILDER.comment("Totems Generates in the World, Default: true")
                .define("totemGen", true);
        BUILDER.pop();
        BUILDER.push("Spells");
        UndeadTeleport = BUILDER.comment("Whether Undead Minions can teleport to Players, Default: false")
                .define("undeadTeleport", false);
        VexTeleport = BUILDER.comment("Whether Vex Minions can teleport to Players, Default: true")
                .define("vexTeleport", true);
        MinionsAttackCreepers = BUILDER.comment("Whether Minions can attack Creepers if Mob Griefing Rule is False, Default: true")
                .define("minionAttackCreepers", true);
        MinionsMasterImmune = BUILDER.comment("Whether Minions are immune to attacks made by other minions that are summoned by the same caster, Default: true")
                .define("minionMasterImmune", true);
        UndeadMinionHeal = BUILDER.comment("Whether Undead Minions can heal if summoned while wearing Necro Robes, Default: true")
                .define("undeadMinionHeal", true);
        TamedSpiderHeal = BUILDER.comment("Whether Loyal Spiders can heal if wearing Arachnid Robes, Default: true")
                .define("loyalSpiderHeal", true);
        UndeadMinionHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Undead Minion to heal, Default: 1")
                .defineInRange("undeadMinionHealCost", 1, 1, Integer.MAX_VALUE);
        TamedSpiderHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Loyal Spider to heal, Default: 1")
                .defineInRange("loyalSpiderHealCost", 1, 1, Integer.MAX_VALUE);
        WandVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a wand, without instantly dying, around the player, Default: 8")
                .defineInRange("wandVexLimit", 8, 1, Integer.MAX_VALUE);
        StaffVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a staff, without instantly dying, around the player, Default: 16")
                .defineInRange("staffVexLimit", 16, 1, Integer.MAX_VALUE);
        ZombieLimit = BUILDER.comment("Number of Zombie Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("zombieLimit", 32, 1, Integer.MAX_VALUE);
        SkeletonLimit = BUILDER.comment("Number of Skeleton Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("skeletonLimit", 32, 1, Integer.MAX_VALUE);
        CreeperlingExplosionRadius = BUILDER.comment("The radius of a Creeperling's Explosion when not powered, Default: 1.25")
                .defineInRange("creeperlingExplosionRadius", 1.25, 0.0, Double.MAX_VALUE);
        SoulSkullFire = BUILDER.comment("Soul Skulls when shot from Staffs sets ground aflame, Default: false")
                .define("soulSkullFire", false);
        BUILDER.pop();
        BUILDER.push("Robe Repairs");
        DarkArmoredRobeRepairAmount = BUILDER.comment("Amount of Souls needed to repair Dark Armored Robes per second, Default: 20")
                .defineInRange("maxSouls", 20, 1, Integer.MAX_VALUE);
        NecroArmoredRobeRepairAmount = BUILDER.comment("Amount of Souls needed to repair Necro Armored Robes per second, Default: 20")
                .defineInRange("maxSouls", 20, 1, Integer.MAX_VALUE);
        WanderBootsRepairAmount = BUILDER.comment("Amount of Souls needed to repair Wander Boots per second, Default: 20")
                .defineInRange("maxSouls", 20, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Misc");
        IllagueSpread = BUILDER.comment("Whether Illague Effect can spread from non Conquillagers that has the effect, Default: true")
                .define("illagueSpread", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

}
