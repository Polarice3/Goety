package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.potions.DesiccateEffect;
import com.Polarice3.Goety.common.potions.CosmicEffect;
import com.Polarice3.Goety.common.potions.IllagueEffect;
import com.Polarice3.Goety.common.potions.ModEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Goety.MOD_ID);

    public static void init(){
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Effect> COSMIC = EFFECTS.register("cosmic",
            CosmicEffect::new);
    public static final RegistryObject<Effect> ILLAGUE = EFFECTS.register("illague",
            IllagueEffect::new);

    public static final RegistryObject<Effect> SUMMONDOWN = EFFECTS.register("summondown",
            () -> new ModEffect(EffectType.HARMFUL, 0));
    public static final RegistryObject<Effect> HOSTED = EFFECTS.register("hosted",
            () -> new ModEffect(EffectType.HARMFUL, 10044730));
    public static final RegistryObject<Effect> GOLDTOUCHED = EFFECTS.register("goldtouched",
            () -> new ModEffect(EffectType.HARMFUL, 4866583));
    public static final RegistryObject<Effect> CURSED = EFFECTS.register("cursed",
            () -> new ModEffect(EffectType.HARMFUL, 197379));
    public static final RegistryObject<Effect> NOMINE = EFFECTS.register("nomine",
            () -> new ModEffect(EffectType.HARMFUL, 4866583));
    public static final RegistryObject<Effect> LAUNCH = EFFECTS.register("launch",
            () -> new ModEffect(EffectType.NEUTRAL, 0));
    public static final RegistryObject<Effect> NECROPOWER = EFFECTS.register("necropower",
            () -> new ModEffect(EffectType.NEUTRAL, 4393481));
    public static final RegistryObject<Effect> APOSTLE_CURSE = EFFECTS.register("apostle_curse",
            () -> new ModEffect(EffectType.HARMFUL, 2236962));
    public static final RegistryObject<Effect> SOUL_SHIELD = EFFECTS.register("soul_shield",
            () -> new ModEffect(EffectType.BENEFICIAL, 0));
    public static final RegistryObject<Effect> DESICCATE = EFFECTS.register("desiccate",
            DesiccateEffect::new);
}
