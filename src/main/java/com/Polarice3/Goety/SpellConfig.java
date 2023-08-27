package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class SpellConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> SpellDamageMultiplier;

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
    public static final ForgeConfigSpec.ConfigValue<Integer> IceChunkCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RecallCost;
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
    public static final ForgeConfigSpec.ConfigValue<Integer> IceChunkDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RecallDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomDuration;

    public static final ForgeConfigSpec.ConfigValue<Double> FangDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> RoarDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> WitchGaleDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SoulSkullDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> FireballDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> LavaballDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> PoisonballDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> FireBreathDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> FrostBreathDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> IceChunkDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> IceStormDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SonicBoomDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> CreeperlingExplosionRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> WandVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> StaffVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadMinionHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TamedSpiderHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RottreantAnimateCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RottreantBugLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterXPCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithCooldown;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSoulEaterLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxWantingLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxPotencyLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRadiusLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRangeLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxDurationLevel;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullZombie;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullSkeleton;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullMinionWander;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VexTeleport;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsAttackCreepers;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsMasterImmune;
    public static final ForgeConfigSpec.ConfigValue<Boolean> OwnerAttackCancel;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MobSense;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadMinionHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TamedSpiderHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RoyalSpiderMinions;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SummonZPiglins;

    static {
        BUILDER.push("General");
        SpellDamageMultiplier = BUILDER.comment("Multiplies the damage of spells by this amount, Default: 1")
                .defineInRange("spellDamageMultiplier", 1, 1, Integer.MAX_VALUE);
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
        IceChunkCost = BUILDER.comment("Ice Chunk Spell Cost, Default: 16")
                .defineInRange("iceChunkCost", 16, 0, Integer.MAX_VALUE);
        IceStormCost = BUILDER.comment("Ice Storm Spell Cost, Default: 16")
                .defineInRange("iceStormCost", 16, 0, Integer.MAX_VALUE);
        LaunchCost = BUILDER.comment("Launch Spell Cost, Default: 4")
                .defineInRange("launchCost", 4, 0, Integer.MAX_VALUE);
        RecallCost = BUILDER.comment("Recall Spell Cost, Default: 1000")
                .defineInRange("recallCost", 1000, 0, Integer.MAX_VALUE);
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
        IceChunkDuration = BUILDER.comment("Time to cast Ice Chunk Spell, Default: 40")
                .defineInRange("iceChunkTime", 40, 0, 72000);
        IceStormDuration = BUILDER.comment("Time to cast Ice Storm Spell, Default: 60")
                .defineInRange("iceStormTime", 60, 0, 72000);
        RecallDuration = BUILDER.comment("Time to cast Recall Spell, Default: 160")
                .defineInRange("recallTime", 160, 0, 72000);
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
        BUILDER.push("Spell Power");
        FangDamage = BUILDER.comment("How much base damage Magic Fangs deals, Default: 6.0")
                .defineInRange("fangDamage", 6.0, 1.0, Double.MAX_VALUE);
        RoarDamage = BUILDER.comment("How much base damage Roar Spell deals, Default: 3.0")
                .defineInRange("roarDamage", 3.0, 1.0, Double.MAX_VALUE);
        WitchGaleDamage = BUILDER.comment("How much base damage Witch's Gale deals when summoned from Staffs, Default: 2.0")
                .defineInRange("witchGaleDamage", 2.0, 1.0, Double.MAX_VALUE);
        SoulSkullDamage = BUILDER.comment("How much base damage Soul Skulls deal when directly hitting a mob, Default: 6.0")
                .defineInRange("soulSkullDamage", 6.0, 1.0, Double.MAX_VALUE);
        FireballDamage = BUILDER.comment("How much base damage Fireballs deal when directly hitting a mob, Default: 5.0")
                .defineInRange("fireballDamage", 5.0, 1.0, Double.MAX_VALUE);
        LavaballDamage = BUILDER.comment("How much base damage Lavaballs deal when directly hitting a mob, Default: 6.0")
                .defineInRange("lavaballDamage", 6.0, 1.0, Double.MAX_VALUE);
        PoisonballDamage = BUILDER.comment("How much base damage Poison Balls deals when summoned from Staffs, Default: 4.0")
                .defineInRange("poisonballDamage", 4.0, 1.0, Double.MAX_VALUE);
        FireBreathDamage = BUILDER.comment("How much base damage Fire Breath deals, Default: 2.0")
                .defineInRange("fireBreathDamage", 2.0, 1.0, Double.MAX_VALUE);
        FrostBreathDamage = BUILDER.comment("How much base damage Frost Breath deals, Default: 1.0")
                .defineInRange("frostBreathDamage", 1.0, 1.0, Double.MAX_VALUE);
        IceChunkDamage = BUILDER.comment("How much base damage Ice Chunks deals, Default: 8.0")
                .defineInRange("iceChunkDamage", 8.0, 1.0, Double.MAX_VALUE);
        IceStormDamage = BUILDER.comment("How much base damage Ice Storm deals, Default: 1.0")
                .defineInRange("iceStormDamage", 1.0, 1.0, Double.MAX_VALUE);
        SonicBoomDamage = BUILDER.comment("How much base damage Sonic Boom Spell deals, Default: 10.0")
                .defineInRange("sonicBoomDamage", 10.0, 1.0, Double.MAX_VALUE);
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
        MobSense = BUILDER.comment("Mobs will automatically be hostile to servants, if servant is hostile towards the mob, Default: true")
                .define("mobSense", true);
        UndeadMinionHeal = BUILDER.comment("Whether Undead Minions can heal if summoned while wearing Necro Robes, Default: true")
                .define("undeadMinionHeal", true);
        TamedSpiderHeal = BUILDER.comment("Whether Loyal Spiders can heal if wearing Fel Helm, Default: true")
                .define("loyalSpiderHeal", true);
        RoyalSpiderMinions = BUILDER.comment("Whether Spiders will follow Royal Spiders around, Default: true")
                .define("royalSpiderMinions", true);
        SummonZPiglins = BUILDER.comment("Allows Players to summon Zombified Piglins when casting Rotting Spell in the Nether, Default: true")
                .define("summonZPiglins", true);
        UndeadMinionHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Undead Minion to heal, Default: 1")
                .defineInRange("undeadMinionHealCost", 1, 0, Integer.MAX_VALUE);
        TamedSpiderHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Loyal Spider to heal, Default: 1")
                .defineInRange("loyalSpiderHealCost", 1, 0, Integer.MAX_VALUE);
        RottreantAnimateCost = BUILDER.comment("How much Soul Energy it cost to animate a Rottreant, Default: 200")
                .defineInRange("rottreantAnimateCost", 200, 0, Integer.MAX_VALUE);
        WandVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a wand, without instantly dying, around the player, Default: 8")
                .defineInRange("wandVexLimit", 8, 1, Integer.MAX_VALUE);
        StaffVexLimit = BUILDER.comment("Number of Vex Minions that can be spawn with a staff, without instantly dying, around the player, Default: 16")
                .defineInRange("staffVexLimit", 16, 1, Integer.MAX_VALUE);
        ZombieLimit = BUILDER.comment("Number of Zombie Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("zombieLimit", 32, 1, Integer.MAX_VALUE);
        SkeletonLimit = BUILDER.comment("Number of Skeleton Servants that can exist around the player without instantly dying, Default: 32")
                .defineInRange("skeletonLimit", 32, 1, Integer.MAX_VALUE);
        UndeadWolfLimit = BUILDER.comment("Number of Undead Wolf that can exist around the player without instantly dying, Default: 32")
                .defineInRange("undeadWolfLimit", 32, 1, Integer.MAX_VALUE);
        RottreantBugLimit = BUILDER.comment("Number of Fel Flies and Spiderlings a Rottreant can summon, Default: 8")
                .defineInRange("rottreantBugLimit", 8, 1, Integer.MAX_VALUE);
        CreeperlingExplosionRadius = BUILDER.comment("The radius of a Creeperling's Explosion when not powered, Default: 1.25")
                .defineInRange("creeperlingExplosionRadius", 1.25, 0.0, Double.MAX_VALUE);
        SoulSkullZombie = BUILDER.comment("Zombies killed by Soul Skull converts into a Servant, Default: true")
                .define("soulSkullZombies", true);
        SoulSkullSkeleton = BUILDER.comment("Skeletons killed by Soul Skull converts into a Servant, Default: true")
                .define("soulSkullSkeletons", true);
        SoulSkullMinionWander = BUILDER.comment("Servants converted by Soul Skulls will automatically be in Wander Mode, Default: true")
                .define("soulSkullServantWander", true);
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
