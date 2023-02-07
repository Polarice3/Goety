package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.world.features.RuinedRitualFeature;
import com.Polarice3.Goety.init.ModStructures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_DARK_MANOR = ModStructures.DARK_MANOR.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PORTAL_OUTPOST= ModStructures.PORTAL_OUTPOST.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_CURSED_GRAVEYARD= ModStructures.CURSED_GRAVEYARD.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_SALVAGED_FORT = ModStructures.SALVAGED_FORT.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_DECREPIT_FORT = ModStructures.DECREPIT_FORT.get().configured(NoFeatureConfig.NONE);
    public static final StructureFeature<RuinedRitualFeature, ? extends Structure<RuinedRitualFeature>> RUINED_RITUAL_STANDARD = ModStructures.RUINED_RITUAL.get().configured(new RuinedRitualFeature(RuinedRitualStructure.Location.STANDARD));
    public static final StructureFeature<RuinedRitualFeature, ? extends Structure<RuinedRitualFeature>> RUINED_RITUAL_JUNGLE = ModStructures.RUINED_RITUAL.get().configured(new RuinedRitualFeature(RuinedRitualStructure.Location.JUNGLE));
    public static StructureFeature<?, ?> CONFIGURED_CRIMSON_SHRINE = ModStructures.CRIMSON_SHRINE.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_WARPED_SHRINE = ModStructures.WARPED_SHRINE.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_VALLEY_SHRINE = ModStructures.VALLEY_SHRINE.get().configured(NoFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_dark_manor"), CONFIGURED_DARK_MANOR);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.DARK_MANOR.get(), CONFIGURED_DARK_MANOR);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_portal_outpost"), CONFIGURED_PORTAL_OUTPOST);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.PORTAL_OUTPOST.get(), CONFIGURED_PORTAL_OUTPOST);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_cursed_graveyard"), CONFIGURED_CURSED_GRAVEYARD);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.CURSED_GRAVEYARD.get(), CONFIGURED_CURSED_GRAVEYARD);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_salvaged_fort"), CONFIGURED_SALVAGED_FORT);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.SALVAGED_FORT.get(), CONFIGURED_SALVAGED_FORT);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_decrepit_fort"), CONFIGURED_DECREPIT_FORT);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.DECREPIT_FORT.get(), CONFIGURED_DECREPIT_FORT);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "ruined_ritual"), RUINED_RITUAL_STANDARD);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.RUINED_RITUAL.get(), RUINED_RITUAL_STANDARD);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "ruined_ritual_jungle"), RUINED_RITUAL_JUNGLE);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.RUINED_RITUAL.get(), RUINED_RITUAL_JUNGLE);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_crimson_shrine"), CONFIGURED_CRIMSON_SHRINE);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.CRIMSON_SHRINE.get(), CONFIGURED_CRIMSON_SHRINE);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_warped_shrine"), CONFIGURED_WARPED_SHRINE);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.WARPED_SHRINE.get(), CONFIGURED_WARPED_SHRINE);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_valley_shrine"), CONFIGURED_VALLEY_SHRINE);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.VALLEY_SHRINE.get(), CONFIGURED_VALLEY_SHRINE);
    }
}
