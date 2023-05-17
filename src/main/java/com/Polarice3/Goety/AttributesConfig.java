package com.Polarice3.Goety;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class AttributesConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> OverrideAttributes;

    public static final ForgeConfigSpec.ConfigValue<Double> FanaticHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> FanaticDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ZealotHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ThugHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ThugDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> CrimsonSpiderHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> CrimsonSpiderDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> DiscipleHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> BeldamHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ChannellerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ZombieVillagerServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ZombieVillagerServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SkeletonVillagerServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> SkeletonVillagerServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ZPiglinServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ZPiglinServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ZPiglinBruteServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ZPiglinBruteServantDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> MalghastHealth;

    public static final ForgeConfigSpec.ConfigValue<Double> HoglordHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> HoglordDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> EnviokerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> EnviokerDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> TormentorHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> TormentorDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> InquillagerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> InquillagerDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ConquillagerHealth;

    public static final ForgeConfigSpec.ConfigValue<Double> DredenHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DredenDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> WraithHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> WraithDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> UrbhadhachHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> UrbhadhachDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> FallenHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> FallenDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> DesiccatedHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DesiccatedDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> DuneSpiderHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DuneSpiderDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> BlightHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> BlightDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> BoomerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> MarcireHealth;

    public static final ForgeConfigSpec.ConfigValue<Double> ZombieServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ZombieServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SkeletonServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> SkeletonServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> UndeadWolfHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> UndeadWolfDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> NecroUndeadWolfHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> NecroUndeadWolfDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> PhantomServantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> PhantomServantDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SummonedVexHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> SummonedVexDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> RottreantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> RottreantDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> LoyalSpiderHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> LoyalSpiderDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> SkullLordHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> SkullLordDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> BoneLordHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> BoneLordDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> ApostleHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ApostleMagicDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> VizierHealth;

    public static final ForgeConfigSpec.ConfigValue<Integer> ApostleDamageCap;
    public static final ForgeConfigSpec.ConfigValue<Integer> ApostleBowDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> VizierDamageCap;

    static {
        BUILDER.push("General");
        OverrideAttributes = BUILDER.comment("Allows newly Configured Attributes to override pre-existing ones, Default: true")
                        .define("overrideAttributes", true);
        BUILDER.pop();
        BUILDER.push("Attributes");
            BUILDER.push("Cultists");
            ZealotHealth = BUILDER.comment("How much Max Health Zealot have, Default: 20.0")
                    .defineInRange("zealotHealth", 20.0, 1.0, Double.MAX_VALUE);
            DiscipleHealth = BUILDER.comment("How much Max Health Disciple have, Default: 26.0")
                    .defineInRange("discipleHealth", 26.0, 1.0, Double.MAX_VALUE);
            BeldamHealth = BUILDER.comment("How much Max Health Beldam have, Default: 26.0")
                    .defineInRange("beldamHealth", 26.0, 1.0, Double.MAX_VALUE);
            ChannellerHealth = BUILDER.comment("How much Max Health Channeller have, Default: 32.0")
                    .defineInRange("channellerHealth", 32.0, 1.0, Double.MAX_VALUE);
            MalghastHealth = BUILDER.comment("How much Max Health Malghast have, Default: 20.0")
                    .defineInRange("malghastHealth", 20.0, 1.0, Double.MAX_VALUE);
                BUILDER.push("Fanatic");
                FanaticHealth = BUILDER.comment("How much Max Health Fanatic have, Default: 20.0")
                        .defineInRange("fanaticHealth", 20.0, 1.0, Double.MAX_VALUE);
                FanaticDamage = BUILDER.comment("How much damage Fanatic deals, Default: 3.0")
                        .defineInRange("fanaticDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Thug");
                ThugHealth = BUILDER.comment("How much Max Health Thug have, Default: 50.0")
                        .defineInRange("thugHealth", 50.0, 1.0, Double.MAX_VALUE);
                ThugDamage = BUILDER.comment("How much damage Thug deals, Default: 7.0")
                        .defineInRange("thugDamage", 7.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Crimson Spider");
                CrimsonSpiderHealth = BUILDER.comment("How much Max Health Crimson Spider have, Default: 24.0")
                        .defineInRange("crimsonSpiderHealth", 24.0, 1.0, Double.MAX_VALUE);
                CrimsonSpiderDamage = BUILDER.comment("How much damage Crimson Spider deals, Default: 2.0")
                        .defineInRange("crimsonSpiderDamage", 2.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Zombie Villager Servant");
                ZombieVillagerServantHealth = BUILDER.comment("How much Max Health Zombie Villager Servants have, Default: 20.0")
                        .defineInRange("zombieVillagerServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                ZombieVillagerServantDamage = BUILDER.comment("How much damage Zombie Villager Servants deals, Default: 3.0")
                        .defineInRange("zombieVillagerServantDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Skeleton Villager Servant");
                SkeletonVillagerServantHealth = BUILDER.comment("How much Max Health Skeleton Villager Servants have, Default: 20.0")
                        .defineInRange("skeletonVillagerServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                SkeletonVillagerServantDamage = BUILDER.comment("How much damage Skeleton Villager Servants deals, Default: 2.0")
                        .defineInRange("skeletonVillagerServantDamage", 2.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Zombified Piglin Servant");
                ZPiglinServantHealth = BUILDER.comment("How much Max Health Zombified Piglin Servants have, Default: 20.0")
                        .defineInRange("zombifiedPiglinServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                ZPiglinServantDamage = BUILDER.comment("How much damage Zombified Piglin Servants deals, Default: 5.0")
                        .defineInRange("zombifiedPiglinServantDamage", 5.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Zombified Piglin Brute Servant");
                ZPiglinBruteServantHealth = BUILDER.comment("How much Max Health Zombified Piglin Brute Servants have, Default: 50.0")
                        .defineInRange("zombifiedPiglinBruteServantHealth", 50.0, 1.0, Double.MAX_VALUE);
                ZPiglinBruteServantDamage = BUILDER.comment("How much damage Zombified Piglin Brute Servants deals, Default: 7.0")
                        .defineInRange("zombifiedPiglinBruteServantDamage", 7.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
            BUILDER.pop();
            BUILDER.push("Illagers");
                BUILDER.push("Envioker");
                EnviokerHealth = BUILDER.comment("How much Max Health Envioker have, Default: 24.0")
                        .defineInRange("enviokerHealth", 24.0, 1.0, Double.MAX_VALUE);
                EnviokerDamage = BUILDER.comment("How much damage Envioker deals, Default: 5.0")
                        .defineInRange("enviokerDamage", 5.0, 1.0, Double.MAX_VALUE);
                TormentorHealth = BUILDER.comment("How much Max Health Tormentor have, Default: 24.0")
                        .defineInRange("tormentorHealth", 24.0, 1.0, Double.MAX_VALUE);
                TormentorDamage = BUILDER.comment("How much damage Tormentor deals, Default: 4.0")
                        .defineInRange("tormentorDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Inquillager");
                InquillagerHealth = BUILDER.comment("How much Max Health Inquillager have, Default: 24.0")
                        .defineInRange("inquillagerHealth", 24.0, 1.0, Double.MAX_VALUE);
                InquillagerDamage = BUILDER.comment("How much damage Inquillager deals, Default: 5.0")
                        .defineInRange("inquillagerDamage", 5.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
            ConquillagerHealth = BUILDER.comment("How much Max Health Conquillager have, Default: 24.0")
                    .defineInRange("conquillagerHealth", 24.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("General");
                BUILDER.push("Hoglord");
                HoglordHealth = BUILDER.comment("How much Max Health Hoglord have, Default: 200.0")
                        .defineInRange("hoglordHealth", 200.0, 1.0, Double.MAX_VALUE);
                HoglordDamage = BUILDER.comment("How much damage Hoglord deals, Default: 6.0")
                        .defineInRange("hoglordDamage", 6.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Wraith");
                WraithHealth = BUILDER.comment("How much Max Health Wraiths have, Default: 25.0")
                        .defineInRange("wraithHealth", 25.0, 1.0, Double.MAX_VALUE);
                WraithDamage = BUILDER.comment("How much damage Wraith deals, Default: 4.0")
                        .defineInRange("wraithDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Dreden");
                DredenHealth = BUILDER.comment("How much Max Health Dredens have, Default: 30.0")
                        .defineInRange("dredenHealth", 30.0, 1.0, Double.MAX_VALUE);
                DredenDamage = BUILDER.comment("How much damage Dreden deals, Default: 4.0")
                        .defineInRange("dredenDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Urbhadhach");
                UrbhadhachHealth = BUILDER.comment("How much Max Health Urbhadhach have, Default: 32.0")
                        .defineInRange("urbhadhachHealth", 32.0, 1.0, Double.MAX_VALUE);
                UrbhadhachDamage = BUILDER.comment("How much damage Urbhadhach deals, Default: 6.0")
                        .defineInRange("urbhadhachDamage", 6.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Loyal Spider");
                LoyalSpiderHealth = BUILDER.comment("How much Max Health Loyal Spider initially have, Default: 16.0")
                        .defineInRange("loyalSpiderHealth", 16.0, 1.0, Double.MAX_VALUE);
                LoyalSpiderDamage = BUILDER.comment("How much damage Loyal Spider initially deals, Default: 2.0")
                        .defineInRange("loyalSpiderDamage", 2.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Skull Lord");
                SkullLordHealth = BUILDER.comment("How much Max Health Skull Lord have, Default: 100.0")
                        .defineInRange("skullLordHealth", 100.0, 1.0, Double.MAX_VALUE);
                SkullLordDamage = BUILDER.comment("How much damage Skull Lord deals, Default: 6.0")
                        .defineInRange("skullLordDamage", 6.0, 1.0, Double.MAX_VALUE);
                BoneLordHealth = BUILDER.comment("How much Max Health Bone Lord have, Default: 20.0")
                        .defineInRange("boneLordHealth", 20.0, 1.0, Double.MAX_VALUE);
                BoneLordDamage = BUILDER.comment("How much damage Bone Lord deals, Default: 3.0")
                        .defineInRange("boneLordDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
            BUILDER.pop();
            BUILDER.push("Dead Sand Mobs");
                BUILDER.push("Fallen");
                FallenHealth = BUILDER.comment("How much Max Health Fallen have, Default: 20.0")
                        .defineInRange("fallenHealth", 20.0, 1.0, Double.MAX_VALUE);
                FallenDamage = BUILDER.comment("How much damage Fallen deals, Default: 3.0")
                        .defineInRange("fallenDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Desiccated");
                DesiccatedHealth = BUILDER.comment("How much Max Health Desiccated have, Default: 20.0")
                        .defineInRange("desiccatedHealth", 20.0, 1.0, Double.MAX_VALUE);
                DesiccatedDamage = BUILDER.comment("How much damage Desiccated deals, Default: 3.0")
                        .defineInRange("desiccatedDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Dune Spider");
                DuneSpiderHealth = BUILDER.comment("How much Max Health Dune Spider have, Default: 16.0")
                        .defineInRange("duneSpiderHealth", 16.0, 1.0, Double.MAX_VALUE);
                DuneSpiderDamage = BUILDER.comment("How much damage Dune Spider deals, Default: 4.0")
                        .defineInRange("duneSpiderDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Blight");
                BlightHealth = BUILDER.comment("How much Max Health Blight have, Default: 25.0")
                        .defineInRange("blightHealth", 25.0, 1.0, Double.MAX_VALUE);
                BlightDamage = BUILDER.comment("How much damage Blight deals, Default: 4.0")
                        .defineInRange("blightDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
            BoomerHealth = BUILDER.comment("How much Max Health Boomer have, Default: 20.0")
                    .defineInRange("boomerHealth", 20.0, 1.0, Double.MAX_VALUE);
            MarcireHealth = BUILDER.comment("How much Max Health Marcire have, Default: 20.0")
                    .defineInRange("marcireHealth", 20.0, 1.0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Summoned Mobs");
                BUILDER.push("Zombie Servant");
                ZombieServantHealth = BUILDER.comment("How much Max Health Zombie Servants and variants have, Default: 20.0")
                        .defineInRange("zombieServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                ZombieServantDamage = BUILDER.comment("How much damage Zombie Servants and variants deals, Default: 3.0")
                        .defineInRange("zombieServantDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Skeleton Servant");
                SkeletonServantHealth = BUILDER.comment("How much Max Health Skeleton Servants and variants have, Default: 20.0")
                        .defineInRange("skeletonServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                SkeletonServantDamage = BUILDER.comment("How much damage Skeleton Servants and variants deals, Default: 3.0")
                        .defineInRange("skeletonServantDamage", 3.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Undead Wolf Servant");
                UndeadWolfHealth = BUILDER.comment("How much Max Health Undead Wolf have, Default: 8.0")
                        .defineInRange("undeadWolfHealth", 8.0, 1.0, Double.MAX_VALUE);
                UndeadWolfDamage = BUILDER.comment("How much damage Undead Wolf deals, Default: 2.0")
                        .defineInRange("undeadWolfDamage", 2.0, 1.0, Double.MAX_VALUE);
                NecroUndeadWolfHealth = BUILDER.comment("How much Max Health Undead Wolf when summoned with Necro Robes have, Default: 20.0")
                        .defineInRange("necroUndeadWolfHealth", 20.0, 1.0, Double.MAX_VALUE);
                NecroUndeadWolfDamage = BUILDER.comment("How much damage Undead Wolf when summoned with Necro Robes deals, Default: 4.0")
                        .defineInRange("necroUndeadWolfDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Phantom Servant");
                PhantomServantHealth = BUILDER.comment("How much Max Health Phantom Servants and variants have, Default: 20.0")
                        .defineInRange("phantomServantHealth", 20.0, 1.0, Double.MAX_VALUE);
                PhantomServantDamage = BUILDER.comment("How much damage Phantom Servants and variants deals, Default: 2.0")
                        .defineInRange("phantomServantDamage", 2.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Vex");
                SummonedVexHealth = BUILDER.comment("How much Max Health Summoned Vexes have, Default: 14.0")
                        .defineInRange("summonedVexHealth", 14.0, 1.0, Double.MAX_VALUE);
                SummonedVexDamage = BUILDER.comment("How much damage Summoned Vexes deals, Default: 4.0")
                        .defineInRange("summonedVexDamage", 4.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Rottreant");
                RottreantHealth = BUILDER.comment("How much Max Health Rottreant have, Default: 75.0")
                        .defineInRange("rottreantHealth", 75.0, 1.0, Double.MAX_VALUE);
                RottreantDamage = BUILDER.comment("How much damage Rottreant deals, Default: 6.0")
                        .defineInRange("rottreantDamage", 6.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
            BUILDER.pop();
            BUILDER.push("Bosses");
                BUILDER.push("Vizier");
                VizierHealth = BUILDER.comment("How much Max Health Viziers have, Default: 200.0")
                        .defineInRange("vizierHealth", 200.0, 100.0, Double.MAX_VALUE);
                VizierDamageCap = BUILDER.comment("The maximum amount of damage a Vizier can attain per hit, Default: 20")
                        .defineInRange("vizierDamageCap", 20, 1, Integer.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Apostle");
                ApostleHealth = BUILDER.comment("How much Max Health Apostles have, Default: 320.0")
                        .defineInRange("apostleHealth", 320.0, 100.0, Double.MAX_VALUE);
                ApostleDamageCap = BUILDER.comment("The maximum amount of damage an Apostle can attain per hit, Default: 20")
                        .defineInRange("apostleDamageCap", 20, 1, Integer.MAX_VALUE);
                ApostleBowDamage = BUILDER.comment("Multiplies Apostle's Bow damage, Default: 4")
                        .defineInRange("apostleBowDamage", 4, 2, Integer.MAX_VALUE);
                ApostleMagicDamage = BUILDER.comment("Set Apostle's spell damage (ie, Fire Tornadoes, Fire Blasts, Roars), Default: 6.0")
                        .defineInRange("apostleMagicDamage", 6.0, 6.0, Double.MAX_VALUE);
                BUILDER.pop();
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
