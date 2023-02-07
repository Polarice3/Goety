package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.events.BossBarEvent;
import com.Polarice3.Goety.client.gui.screen.inventory.FocusBagScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.SoulItemScreen;
import com.Polarice3.Goety.client.gui.screen.inventory.WandandBagScreen;
import com.Polarice3.Goety.client.inventory.container.ModContainerType;
import com.Polarice3.Goety.client.particles.*;
import com.Polarice3.Goety.client.render.*;
import com.Polarice3.Goety.client.render.layers.PlayerSoulShieldLayer;
import com.Polarice3.Goety.client.render.tileentities.*;
import com.Polarice3.Goety.common.blocks.ModWoodType;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import com.Polarice3.Goety.common.items.equipment.NetheriteBowItem;
import com.Polarice3.Goety.common.items.magic.GoldTotemItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHBOMB.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 0.75F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BURNING_POTION.get(), (rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 0.75F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.NETHER_METEOR.get(), NetherMeteorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MOD_FIREBALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 0.75F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LAVABALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 3.0F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.GRAND_LAVABALL.get(), GrandLavaballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MOD_DRAGON_FIREBALL.get(), ModDragonFireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FROST_BALL.get(), FrostBallRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DEAD_SLIME_BALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer, 0.75F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SWORD.get(),(rendererManager) -> new SwordProjectileRenderer<>(rendererManager, itemRenderer, 1.25F, true));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SCYTHE.get(), ScytheProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WARPED_SPEAR.get(), WarpedSpearRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PITCHFORK.get(), PitchforkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_SKULL.get(), SoulSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DESICCATED_SKULL.get(), CorruptSkullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DEAD_TNT.get(), DeadTNTRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ICE_STORM.get(), IceStormRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_BULLET.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SOUL_LIGHT.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.GLOW_LIGHT.get(), SoulBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.POISON_BALL.get(),(rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FANG.get(), FangRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPIKE.get(), SpikeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ROOTS.get(), RootsTrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.GHOST_FIRE.get(), GhostFireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ICE_CHUNK.get(), IceChunkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WITCHGALE.get(), WitchGaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRETORNADO.get(), FireTornadoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CHANNELLER.get(), ChannellerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FANATIC.get(), FanaticRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZEALOT.get(), ZealotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.THUG.get(), ThugRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CRIMSON_SPIDER.get(), CrimsonSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DISCIPLE.get(), DiscipleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BELDAM.get(), BeldamRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.APOSTLE.get(), ApostleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonVillagerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZPIGLIN_MINION.get(), ZPiglinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ZPiglinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.RETURNED.get(), ReturnedRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MALGHAST.get(), MalghastRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HOGLORD.get(), HogLordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ENVIOKER.get(), EnviokerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.INQUILLAGER.get(), InquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CONQUILLAGER.get(), ConquillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TORMENTOR.get(), TormentorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HUSKARL.get(), HuskarlRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SHADE.get(), ShadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DREDEN.get(), DredenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WRAITH.get(), WraithRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.URBHADHACH.get(), UrbhadhachRenderer::new);
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.HUSK_MINION.get(), HuskMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DROWNED_MINION.get(), DrownedMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKELETON_MINION.get(), SkeletonMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.STRAY_MINION.get(), SkeletonMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.DREDEN_MINION.get(), DredenMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.WRAITH_MINION.get(), WraithMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FARMER_MINION.get(), FarmerMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.UNDEAD_WOLF_MINION.get(), UndeadWolfRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.PHANTOM_MINION.get(), PhantomMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ILLUSION_CLONE.get(), IllusionCloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FEL_FLY.get(), FelFlyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ROT_TREE.get(), RotTreeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.TAMED_SPIDER.get(), LoyalSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.VIZIER.get(), VizierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.IRK.get(), IrkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SCORCH.get(), ScorchRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SKULL_LORD.get(), SkullLordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BONE_LORD.get(), BoneLordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SENTINEL.get(), SentinelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MOD_BOAT.get(), ModBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LIGHTNINGTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRERAINTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ARROWRAINTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIRETORNADOTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FIREBLASTTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.MAGICBLASTTRAP.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.BURNING_GROUND.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.POISON_GROUND.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.STORMUTIL.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SUMMON_APOSTLE.get(), TrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.LASER.get(), TrapRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.FANG_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.MUTATE_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.WIND_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SOUL_FANG_TOTEM.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SOUL_LIGHT.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.GLOW_LIGHT.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.FORBIDDEN_GRASS.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.GHOST_FIRE_TRAP.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CULT_STATUE.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSEDBURNER.get(), CursedBurnerTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSED_KILN.get(), CursedKilnTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.CURSED_CAGE.get(), CursedCageTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.DARK_ALTAR.get(), DarkAltarTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.PEDESTAL.get(), PedestalTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SOUL_ABSORBER.get(), SoulAbsorberTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.TEMP_WEB.get(), ModTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.ARCA.get(), ArcaTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.SIGN_TILE_ENTITIES.get(), SignTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityType.TALL_SKULL.get(), TallSkullTileEntityRenderer::new);
        RenderTypeLookup.setRenderLayer(ModBlocks.CURSED_CAGE_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARCA_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CURSED_BARS_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CLOUD.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SOUL_LIGHT_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.DEAD_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.FALSE_PORTAL.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.TEMP_WEB.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.HAUNTED_CACTUS.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.HAUNTED_BUSH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.IRON_FINGER.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLOOM_TRAPDOOR.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.MURK_TRAPDOOR.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLOOM_DOOR.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLOOM_SAPLING.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.MURK_SAPLING.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GRAND_TORCH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WALL_GRAND_TORCH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GRAND_SOUL_TORCH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WALL_GRAND_SOUL_TORCH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLOOM_LEAVES.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.MURK_LEAVES.get(), RenderType.cutoutMipped());
        ScreenManager.register(ModContainerType.WAND.get(), SoulItemScreen::new);
        ScreenManager.register(ModContainerType.FOCUSBAG.get(), FocusBagScreen::new);
        ScreenManager.register(ModContainerType.WANDANDBAG.get(), WandandBagScreen::new);
        ModKeybindings.init();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(BossBarEvent::renderBossBar);
        event.enqueueWork(() -> {
            Atlases.addWoodType(ModWoodType.HAUNTED);
            Atlases.addWoodType(ModWoodType.GLOOM);
            Atlases.addWoodType(ModWoodType.MURK);
        });
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        for (PlayerRenderer render : skinMap.values()) {
            render.addLayer(new PlayerSoulShieldLayer(render));
        }

        ItemModelsProperties.register(ModItems.GOLDTOTEM.get(), new ResourceLocation("souls"),
                (stack, world, living) -> ((float) GoldTotemItem.currentSouls(stack)) / GoldTotemItem.MAXSOULS);
        ItemModelsProperties.register(ModItems.GOLDTOTEM.get(), new ResourceLocation("activated"),
                (stack, world, living) -> GoldTotemItem.isActivated(stack) ? 1.0F : 0.0F);
        ItemModelsProperties.register(ModItems.NETHERITE_BOW.get(), new ResourceLocation("pull"),
                (stack, world, living) -> {
                    if (living == null) {
                        return 0.0F;
                    } else {
                        return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / NetheriteBowItem.getBowTime();
                    }
                });
        ItemModelsProperties.register(ModItems.NETHERITE_BOW.get(), new ResourceLocation("pulling")
                , (stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
        ItemModelsProperties.register(ModItems.WITCH_BOW.get(), new ResourceLocation("pull"),
                (stack, world, living) -> {
                    if (living == null) {
                        return 0.0F;
                    } else {
                        return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20;
                    }
                });
        ItemModelsProperties.register(ModItems.WITCH_BOW.get(), new ResourceLocation("pulling")
                , (stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
        ItemModelsProperties.register(ModItems.PITCHFORK.get(), new ResourceLocation("throwing")
                , (stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
        ItemModelsProperties.register(ModItems.WARPED_SPEAR.get(), new ResourceLocation("throwing")
                , (stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void colorLeavesBlock(ColorHandlerEvent.Block event){
        event.getBlockColors().register(
                (state, lightReader, pos, color) ->
                        lightReader != null && pos != null ? BiomeColors.getAverageFoliageColor(lightReader, pos) :
                        FoliageColors.get(0.5D, 1.0D), ModBlocks.GLOOM_LEAVES.get(), ModBlocks.MURK_LEAVES.get());
    }

    @SubscribeEvent
    public static void colorLeavesItem(ColorHandlerEvent.Item event){
        IItemColor handler = (stack, tint) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(state, null, null, tint);
        };
        event.getItemColors().register(handler, ModBlocks.GLOOM_LEAVES.get(), ModBlocks.MURK_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        ParticleManager particles = Minecraft.getInstance().particleEngine;

        particles.register(ModParticleTypes.TOTEM_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.WHITE_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.PLAGUE_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.BULLET_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.GLOW_EFFECT.get(), SpellParticle.Factory::new);
        particles.register(ModParticleTypes.LEECH.get(), FlameParticle.Factory::new);
        particles.register(ModParticleTypes.WRAITH.get(), WraithParticle.Factory::new);
        particles.register(ModParticleTypes.HEAL_EFFECT.get(), HeartParticle.Factory::new);
        particles.register(ModParticleTypes.SOUL_LIGHT_EFFECT.get(), GlowingParticle.Factory::new);
        particles.register(ModParticleTypes.GLOW_LIGHT_EFFECT.get(), GlowingParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), LargeExplosionParticle.Factory::new);
        particles.register(ModParticleTypes.DEAD_SAND_EXPLOSION_EMITTER.get(), new HugeDSEParticle.Factory());
        particles.register(ModParticleTypes.LASER_GATHER.get(), GatheringParticle.Factory::new);
        particles.register(ModParticleTypes.SONIC_GATHER.get(), GatheringParticle.Factory::new);
        particles.register(ModParticleTypes.FLAME_GATHER.get(), GatheringParticle.Factory::new);
        particles.register(ModParticleTypes.POISON.get(), FlameParticle.Factory::new);
        particles.register(ModParticleTypes.BURNING.get(), FlameParticle.Factory::new);
        particles.register(ModParticleTypes.CULT_SPELL.get(), SpellParticle.MobFactory::new);
        particles.register(ModParticleTypes.SONIC_BOOM.get(), SonicBoomParticle.Factory::new);
        particles.register(ModParticleTypes.CONFUSED.get(), HeartParticle.Factory::new);
    }

}
