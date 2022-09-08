package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.Goety;
import net.minecraft.util.ResourceLocation;

public class ModelUtils {
    public static ResourceLocation decorateBlockModelLocation(String pBlockModelLocation) {
        return new ResourceLocation(Goety.MOD_ID, "block/" + pBlockModelLocation);
    }
}
