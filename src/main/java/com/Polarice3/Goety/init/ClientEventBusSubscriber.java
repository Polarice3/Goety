package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.gui.screen.inventory.FocusBagScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.SoulItemScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.WandandBagScreen;
import com.Polarice3.Goety.client.inventory.container.ModContainerType;
import com.Polarice3.Goety.client.particles.GlowingParticle;
import com.Polarice3.Goety.client.particles.HugeDSEParticle;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.client.render.*;
import com.Polarice3.Goety.client.render.tileentities.*;
import com.Polarice3.Goety.common.blocks.ModWoodType;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.LargeExplosionParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
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
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TANK.get(), TankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FRIENDTANK.get(), FriendTankRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHBOMB.get(), WitchBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_FIREBALL.get(), SoulFireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MOD_FIREBALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 0.75F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LAVABALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 3.0F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WARPED_SPEAR.get(), WarpedSpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PITCHFORK.get(), PitchforkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOULSKULL.get(), SoulSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DESICCATEDSKULL.get(), CorruptSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_BULLET.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_LIGHT.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.GLOW_LIGHT.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.POISON_BALL.get(), PoisonBallRenderer::new);
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZPIGLIN_MINION.get(), ZPiglinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ZPiglinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ENVIOKER.get(), EnviokerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.INQUILLAGER.get(), InquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CONQUILLAGER.get(), ConquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TORMENTOR.get(), TormentorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HUSKARL.get(), HuskarlRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SHADE.get(), ShadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BOOMER.get(), BoomerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DUNE_SPIDER.get(), DuneSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FALLEN.get(), FallenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DESICCATED.get(), DesiccatedRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MARCIRE.get(), MarcireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LOCUST.get(), LocustRenderer::new);
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FARMER_MINION.get(), FarmerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ILLUSION_CLONE.get(), IllusionCloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAMED_SPIDER.get(), LoyalSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.VIZIER.get(), VizierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.IRK.get(), IrkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SCORCH.get(), ScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKULL_LORD.get(), SkullLordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BONE_LORD.get(), BoneLordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SENTINEL.get(), SentinelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PENANCE.get(), PenanceRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MOD_BOAT.get(), ModBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LIGHTNINGTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRERAINTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRETORNADOTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.STORMUTIL.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SUMMON_APOSTLE.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LASER.get(), TrapRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.FANG_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.MUTATE_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.WIND_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SOUL_FANG_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SOUL_LIGHT.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.GLOW_LIGHT.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSEDBURNER.get(), CursedBurnerTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSED_KILN.get(), CursedKilnTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSED_CAGE.get(), CursedCageTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.DARK_ALTAR.get(), DarkAltarTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.PEDESTAL.get(), PedestalTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.TEMP_WEB.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.ARCA.get(), ArcaTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SIGN_TILE_ENTITIES.get(), SignTileEntityRenderer::new);
        RenderTypeLookup.setRenderLayer(ModBlocks.CURSED_CAGE_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARCA_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CURSED_BARS_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CLOUD.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SOUL_LIGHT_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.DEAD_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.TEMP_WEB.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.HAUNTED_CACTUS.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.HAUNTED_BUSH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.IRON_FINGER.get(), RenderType.cutout());
        ScreenManager.register(ModContainerType.WAND.get(), SoulItemScreen::new);
        ScreenManager.register(ModContainerType.FOCUSBAG.get(), FocusBagScreen::new);
        ScreenManager.register(ModContainerType.WANDANDBAG.get(), WandandBagScreen::new);
        ModKeybindings.init();
        event.enqueueWork(() -> {
            Atlases.addWoodType(ModWoodType.HAUNTED);
        });
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        ParticleManager particles = Minecraft.getInstance().particleEngine;

        particles.register(ModParticleTypes.TOTEM_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.PLAGUE_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.BULLET_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.GLOW_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.HEAL_EFFECT.get(), HeartParticle.Factory::new);
        particles.register(ModParticleTypes.SOUL_LIGHT_EFFECT.get(), GlowingParticle.Factory::new);
        particles.register(ModParticleTypes.GLOW_LIGHT_EFFECT.get(), GlowingParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), LargeExplosionParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION_EMITTER.get(), new HugeDSEParticle.Factory());
    }

}
