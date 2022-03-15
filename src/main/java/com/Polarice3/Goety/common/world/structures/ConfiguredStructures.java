package com.Polarice3.Goety.common.world.structures;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModStructures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_DARKMANOR= ModStructures.DARKMANOR.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PORTAL_OUTPOST= ModStructures.PORTAL_OUTPOST.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_CURSED_GRAVEYARD= ModStructures.CURSED_GRAVEYARD.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_SALVAGED_FORT = ModStructures.SALVAGED_FORT.get().configured(NoFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_darkmanor"), CONFIGURED_DARKMANOR);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.DARKMANOR.get(), CONFIGURED_DARKMANOR);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_portal_outpost"), CONFIGURED_PORTAL_OUTPOST);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.PORTAL_OUTPOST.get(), CONFIGURED_PORTAL_OUTPOST);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_cursed_graveyard"), CONFIGURED_CURSED_GRAVEYARD);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.CURSED_GRAVEYARD.get(), CONFIGURED_CURSED_GRAVEYARD);
        Registry.register(registry, new ResourceLocation(Goety.MOD_ID, "configured_salvaged_fort"), CONFIGURED_SALVAGED_FORT);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.SALVAGED_FORT.get(), CONFIGURED_SALVAGED_FORT);
    }
}
