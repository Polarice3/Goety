package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.world.features.RuinedRitualFeature;
import com.Polarice3.Goety.common.world.structures.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Goety.MOD_ID);
    public static final RegistryObject<Structure<NoFeatureConfig>> DARK_MANOR = STRUCTURES.register("dark_manor", DarkManorStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> PORTAL_OUTPOST = STRUCTURES.register("portal_outpost", () -> (new PortalOutpostStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> CURSED_GRAVEYARD = STRUCTURES.register("cursed_graveyard", () -> (new CursedGraveyardStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> SALVAGED_FORT = STRUCTURES.register("salvaged_fort", SalvagedFortStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> DECREPIT_FORT = STRUCTURES.register("decrepit_fort", DecrepitFortStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> CRIMSON_SHRINE = STRUCTURES.register("crimson_shrine", CrimsonShrineStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> WARPED_SHRINE = STRUCTURES.register("warped_shrine", WarpedShrineStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> VALLEY_SHRINE = STRUCTURES.register("valley_shrine", ValleyShrineStructure::new);
    public static final RegistryObject<Structure<RuinedRitualFeature>> RUINED_RITUAL = STRUCTURES.register("ruined_ritual",() -> (new RuinedRitualStructure(RuinedRitualFeature.CODEC)));

    public static void init(){
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(
                DARK_MANOR.get(),
                new StructureSeparationSettings(MainConfig.DarkManorSpacing.get(),
                        MainConfig.DarkManorSeperation.get(),
                        1543212345),
                true);
        setupMapSpacingAndLand(
                PORTAL_OUTPOST.get(),
                new StructureSeparationSettings(MainConfig.PortalOutpostSpacing.get(),
                        MainConfig.PortalOutpostSeperation.get(),
                        1654323456),
                true);
        setupMapSpacingAndLand(
                CURSED_GRAVEYARD.get(),
                new StructureSeparationSettings(MainConfig.CursedGraveyardSpacing.get(),
                        MainConfig.CursedGraveyardSeperation.get(),
                        1765434567),
                true);
        setupMapSpacingAndLand(
                SALVAGED_FORT.get(),
                new StructureSeparationSettings(MainConfig.SalvagedFortSpacing.get(),
                        MainConfig.SalvagedFortSeperation.get(),
                        1876545678),
                true);
        setupMapSpacingAndLand(
                DECREPIT_FORT.get(),
                new StructureSeparationSettings(MainConfig.DecrepitFortSpacing.get(),
                        MainConfig.DecrepitFortSeperation.get(),
                        1987656789),
                true);
        setupMapSpacingAndLand(
                RUINED_RITUAL.get(),
                new StructureSeparationSettings(MainConfig.RuinedRitualSpacing.get(),
                        MainConfig.RuinedRitualSeperation.get(),
                        1432101234),
                true);
        setupMapSpacingAndLand(
                CRIMSON_SHRINE.get(),
                new StructureSeparationSettings(MainConfig.CrimsonShrineSpacing.get(),
                        MainConfig.CrimsonShrineSeperation.get(),
                        1321010123),
                true);
        setupMapSpacingAndLand(
                WARPED_SHRINE.get(),
                new StructureSeparationSettings(MainConfig.WarpedShrineSpacing.get(),
                        MainConfig.WarpedShrineSeperation.get(),
                        1210101012),
                true);
        setupMapSpacingAndLand(
                VALLEY_SHRINE.get(),
                new StructureSeparationSettings(MainConfig.ValleyShrineSpacing.get(),
                        MainConfig.ValleyShrineSeperation.get(),
                        1101010101),
                true);

    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
