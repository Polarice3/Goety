package com.Polarice3.Goety.common.world.features;

import com.Polarice3.Goety.init.ModFeatures;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> CONFIGURED_CURSEDTOTEM = register("cursedtotemfeature", ModFeatures.CURSEDTOTEM.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(512));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}
