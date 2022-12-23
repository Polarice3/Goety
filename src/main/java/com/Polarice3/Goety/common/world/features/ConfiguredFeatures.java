package com.Polarice3.Goety.common.world.features;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModFeatures;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;

import java.util.OptionalInt;

public class ConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> CONFIGURED_CURSEDTOTEM = register("cursed_totem_feature",
            ModFeatures.CURSEDTOTEM.get().configured(IFeatureConfig.NONE)
                    .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                    .chance(512));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> SAPLING_GLOOM_TREE =
            register("gloom_tree_sapling",
                    Feature.TREE.configured(getGloomTree()
                                    .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE))
                                    .ignoreVines()
                                    .heightmap(Heightmap.Type.MOTION_BLOCKING)
                                    .build()));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> SAPLING_MURK_TREE =
            register("murk_tree_sapling", Feature.TREE.configured(getMurkTree()
                            .ignoreVines()
                            .heightmap(Heightmap.Type.MOTION_BLOCKING)
                            .build()));
    public static final ConfiguredFeature<?, ?> GLOOM_TREE = register("gloom_tree",
            Feature.TREE.configured(getGloomTree()
                            .maxWaterDepth(1)
                            .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE))
                            .build())
                            .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                            .decorated(Placement.COUNT_EXTRA.configured(
                                    new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> MURK_TREE = register("murk_tree",
            Feature.TREE.configured(getMurkTree().build())
                    .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                    .decorated(Placement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(0, 0.01F, 1))));

    public static BaseTreeFeatureConfig.Builder getGloomTree(){
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(States.GLOOM_LOG),
                new SimpleBlockStateProvider(States.GLOOM_LEAVES),
                new AcaciaFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(2)),
                new FancyTrunkPlacer(6, 3, 0),
                new TwoLayerFeature(1, 0, 1));
    }

    public static BaseTreeFeatureConfig.Builder getMurkTree(){
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(States.MURK_LOG),
                new SimpleBlockStateProvider(States.MURK_LEAVES),
                new FancyFoliagePlacer(FeatureSpread.fixed(2),
                        FeatureSpread.fixed(4), 4),
                new FancyTrunkPlacer(9, 12, 0),
                new TwoLayerFeature(1, 0, 1,
                        OptionalInt.of(4)));
    }

    public static final class States {
        private static final BlockState GLOOM_LOG = ModBlocks.GLOOM_LOG.get().defaultBlockState();
        private static final BlockState GLOOM_LEAVES = ModBlocks.GLOOM_LEAVES.get().defaultBlockState();
        private static final BlockState MURK_LOG = ModBlocks.MURK_LOG.get().defaultBlockState();
        private static final BlockState MURK_LEAVES = ModBlocks.MURK_LEAVES.get().defaultBlockState();
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}
