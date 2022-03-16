package com.Polarice3.Goety.common.world.structures;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

/**
 * This class is based on a part of the Astral Sorcery Mod
 * The source code that is based for this mod can be found on Astral Sorcery github.
 * Code based made by HellFirePvP
 */
public abstract class ModStructureTemplate extends Structure<NoFeatureConfig> {
    public ModStructureTemplate() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getFeatureName() {
        return this.getRegistryName().toString();
    }
}
