package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModRitualFactory;
import com.Polarice3.Goety.init.ModRituals;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegisterEvents {

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        new RegistryBuilder<ModRitualFactory>().setName(Goety.location("ritual_factory"))
                .setType(ModRitualFactory.class).create();
        ModRituals.RITUALS.register(modEventBus);
    }
}
