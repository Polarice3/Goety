package com.Polarice3.Goety;

import com.Polarice3.Goety.client.inventory.container.ModContainerType;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.enchantments.ModEnchantmentsType;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.bosses.PenanceEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.*;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyImp;
import com.Polarice3.Goety.common.infamy.InfamyStore;
import com.Polarice3.Goety.common.infamy.ModNetwork;
import com.Polarice3.Goety.common.potions.ModPotions;
import com.Polarice3.Goety.common.tileentities.ModTileEntityType;
import com.Polarice3.Goety.common.world.features.ConfiguredFeatures;
import com.Polarice3.Goety.common.world.structures.ConfiguredStructures;
import com.Polarice3.Goety.compat.CuriosCompat;
import com.Polarice3.Goety.init.*;
import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.params.CoreProtocolPNames.PROTOCOL_VERSION;

@Mod("goety")
public class Goety {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "goety";

    public static SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(location("general"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public Goety() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModEntityType.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModTileEntityType.TILEENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModSpawnEggs.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModContainerType.CONTAINER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModEnchantmentsType.ENCHANTMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModParticleTypes.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModRecipeSerializer.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MainConfig.SPEC, "goety.toml");

        MainConfig.loadConfig(MainConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety.toml").toString());

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

        ModRegistry.init();
        ModStructures.init();
        ModFeatures.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IInfamy.class, new InfamyStore(), InfamyImp::new);
        MinecraftForge.EVENT_BUS.register(RegisterCommands.class);
        ModNetwork.init();

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TANK.get(), AbstractTankEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDTANK.get(), AbstractTankEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CHANNELLER.get(), ChannellerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FANATIC.get(), FanaticEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZEALOT.get(), ZealotEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.THUG.get(), ThugEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.DISCIPLE.get(), DiscipleEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.APOSTLE.get(), ApostleEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ENVIOKER.get(), EnviokerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.INQUILLAGER.get(), InquillagerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CONQUILLAGER.get(), ConquillagerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.HUSKARL.get(), HuskarlEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_COW.get(), MutatedCowEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_PIG.get(), MutatedPigEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_RABBIT.get(), MutatedRabbitEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SACRED_FISH.get(), SacredFishEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PARASITE.get(), ParasiteEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyScorchEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SKELETON_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAMED_SPIDER.get(), TamedSpiderEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.VIZIER.get(), VizierEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.IRK.get(), IrkEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SCORCH.get(), ScorchEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.NETHERNAL.get(), NethernalEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PENANCE.get(), PenanceEntity.setCustomAttributes().build());
        });

        CuriosCompat.setup(event);

        event.enqueueWork(() -> {
            ModStructures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });

    }

    public void biomeModification(final BiomeLoadingEvent event) {

        if (MainConfig.DarkManorGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.ICY
                    || event.getCategory() == Biome.Category.TAIGA) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_DARKMANOR);
            }
        }
        if (MainConfig.CursedGraveyardGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.SAVANNA
                    || event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.PLAINS
                    || event.getCategory() == Biome.Category.EXTREME_HILLS
                    || event.getCategory() == Biome.Category.SWAMP) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_CURSED_GRAVEYARD);
            }
        }
        if (MainConfig.SalvagedFortGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.ICY
                    || event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.PLAINS) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_SALVAGED_FORT);
            }
        }
        if (MainConfig.DecrepitFortGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.ICY
                    || event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.PLAINS) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_DECREPIT_FORT);
            }
        }
        if (MainConfig.TotemGen.get()) {
            if (event.getCategory() != Biome.Category.THEEND
                    && event.getCategory() != Biome.Category.MUSHROOM
                    && event.getCategory() != Biome.Category.NETHER
                    && event.getCategory() != Biome.Category.OCEAN
                    && event.getCategory() != Biome.Category.RIVER) {
                event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.CONFIGURED_CURSEDTOTEM);
            }
        }
        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PORTAL_OUTPOST);

    }

    private static Method GETCODEC_METHOD;

    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                Goety.LOGGER.error("Was unable to check if " + serverWorld.dimension().getRegistryName() + " is using Terraforged's ChunkGenerator.");
            }

            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(ModStructures.DARKMANOR.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.DARKMANOR.get()));
            tempMap.putIfAbsent(ModStructures.PORTAL_OUTPOST.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.PORTAL_OUTPOST.get()));
            tempMap.putIfAbsent(ModStructures.CURSED_GRAVEYARD.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.CURSED_GRAVEYARD.get()));
            tempMap.putIfAbsent(ModStructures.SALVAGED_FORT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.SALVAGED_FORT.get()));
            tempMap.putIfAbsent(ModStructures.DECREPIT_FORT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.DECREPIT_FORT.get()));

            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    public static final ItemGroup TAB = new ItemGroup("goetyTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModRegistry.GOLDTOTEM.get());
        }
    };
}
