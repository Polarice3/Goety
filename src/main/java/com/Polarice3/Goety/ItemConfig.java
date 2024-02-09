package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class ItemConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> DarkArmoredRobeRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> EmeraldAmuletSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> NecroSoulSandSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> ItemsRepairAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> DarkScytheSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PendantOfHungerLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> WitchBowSouls;

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
    public static final ForgeConfigSpec.ConfigValue<Integer> PhilosophersMaceEnchantability;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulRepair;

    public static final ForgeConfigSpec.ConfigValue<Boolean> NecroSetUndeadNeutral;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FelHelmArthropodNeutral;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FelRobesCreeperNeutral;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FelBootsSlimeNeutral;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ScytheSlashBreaks;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FireSpawnCage;

    static {
        BUILDER.push("General");
        SoulRepair = BUILDER.comment("Certain Items repair themselves using Soul Energy, Default: true")
                .define("soulRepair", true);
        FireSpawnCage = BUILDER.comment("Fire Spawn Cage are enabled, Default: true")
                .define("fireSpawnCage", true);
        BUILDER.pop();
        BUILDER.push("Equipment");
        DarkStaffDamage = BUILDER.comment("How much base damage Dark Staffs deals, Default: 4.0")
                .defineInRange("darkStaffDamage", 4.0, 1.0, Double.MAX_VALUE);
            BUILDER.push("Robes");
            DarkArmoredRobeRepairAmount = BUILDER.comment("Amount of Souls needed to repair Dark Armored Robes per second, Default: 10")
                    .defineInRange("darkArmoredRobeRepairSouls", 10, 1, Integer.MAX_VALUE);
            NecroSoulSandSouls = BUILDER.comment("Amount of Soul Energy Necro Boots of Wander gives when running on Soul Sand Blocks, Default: 1")
                    .defineInRange("necroSoulSandSouls", 1, 1, Integer.MAX_VALUE);
            NecroSetUndeadNeutral = BUILDER.comment("Whether wearing Necro Set will cause Undead mobs to be neutral, Default: true")
                    .define("necroSetUndeadNeutral", true);
            FelHelmArthropodNeutral = BUILDER.comment("Whether wearing Fel Helm will cause Arthropod mobs to be neutral, Default: true")
                    .define("felHelmArthropodNeutral", true);
            FelRobesCreeperNeutral = BUILDER.comment("Whether wearing Fel Robes will cause Creeper mobs to be neutral, Default: true")
                    .define("felRobesCreeperNeutral", true);
            FelBootsSlimeNeutral = BUILDER.comment("Whether wearing Fel Boots of Wander will cause Slime mobs to be neutral, Default: true")
                    .define("felBootsSlimeNeutral", true);
            BUILDER.pop();
            BUILDER.push("Scythes");
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
            DarkScytheSouls = BUILDER.comment("Amount of Soul Energy Dark Scythe gives when hitting mob(s), Default: 1")
                    .defineInRange("darkScytheSouls", 1, 1, Integer.MAX_VALUE);
            ScytheSlashBreaks = BUILDER.comment("Scythe Slashes from Death Scythe breaks blocks that regular Scythes easily breaks, Default: true")
                    .define("scytheSlashBreaks", true);
            BUILDER.pop();
            BUILDER.push("Warped Spear");
            WarpedSpearDamage = BUILDER.comment("How much damage Warped Spear deals, Default: 10.0")
                    .defineInRange("warpedSpearDamage", 10.0, 1.0, Double.MAX_VALUE);
            WarpedSpearDurability = BUILDER.comment("How many uses before Warped Spear breaks, Default: 250")
                    .defineInRange("warpedSpearDurability", 250, 1, Integer.MAX_VALUE);
            WarpedSpearEnchantability = BUILDER.comment("Define the Enchantability for Warped Spear, higher number the better, Default: 1")
                    .defineInRange("warpedSpearEnchantability", 1, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Philosopher's Mace");
            PhilosophersMaceDamage = BUILDER.comment("How much damage Philosopher's Mace deals, Default: 9.0")
                    .defineInRange("philosophersMaceDamage", 9.0, 1.0, Double.MAX_VALUE);
            PhilosophersMaceDurability = BUILDER.comment("How many uses before the Philosopher's Mace breaks, Default: 128")
                    .defineInRange("philosophersMaceDurability", 128, 1, Integer.MAX_VALUE);
            PhilosophersMaceEnchantability = BUILDER.comment("Define the Enchantability for Philosopher's Mace, higher number the better, Default: 20")
                    .defineInRange("philosophersMaceEnchantability", 20, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Frost Tools");
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
        BUILDER.push("Curios");
        EmeraldAmuletSouls = BUILDER.comment("Amount of Soul Energy Emerald Amulet gives every 5 seconds, Default: 1")
                .defineInRange("emeraldAmuletSouls", 1, 1, Integer.MAX_VALUE);
        WitchBowSouls = BUILDER.comment("Amount of Soul Energy Witch's Bow required to shoot a tipped Arrow, Default: 25")
                .defineInRange("witchBowSouls", 25, 1, Integer.MAX_VALUE);
        ItemsRepairAmount = BUILDER.comment("Amount of Souls needed to repair certain Equipments per second, Default: 5")
                .defineInRange("darkArmoredRobeRepairSouls", 5, 1, Integer.MAX_VALUE);
        PendantOfHungerLimit = BUILDER.comment("The total amount of Rotten Flesh a Pendant of Hunger can hold, Default: 256")
                .defineInRange("pendantOfHungerLimit", 256, 1, Integer.MAX_VALUE);
        BUILDER.pop();
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
