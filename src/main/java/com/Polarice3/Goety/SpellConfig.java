package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class SpellConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> OwnerHitCommand;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpellDamageMultiplier;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> VexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WandVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> StaffVexLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> VexCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> VexSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VexTeleport;

    public static final ForgeConfigSpec.ConfigValue<Integer> FangCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> FangDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangGainSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> RoarCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> RoarDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieLimit;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SummonZPiglins;

    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> DredenCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DredenSummonDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadWolfSummonDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> PhantomSummonDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> WraithCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> WraithLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchGaleCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> WitchGaleDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterXPCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> SoulSkullDamage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullZombie;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullSkeleton;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulSkullMinionWander;

    public static final ForgeConfigSpec.ConfigValue<Integer> FeastCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TemptingDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> EnderAcidCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DragonFireballCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> CreeperlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Double> CreeperlingExplosionRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BreathingDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> FireballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireballCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> FireballDamage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FireballGriefing;

    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> LavaballCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> LavaballDamage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LavaballGriefing;

    public static final ForgeConfigSpec.ConfigValue<Integer> PoisonballCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> PoisonballDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> PoisonballCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> PoisonballDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllusionCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> FireBreathCost;
    public static final ForgeConfigSpec.ConfigValue<Double> FireBreathDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> FrostBreathCost;
    public static final ForgeConfigSpec.ConfigValue<Double> FrostBreathDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> SoulLightCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulLightDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulLightCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> GlowLightCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> GlowLightDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> GlowLightCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> IceChunkCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceChunkDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceChunkCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> IceChunkDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> IceStormCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> IceStormDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> LaunchCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> RecallCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RecallDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RecallCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SonicBoomCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> SonicBoomDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadMinionHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TamedSpiderHealCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RottreantAnimateCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RottreantBugLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSoulEaterLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxWantingLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxPotencyLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRadiusLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxRangeLevel;
    public static final ForgeConfigSpec.ConfigValue<Integer> MaxDurationLevel;

    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadTeleport;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsAttackCreepers;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MinionsMasterImmune;
    public static final ForgeConfigSpec.ConfigValue<Boolean> OwnerAttackCancel;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MobSense;
    public static final ForgeConfigSpec.ConfigValue<Boolean> UndeadMinionHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TamedSpiderHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RoyalSpiderMinions;

    static {
        BUILDER.push("General");
        SpellDamageMultiplier = BUILDER.comment("Multiplies the damage of spells by this amount, Default: 1")
                .defineInRange("spellDamageMultiplier", 1, 1, Integer.MAX_VALUE);
        OwnerHitCommand = BUILDER.comment("Whether Servants change navigation modes by hitting them, put false to make them change by right-clicking on them, Default: true")
                .define("ownerHitCommand", true);
        BUILDER.pop();
        BUILDER.push("Spells");
            BUILDER.push("Vexing Spell");
            VexCost = BUILDER.comment("Vexing Spell Cost, Default: 18")
                    .defineInRange("vexCost", 18, 0, Integer.MAX_VALUE);
            VexDuration = BUILDER.comment("Time to cast Vexing Spell, Default: 100")
                    .defineInRange("vexDuration", 100, 0, 72000);
            VexCoolDown = BUILDER.comment("Vexing Spell Cooldown, Default: 340")
                    .defineInRange("vexCoolDown", 340, 0, Integer.MAX_VALUE);
            VexSummonDown = BUILDER.comment("Vexing Spell Summon Down, Default: 340")
                    .defineInRange("vexSummonDown", 340, 0, 72000);
            WandVexLimit = BUILDER.comment("Number of Vex Servants that can be spawn with a wand, without instantly dying, around the player, Default: 8")
                    .defineInRange("wandVexLimit", 8, 1, Integer.MAX_VALUE);
            StaffVexLimit = BUILDER.comment("Number of Vex Servants that can be spawn with a staff, without instantly dying, around the player, Default: 16")
                    .defineInRange("staffVexLimit", 16, 1, Integer.MAX_VALUE);
            VexTeleport = BUILDER.comment("Whether Vex Servants can teleport to Players, Default: true")
                    .define("vexTeleport", true);
            BUILDER.pop();
            BUILDER.push("Biting Spell && Magic Fangs");
            FangCost = BUILDER.comment("Biting Spell Cost, Default: 8")
                    .defineInRange("fangCost", 8, 0, Integer.MAX_VALUE);
            FangDuration = BUILDER.comment("Time to cast Biting Spell, Default: 40")
                    .defineInRange("fangDuration", 40, 0, 72000);
            FangCoolDown = BUILDER.comment("Biting Spell Cooldown, Default: 100")
                    .defineInRange("fangCoolDown", 100, 0, Integer.MAX_VALUE);
            FangDamage = BUILDER.comment("How much base damage Magic Fangs deals, Default: 6.0")
                    .defineInRange("fangDamage", 6.0, 1.0, Double.MAX_VALUE);
            FangGainSouls = BUILDER.comment("Amount of Soul Energy Magic Fangs gives when hitting mob(s), Default: 1")
                    .defineInRange("fangGainSouls", 1, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Rotting Spell");
            ZombieCost = BUILDER.comment("Rotting Spell Cost, Default: 5")
                    .defineInRange("zombieCost", 5, 0, Integer.MAX_VALUE);
            ZombieDuration = BUILDER.comment("Time to cast Rotting Spell, Default: 20")
                    .defineInRange("zombieDuration", 20, 0, 72000);
            ZombieCoolDown = BUILDER.comment("Rotting Spell Cooldown, Default: 100")
                    .defineInRange("zombieCoolDown", 100, 0, Integer.MAX_VALUE);
            ZombieSummonDown = BUILDER.comment("Rotting Spell Summon Down, Default: 120")
                    .defineInRange("zombieSummonDown", 120, 0, 72000);
            ZombieLimit = BUILDER.comment("Number of Zombie Servants that can exist around the player without instantly dying, Default: 32")
                    .defineInRange("zombieLimit", 32, 1, Integer.MAX_VALUE);
            SummonZPiglins = BUILDER.comment("Allows Players to summon Zombified Piglins when casting Rotting Spell in the Nether, Default: true")
                    .define("summonZPiglins", true);
            BUILDER.pop();
            BUILDER.push("Osseous Spell");
            SkeletonCost = BUILDER.comment("Osseous Spell Cost, Default: 8")
                    .defineInRange("skeletonCost", 8, 0, Integer.MAX_VALUE);
            SkeletonDuration = BUILDER.comment("Time to cast Osseous Spell, Default: 60")
                    .defineInRange("skeletonDuration", 60, 0, 72000);
            SkeletonCoolDown = BUILDER.comment("Osseous Spell Cooldown, Default: 100")
                    .defineInRange("skeletonCoolDown", 100, 0, Integer.MAX_VALUE);
            SkeletonSummonDown = BUILDER.comment("Osseous Spell Summon Down, Default: 280")
                    .defineInRange("skeletonSummonDown", 280, 0, 72000);
            SkeletonLimit = BUILDER.comment("Number of Skeleton Servants that can exist around the player without instantly dying, Default: 32")
                    .defineInRange("skeletonLimit", 32, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Roaring Spell");
            RoarCost = BUILDER.comment("Roaring Spell Cost, Default: 10")
                    .defineInRange("roarCost", 10, 0, Integer.MAX_VALUE);
            RoarDuration = BUILDER.comment("Time to cast Roaring Spell, Default: 40")
                    .defineInRange("roarDuration", 40, 0, 72000);
            RoarCoolDown = BUILDER.comment("Roaring Spell Cooldown, Default: 120")
                    .defineInRange("roarCoolDown", 120, 0, Integer.MAX_VALUE);
            RoarDamage = BUILDER.comment("How much base damage Roaring Spell deals, Default: 3.0")
                    .defineInRange("roarDamage", 3.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Spooky Spell");
            WraithCost = BUILDER.comment("Spooky Spell Cost, Default: 24")
                    .defineInRange("wraithCost", 24, 0, Integer.MAX_VALUE);
            WraithDuration = BUILDER.comment("Time to cast Spooky Spell, Default: 60")
                    .defineInRange("wraithDuration", 60, 0, 72000);
            WraithCoolDown = BUILDER.comment("Spooky Spell Cooldown, Default: 100")
                    .defineInRange("wraithCoolDown", 100, 0, Integer.MAX_VALUE);
            WraithSummonDown = BUILDER.comment("Spooky Spell Summon Down, Default: 300")
                    .defineInRange("wraithSummonDown", 300, 0, 72000);
            WraithLimit = BUILDER.comment("Number of Wraith Servants that can exist around the player without instantly dying, Default: 6")
                    .defineInRange("wraithLimit", 6, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Rigid Spell");
            DredenCost = BUILDER.comment("Rigid Spell Cost, Default: 16")
                    .defineInRange("dredenCost", 16, 0, Integer.MAX_VALUE);
            DredenDuration = BUILDER.comment("Time to cast Rigid Spell, Default: 60")
                    .defineInRange("dredenDuration", 60, 0, 72000);
            DredenCoolDown = BUILDER.comment("Rigid Spell Cooldown, Default: 100")
                    .defineInRange("dredenCoolDown", 100, 0, Integer.MAX_VALUE);
            DredenSummonDown = BUILDER.comment("Rigid Spell SummonDown, Default: 300")
                    .defineInRange("dredenSummonDown", 300, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Hounding Spell");
            UndeadWolfCost = BUILDER.comment("Hounding Spell Cost, Default: 2")
                    .defineInRange("undeadWolfCost", 2, 0, Integer.MAX_VALUE);
            UndeadWolfDuration = BUILDER.comment("Time to cast Hounding Spell, Default: 20")
                    .defineInRange("undeadWolfDuration", 20, 0, 72000);
            UndeadWolfCoolDown = BUILDER.comment("Hounding Spell Cooldown, Default: 20")
                    .defineInRange("undeadWolfCoolDown", 20, 0, Integer.MAX_VALUE);
            UndeadWolfSummonDown = BUILDER.comment("Hounding Spell Summon Down, Default: 30")
                    .defineInRange("undeadWolfSummonDown", 30, 0, 72000);
            UndeadWolfLimit = BUILDER.comment("Number of Undead Wolf that can exist around the player without instantly dying, Default: 32")
                    .defineInRange("undeadWolfLimit", 32, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Phantasm Spell");
            PhantomCost = BUILDER.comment("Phantasm Spell Cost, Default: 16")
                    .defineInRange("phantomCost", 16, 0, Integer.MAX_VALUE);
            PhantomDuration = BUILDER.comment("Time to cast Phantasm Spell, Default: 60")
                    .defineInRange("phantomDuration", 60, 0, 72000);
            PhantomCoolDown = BUILDER.comment("Phantasm Spell Cooldown, Default: 100")
                    .defineInRange("phantomCoolDown", 100, 0, Integer.MAX_VALUE);
            PhantomSummonDown = BUILDER.comment("Phantasm Spell Summon Down, Default: 300")
                    .defineInRange("phantomSummonDown", 300, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Witch's Gale Spell");
            WitchGaleCost = BUILDER.comment("Witch's Gale Spell Cost, Default: 15")
                    .defineInRange("witchGaleCost", 15, 0, Integer.MAX_VALUE);
            WitchGaleDuration = BUILDER.comment("Time to cast Witch's Gale Spell, Default: 20")
                    .defineInRange("witchGaleDuration", 20, 0, 72000);
            WitchGaleCoolDown = BUILDER.comment("Witch's Gale Spell Cooldown, Default: 100")
                    .defineInRange("witchGaleCoolDown", 100, 0, Integer.MAX_VALUE);
            WitchGaleDamage = BUILDER.comment("How much base damage Witch's Gale deals when summoned from Staffs, Default: 2.0")
                    .defineInRange("witchGaleDamage", 2.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Spiderling Spell");
            SpiderlingCost = BUILDER.comment("Spiderling Spell Cost per second, Default: 2")
                    .defineInRange("spiderlingCost", 2, 0, Integer.MAX_VALUE);
            SpiderlingDuration = BUILDER.comment("Time to cast Spiderling Spell per second, Default: 10")
                    .defineInRange("spiderlingDuration", 10, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Brain Eater Spell");
            BrainEaterCost = BUILDER.comment("Brain Eater Spell Cost per second, Default: 5")
                    .defineInRange("brainCost", 5, 0, Integer.MAX_VALUE);
            BrainEaterDuration = BUILDER.comment("Time to cast Brain Eater Spell per second, Default: 1")
                    .defineInRange("brainEaterDuration", 1, 0, 72000);
            BrainEaterXPCost = BUILDER.comment("How much Experience the above spell Cost per heal, Default: 40")
                    .defineInRange("brainXpCost", 40, 0, 1000);
            BUILDER.pop();
            BUILDER.push("Teleport Spell");
            TeleportCost = BUILDER.comment("Teleport Spell Cost, Default: 5")
                    .defineInRange("teleportCost", 5, 0, Integer.MAX_VALUE);
            TeleportDuration = BUILDER.comment("Time to cast Teleport Spell, Default: 0")
                    .defineInRange("teleportDuration", 0, 0, 72000);
            TeleportCoolDown = BUILDER.comment("Teleport Spell Cooldown, Default: 20")
                    .defineInRange("teleportCoolDown", 20, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Soul Skull Spell");
            SoulSkullCost = BUILDER.comment("Soul Skull Spell Cost, Default: 16")
                    .defineInRange("soulSkullCost", 16, 0, Integer.MAX_VALUE);
            SoulSkullDuration = BUILDER.comment("Time to cast Soul Skull Spell, Default: 0")
                    .defineInRange("soulSkullDuration", 0, 0, 72000);
            SoulSkullCoolDown = BUILDER.comment("Soul Skull Spell Cooldown, Default: 20")
                    .defineInRange("soulSkullCoolDown", 20, 0, Integer.MAX_VALUE);
            SoulSkullDamage = BUILDER.comment("How much base damage Soul Skulls deal when directly hitting a mob, Default: 6.0")
                    .defineInRange("soulSkullDamage", 6.0, 1.0, Double.MAX_VALUE);
            SoulSkullZombie = BUILDER.comment("Zombies killed by Soul Skull converts into a Servant, Default: true")
                    .define("soulSkullZombies", true);
            SoulSkullSkeleton = BUILDER.comment("Skeletons killed by Soul Skull converts into a Servant, Default: true")
                    .define("soulSkullSkeletons", true);
            SoulSkullMinionWander = BUILDER.comment("Servants converted by Soul Skulls will automatically be in Wander Mode, Default: true")
                    .define("soulSkullServantWander", true);
            BUILDER.pop();
            BUILDER.push("Feasting Spell");
            FeastCost = BUILDER.comment("Feasting Spell Cost, Default: 8")
                    .defineInRange("feastCost", 8, 0, Integer.MAX_VALUE);
            FeastDuration = BUILDER.comment("Time to cast Feasting Spell per second, Default: 20")
                    .defineInRange("feastDuration", 20, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Tempting Spell");
            TemptingCost = BUILDER.comment("Tempting Spell Cost, Default: 2")
                    .defineInRange("temptingCost", 2, 0, Integer.MAX_VALUE);
            TemptingDuration = BUILDER.comment("Time to cast Tempting Spell per second, Default: 20")
                    .defineInRange("temptingDuration", 20, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Ender Acid Breath Spell");
            EnderAcidCost = BUILDER.comment("Ender Acid Breath Spell Cost, Default: 6")
                    .defineInRange("enderAcidBreathCost", 6, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Dragon Fireball Spell");
            DragonFireballCost = BUILDER.comment("Dragon Fireball Spell Cost, Default: 64")
                    .defineInRange("dragonFireballCost", 64, 0, Integer.MAX_VALUE);
            DragonFireballDuration = BUILDER.comment("Time to cast Dragon Fireball Spell, Default: 30")
                    .defineInRange("dragonFireballDuration", 30, 0, 72000);
            DragonFireballCoolDown = BUILDER.comment("Dragon Fireball Spell Cooldown, Default: 100")
                    .defineInRange("dragonFireballCoolDown", 100, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Creeperling Spell");
            CreeperlingCost = BUILDER.comment("Creeperling Spell Cost per second, Default: 8")
                    .defineInRange("creeperlingCost", 8, 0, Integer.MAX_VALUE);
            CreeperlingDuration = BUILDER.comment("Time to cast Creeperling Spell per second, Default: 10")
                    .defineInRange("creeperlingDuration", 10, 0, 72000);
            CreeperlingExplosionRadius = BUILDER.comment("The radius of a Creeperling's Explosion when not powered, Default: 1.25")
                    .defineInRange("creeperlingExplosionRadius", 1.25, 0.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Breathing Spell");
            BreathingCost = BUILDER.comment("Breathing Spell Cost per second, Default: 2")
                    .defineInRange("breathingCost", 2, 0, Integer.MAX_VALUE);
            BreathingDuration = BUILDER.comment("Time to cast Breathing Spell per second, Default: 1")
                    .defineInRange("breathingDuration", 1, 0, 72000);
            BUILDER.pop();
            BUILDER.push("Fireball Spell");
            FireballCost = BUILDER.comment("Fireball Spell Cost, Default: 4")
                    .defineInRange("fireballCost", 4, 0, Integer.MAX_VALUE);
            FireballDuration = BUILDER.comment("Time to cast Fireball Spell, Default: 0")
                    .defineInRange("fireballTime", 0, 0, 72000);
            FireballCoolDown = BUILDER.comment("Fireball Spell Cooldown, Default: 0")
                    .defineInRange("fireballCoolDown", 0, 0, Integer.MAX_VALUE);
            FireballDamage = BUILDER.comment("How much base damage Fireballs deal when directly hitting a mob, Default: 5.0")
                    .defineInRange("fireballDamage", 5.0, 1.0, Double.MAX_VALUE);
            FireballGriefing = BUILDER.comment("Enable Fireball projectile griefing, Default: true")
                    .define("fireballGriefing", true);
            BUILDER.pop();
            BUILDER.push("Lava Bomb Spell");
            LavaballCost = BUILDER.comment("Lava Bomb Spell Cost, Default: 16")
                    .defineInRange("lavaBombCost", 16, 0, Integer.MAX_VALUE);
            LavaballDuration = BUILDER.comment("Time to cast Lava Bomb Spell, Default: 40")
                    .defineInRange("lavaBombTime", 40, 0, 72000);
            LavaballCoolDown = BUILDER.comment("Lava Bomb Spell Cooldown, Default: 40")
                    .defineInRange("lavaBombCoolDown", 40, 0, Integer.MAX_VALUE);
            LavaballDamage = BUILDER.comment("How much base damage Lavaballs deal when directly hitting a mob, Default: 6.0")
                    .defineInRange("lavaballDamage", 6.0, 1.0, Double.MAX_VALUE);
            LavaballGriefing = BUILDER.comment("Enable Lavaball projectile griefing, Default: true")
                    .define("lavaballGriefing", true);
            BUILDER.pop();
            BUILDER.push("Poison Ball Spell");
            PoisonballCost = BUILDER.comment("Poison Ball Spell Cost, Default: 4")
                    .defineInRange("poisonBallCost", 4, 0, Integer.MAX_VALUE);
            PoisonballDuration = BUILDER.comment("Time to cast Poison Ball Spell, Default: 0")
                    .defineInRange("poisonBallDuration", 0, 0, 72000);
            PoisonballCoolDown = BUILDER.comment("Poison Ball Spell Cooldown, Default: 0")
                    .defineInRange("poisonBallCoolDown", 0, 0, Integer.MAX_VALUE);
            PoisonballDamage = BUILDER.comment("How much base damage Poison Balls deals when summoned from Staffs, Default: 4.0")
                    .defineInRange("poisonballDamage", 4.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Illusion Spell");
            IllusionCost = BUILDER.comment("Illusion Spell Cost, Default: 20")
                    .defineInRange("illusionCost", 20, 0, Integer.MAX_VALUE);
            IllusionDuration = BUILDER.comment("Time to cast Illusion Spell, Default: 40")
                    .defineInRange("illusionDuration", 40, 0, 72000);
            IllusionCoolDown = BUILDER.comment("Illusion Spell Cooldown, Default: 340")
                    .defineInRange("illusionCoolDown", 340, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Fire Breath Spell");
            FireBreathCost = BUILDER.comment("Fire Breath Spell Cost per second, Default: 2")
                    .defineInRange("fireBreathCost", 2, 0, Integer.MAX_VALUE);
            FireBreathDamage = BUILDER.comment("How much base damage Fire Breath deals, Default: 2.0")
                    .defineInRange("fireBreathDamage", 2.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Frost Breath Spell");
            FrostBreathCost = BUILDER.comment("Frost Breath Spell Cost per second, Default: 2")
                    .defineInRange("frostBreathCost", 2, 0, Integer.MAX_VALUE);
            FrostBreathDamage = BUILDER.comment("How much base damage Frost Breath deals, Default: 1.0")
                    .defineInRange("frostBreathDamage", 1.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Soul Light Spell");
            SoulLightCost = BUILDER.comment("Soul Light Spell Cost, Default: 1")
                    .defineInRange("soulLightCost", 1, 0, Integer.MAX_VALUE);
            SoulLightDuration = BUILDER.comment("Time to cast Soul Light Spell, Default: 0")
                    .defineInRange("soulLightTime", 0, 0, 72000);
            SoulLightCoolDown = BUILDER.comment("Soul Light Spell Cooldown, Default: 10")
                    .defineInRange("soulLightCoolDown", 10, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Glow Light Spell");
            GlowLightCost = BUILDER.comment("Glow Light Spell Cost, Default: 2")
                    .defineInRange("glowLightCost", 2, 0, Integer.MAX_VALUE);
            GlowLightDuration = BUILDER.comment("Time to cast Glow Light Spell, Default: 0")
                    .defineInRange("glowLightTime", 0, 0, 72000);
            GlowLightCoolDown = BUILDER.comment("Glow Light Spell Cooldown, Default: 10")
                    .defineInRange("glowLightCoolDown", 10, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Iceology Spell");
            IceChunkCost = BUILDER.comment("Iceology Spell Cost, Default: 16")
                    .defineInRange("iceChunkCost", 16, 0, Integer.MAX_VALUE);
            IceChunkDuration = BUILDER.comment("Time to cast Iceology Spell, Default: 0")
                    .defineInRange("iceChunkTime", 0, 0, 72000);
            IceChunkCoolDown = BUILDER.comment("Iceology Spell Cooldown, Default: 300")
                    .defineInRange("iceChunkCoolDown", 300, 0, Integer.MAX_VALUE);
            IceChunkDamage = BUILDER.comment("How much base damage Iceology deals, Default: 8.0")
                    .defineInRange("iceChunkDamage", 8.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Ice Storm Spell");
            IceStormCost = BUILDER.comment("Ice Storm Spell Cost, Default: 16")
                    .defineInRange("iceStormCost", 16, 0, Integer.MAX_VALUE);
            IceStormDuration = BUILDER.comment("Time to cast Ice Storm Spell, Default: 60")
                    .defineInRange("iceStormDuration", 60, 0, 72000);
            IceStormCoolDown = BUILDER.comment("Ice Storm Spell Cooldown, Default: 100")
                    .defineInRange("iceStormCoolDown", 100, 0, Integer.MAX_VALUE);
            IceStormDamage = BUILDER.comment("How much base damage Ice Storm deals, Default: 1.0")
                    .defineInRange("iceStormDamage", 1.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Launching Spell");
            LaunchCost = BUILDER.comment("Launch Spell Cost, Default: 4")
                    .defineInRange("launchCost", 4, 0, Integer.MAX_VALUE);
            LaunchDuration = BUILDER.comment("Time to cast Launching Spell, Default: 0")
                    .defineInRange("launchTime", 0, 0, 72000);
            LaunchCoolDown = BUILDER.comment("Launch Spell Cooldown, Default: 0")
                    .defineInRange("launchCoolDown", 0, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Recall Spell");
            RecallCost = BUILDER.comment("Recall Spell Cost, Default: 1000")
                    .defineInRange("recallCost", 1000, 0, Integer.MAX_VALUE);
            RecallDuration = BUILDER.comment("Time to cast Recall Spell, Default: 160")
                    .defineInRange("recallTime", 160, 0, 72000);
            RecallCoolDown = BUILDER.comment("Recall Spell Cooldown, Default: 100")
                    .defineInRange("recallCoolDown", 100, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Sonic Boom Spell");
            SonicBoomCost = BUILDER.comment("Sonic Boom Spell Cost, Default: 16")
                    .defineInRange("sonicBoomCost", 16, 0, Integer.MAX_VALUE);
            SonicBoomDuration = BUILDER.comment("Time to cast Sonic Boom Spell, Default: 60")
                    .defineInRange("sonicBoomTime", 60, 0, 72000);
            SonicBoomCoolDown = BUILDER.comment("Sonic Boom Spell Cooldown, Default: 40")
                    .defineInRange("sonicBoomCoolDown", 40, 0, Integer.MAX_VALUE);
            SonicBoomDamage = BUILDER.comment("How much base damage Sonic Boom Spell deals, Default: 10.0")
                    .defineInRange("sonicBoomDamage", 10.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
        BUILDER.pop();
        BUILDER.push("Minions");
        UndeadTeleport = BUILDER.comment("Whether Undead Servants can teleport to Players, Default: false")
                .define("undeadTeleport", false);
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
        UndeadMinionHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Undead Minion to heal, Default: 1")
                .defineInRange("undeadMinionHealCost", 1, 0, Integer.MAX_VALUE);
        TamedSpiderHealCost = BUILDER.comment("How much Soul Energy it cost per second for an Loyal Spider to heal, Default: 1")
                .defineInRange("loyalSpiderHealCost", 1, 0, Integer.MAX_VALUE);
        RottreantAnimateCost = BUILDER.comment("How much Soul Energy it cost to animate a Rottreant, Default: 200")
                .defineInRange("rottreantAnimateCost", 200, 0, Integer.MAX_VALUE);
        RottreantBugLimit = BUILDER.comment("Number of Fel Flies and Spiderlings a Rottreant can summon, Default: 8")
                .defineInRange("rottreantBugLimit", 8, 1, Integer.MAX_VALUE);
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
