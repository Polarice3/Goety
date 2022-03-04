package com.Polarice3.Goety.common.potions;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Goety.MOD_ID);

    public static final RegistryObject<Potion> MINING_FATIGUE = POTIONS.register("mining_fatigue",
            () -> new Potion("mining_fatigue", new EffectInstance(Effects.DIG_SLOWDOWN, 1800, 1)));

    public static final RegistryObject<Potion> MINOR_HARM = POTIONS.register("minorharm",
            () -> new Potion("minorharm", new EffectInstance(ModRegistryHandler.MINOR_HARM.get(), 1)));
}
