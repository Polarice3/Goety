package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.gui.screen.inventory.FocusBagScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.SoulItemScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.WandandBagScreen;
import com.Polarice3.Goety.client.inventory.container.ModContainerType;
import com.Polarice3.Goety.client.render.*;
import com.Polarice3.Goety.client.render.tileentities.*;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TANK.get(), TankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDTANK.get(), FriendTankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHBOMB.get(), WitchBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_FIREBALL.get(), SoulFireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WARPED_SPEAR.get(), WarpedSpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PITCHFORK.get(), PitchforkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NETHERBALL.get(), NetherBallRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOULSKULL.get(), SoulSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FANG.get(), FangRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHGALE.get(), WitchGaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRETORNADO.get(), FireTornadoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CHANNELLER.get(), ChannellerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FANATIC.get(), FanaticRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZEALOT.get(), ZealotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.THUG.get(), ThugRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CRIMSON_SPIDER.get(), CrimsonSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DISCIPLE.get(), DiscipleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.APOSTLE.get(), ApostleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ENVIOKER.get(), EnviokerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.INQUILLAGER.get(), InquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CONQUILLAGER.get(), ConquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TORMENTOR.get(), TormentorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HUSKARL.get(), HuskarlRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BOOMER.get(), BoomerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_COW.get(), MutatedCowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_PIG.get(), MutatedPigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MUTATED_RABBIT.get(), MutantRabbitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SACRED_FISH.get(), SacredFishRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PARASITE.get(), ParasiteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_MINION.get(), SkeletonMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAMED_SPIDER.get(), LoyalSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.VIZIER.get(), VizierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.IRK.get(), IrkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SCORCH.get(), ScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NETHERNAL.get(), NethernalRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PENANCE.get(), PenanceRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LIGHTNINGTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRERAINTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRETORNADOTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.STORMUTIL.get(), TrapRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.FANG_TOTEM.get(), FangTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.MUTATE_TOTEM.get(), MutateTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.WIND_TOTEM.get(), WindTotemTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSEDBURNER.get(), CursedBurnerTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSED_CAGE.get(), CursedCageTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.DARK_ALTAR.get(), DarkAltarTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.PEDESTAL.get(), PedestalTileEntityRenderer::new);
        RenderTypeLookup.setRenderLayer(ModRegistry.CURSED_CAGE_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModRegistry.CURSED_BARS_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModRegistry.HAUNTED_CACTUS.get(), RenderType.cutout());
        ScreenManager.register(ModContainerType.WAND.get(), SoulItemScreen::new);
        ScreenManager.register(ModContainerType.FOCUSBAG.get(), FocusBagScreen::new);
        ScreenManager.register(ModContainerType.WANDANDBAG.get(), WandandBagScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

}
