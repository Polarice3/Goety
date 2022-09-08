package com.Polarice3.Goety.common.world.features;

import com.Polarice3.Goety.common.world.structures.RuinedRitualStructure;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RuinedRitualFeature implements IFeatureConfig {
    public static final Codec<RuinedRitualFeature> CODEC = RuinedRitualStructure.Location.CODEC
            .fieldOf("location_type").xmap(RuinedRitualFeature::new,
                    (p_236629_0_) -> p_236629_0_.locationType).codec();
    public final RuinedRitualStructure.Location locationType;

    public RuinedRitualFeature(RuinedRitualStructure.Location p_i232016_1_) {
        this.locationType = p_i232016_1_;
    }
}
