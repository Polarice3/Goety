package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.effects.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
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

    public static final RegistryObject<Effect> ILLAGUE = EFFECTS.register("illague",
            IllagueEffect::new);

    public static final RegistryObject<Effect> COSMIC = EFFECTS.register("cosmic",
            () -> new ModEffect(EffectType.NEUTRAL, 9044223));
    public static final RegistryObject<Effect> SUMMONDOWN = EFFECTS.register("summondown",
            () -> new ModEffect(EffectType.HARMFUL, 0));
    public static final RegistryObject<Effect> HOSTED = EFFECTS.register("hosted",
            () -> new ModEffect(EffectType.HARMFUL, 10044730));
    public static final RegistryObject<Effect> GOLDTOUCHED = EFFECTS.register("goldtouched",
            () -> new ModEffect(EffectType.HARMFUL, 4866583));
    public static final RegistryObject<Effect> CURSED = EFFECTS.register("cursed",
            () -> new ModEffect(EffectType.HARMFUL, 197379));
    public static final RegistryObject<Effect> SAPPED = EFFECTS.register("sapped",
            () -> new ModEffect(EffectType.HARMFUL, 197379));
    public static final RegistryObject<Effect> NOMINE = EFFECTS.register("nomine",
            () -> new ModEffect(EffectType.HARMFUL, 4866583));
    public static final RegistryObject<Effect> BURN_HEX = EFFECTS.register("apostle_curse",
            () -> new ModEffect(EffectType.HARMFUL, 2236962));
    public static final RegistryObject<Effect> HEALTH_LOSS = EFFECTS.register("health_loss",
            () -> new ModEffect(EffectType.HARMFUL, 9244735)
                    .addAttributeModifier(Attributes.MAX_HEALTH, "2f69ece0-0ddb-4e3a-9b44-4321548d7b71",
                            -4.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Effect> BUFF = EFFECTS.register("buff",
            () -> new ModEffect(EffectType.BENEFICIAL, 9643043)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, "f033b086-8a5e-44f2-8655-888dd700691c",
                            1.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Effect> TRAPPED = EFFECTS.register("trapped",
            () -> new ModEffect(EffectType.HARMFUL, 0)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "5b6594a7-03c7-47b4-bacb-16fbb661219d",
                            -0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "cea84175-df3e-42dc-b4d2-22b4a4c2c6c6",
                            1.0F, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Effect> DESICCATE = EFFECTS.register("desiccate",
            DesiccateEffect::new);
    public static final RegistryObject<Effect> SOUL_HUNGER = EFFECTS.register("soul_hunger",
            SoulHungerEffect::new);
    public static final RegistryObject<Effect> NECROPOWER = EFFECTS.register("necropower",
            NecroPowerEffect::new);
    public static final RegistryObject<Effect> FEL_VISION = EFFECTS.register("fel_vision",
            FelVisionEffect::new);
}
