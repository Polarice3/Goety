package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.world.features.CursedTotemFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Goety.MOD_ID);
    public static final RegistryObject<Feature<NoFeatureConfig>> CURSEDTOTEM = FEATURES.register("cursedtotemfeature", () -> (new CursedTotemFeature(NoFeatureConfig.CODEC)));

    public static void init(){
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
