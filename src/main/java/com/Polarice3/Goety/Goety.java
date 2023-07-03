package com.Polarice3.Goety;

import com.Polarice3.Goety.client.ClientProxy;
import com.Polarice3.Goety.client.inventory.container.ModContainerType;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.CommonProxy;
import com.Polarice3.Goety.common.blocks.ModWoodType;
import com.Polarice3.Goety.common.capabilities.lichdom.ILichdom;
import com.Polarice3.Goety.common.capabilities.lichdom.LichImp;
import com.Polarice3.Goety.common.capabilities.lichdom.LichStore;
import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEImp;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEStore;
import com.Polarice3.Goety.common.capabilities.spider.ISpiderLevels;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsImp;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsStore;
import com.Polarice3.Goety.common.effects.ModPotions;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.*;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.hostile.dead.*;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.entities.projectiles.*;
import com.Polarice3.Goety.common.entities.utilities.LaserEntity;
import com.Polarice3.Goety.common.fluid.ModFluids;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.world.features.ConfiguredFeatures;
import com.Polarice3.Goety.common.world.structures.ConfiguredStructures;
import com.Polarice3.Goety.compat.curios.CuriosCompat;
import com.Polarice3.Goety.init.*;
import com.Polarice3.Goety.utils.ModPotionUtil;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.WoodType;
import net.minecraft.dispenser.*;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potions;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod("goety")
public class Goety {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "goety";
    public static final ModProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public Goety() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntityType.ENTITY_TYPES.register(eventBus);
        ModTileEntityType.TILE_ENTITY_TYPES.register(eventBus);
        ModSpawnEggs.ITEMS.register(eventBus);
        ModPotions.POTIONS.register(eventBus);
        ModContainerType.CONTAINER_TYPE.register(eventBus);
        ModEnchantments.ENCHANTMENTS.register(eventBus);
        ModParticleTypes.PARTICLE_TYPES.register(eventBus);
        ModRecipeSerializer.RECIPE_SERIALIZERS.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::setupEntityAttributeCreation);
        eventBus.addListener(this::enqueueIMC);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.register(this);
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MainConfig.SPEC, "goety.toml");
        MainConfig.loadConfig(MainConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety.toml").toString());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AttributesConfig.SPEC, "goety-attributes.toml");
        AttributesConfig.loadConfig(AttributesConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety-attributes.toml").toString());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpellConfig.SPEC, "goety-spells.toml");
        SpellConfig.loadConfig(SpellConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety-spells.toml").toString());

        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        ModEffects.init();
        ModSounds.init();
        ModStructures.init();
        ModFeatures.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ILichdom.class, new LichStore(), LichImp::new);
        CapabilityManager.INSTANCE.register(ISoulEnergy.class, new SEStore(), SEImp::new);
        CapabilityManager.INSTANCE.register(ISpiderLevels.class, new SpiderLevelsStore(), SpiderLevelsImp::new);
        MinecraftForge.EVENT_BUS.register(RegisterCommands.class);
        ModNetwork.init();

        CuriosCompat.setup(event);

        event.enqueueWork(() -> {
            EntitySpawnPlacementRegistry();
            ModStructures.setupStructures();
            DispenserBlock.registerBehavior(ModBlocks.TALL_SKULL_ITEM.get(), new OptionalDispenseBehavior() {
                protected ItemStack execute(IBlockSource source, ItemStack stack) {
                    this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                    return stack;
                }
            });
            DispenserBlock.registerBehavior(ModItems.WITCH_BOMB.get(), new ProjectileDispenseBehavior() {
                protected ProjectileEntity getProjectile(World pLevel, IPosition pPosition, ItemStack pStack) {
                    return Util.make(new WitchBombEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), (witchBomb) -> {
                        witchBomb.setItem(pStack);
                    });
                }
            });
            DispenserBlock.registerBehavior(ModItems.DEAD_SLIME_BALL.get(), new ProjectileDispenseBehavior() {
                protected ProjectileEntity getProjectile(World pLevel, IPosition pPosition, ItemStack pStack) {
                    return Util.make(new DeadSlimeBallEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), (deadSlimeBall) -> {
                        deadSlimeBall.setItem(pStack);
                    });
                }
            });
            DispenserBlock.registerBehavior(ModItems.BURNING_POTION.get(), new IDispenseItemBehavior() {
                public ItemStack dispense(IBlockSource p_dispense_1_, ItemStack p_dispense_2_) {
                    return (new ProjectileDispenseBehavior() {
                        protected ProjectileEntity getProjectile(World pLevel, IPosition pPosition, ItemStack pStack) {
                            return Util.make(new BurningPotionEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), (burningPotion) -> {
                                burningPotion.setItem(pStack);
                            });
                        }

                        protected float getUncertainty() {
                            return super.getUncertainty() * 0.5F;
                        }

                        protected float getPower() {
                            return super.getPower() * 1.25F;
                        }
                    }).dispense(p_dispense_1_, p_dispense_2_);
                }
            });
            DispenserBlock.registerBehavior(ModBlocks.DEAD_TNT.get(), new DefaultDispenseItemBehavior() {
                protected ItemStack execute(IBlockSource pSource, ItemStack pStack) {
                    World world = pSource.getLevel();
                    BlockPos blockpos = pSource.getPos().relative(pSource.getBlockState().getValue(DispenserBlock.FACING));
                    DeadTNTEntity tntentity = new DeadTNTEntity(world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D, (LivingEntity)null);
                    world.addFreshEntity(tntentity);
                    world.playSound((PlayerEntity)null, tntentity.getX(), tntentity.getY(), tntentity.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pStack.shrink(1);
                    return pStack;
                }
            });
            DispenserBlock.registerBehavior(ModItems.FROST_CHARGE.get(), new DefaultDispenseItemBehavior() {

                public ItemStack execute(IBlockSource pSource, ItemStack pStack) {
                    Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
                    IPosition iposition = DispenserBlock.getDispensePosition(pSource);
                    double d0 = iposition.x() + (double)((float)direction.getStepX() * 0.3F);
                    double d1 = iposition.y() + (double)((float)direction.getStepY() * 0.3F);
                    double d2 = iposition.z() + (double)((float)direction.getStepZ() * 0.3F);
                    World world = pSource.getLevel();
                    Random random = world.random;
                    double d3 = random.nextGaussian() * 0.05D + (double)direction.getStepX();
                    double d4 = random.nextGaussian() * 0.05D + (double)direction.getStepY();
                    double d5 = random.nextGaussian() * 0.05D + (double)direction.getStepZ();
                    world.addFreshEntity(new FrostBallEntity(world, d0, d1, d2, d3, d4, d5));
                    pStack.shrink(1);
                    return pStack;
                }

                protected void playSound(IBlockSource pSource) {
                    pSource.getLevel().levelEvent(1018, pSource.getPos(), 0);
                }
            });
            AxeItem.STRIPABLES = Maps.newHashMap(AxeItem.STRIPABLES);
            AxeItem.STRIPABLES.put(ModBlocks.HAUNTED_LOG.get(), ModBlocks.STRIPPED_HAUNTED_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.HAUNTED_WOOD.get(), ModBlocks.STRIPPED_HAUNTED_WOOD.get());
            AxeItem.STRIPABLES.put(ModBlocks.GLOOM_LOG.get(), ModBlocks.STRIPPED_GLOOM_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.GLOOM_WOOD.get(), ModBlocks.STRIPPED_GLOOM_WOOD.get());
            AxeItem.STRIPABLES.put(ModBlocks.MURK_LOG.get(), ModBlocks.STRIPPED_MURK_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.MURK_WOOD.get(), ModBlocks.STRIPPED_MURK_WOOD.get());
            ComposterBlock.COMPOSTABLES.put(ModBlocks.MURK_SAPLING_ITEM.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(ModBlocks.GLOOM_SAPLING_ITEM.get(), 0.3F);
            WoodType.register(ModWoodType.HAUNTED);
            WoodType.register(ModWoodType.GLOOM);
            WoodType.register(ModWoodType.MURK);
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(ModPotionUtil.setSplashPotion(Potions.AWKWARD)), Ingredient.of(Items.FIRE_CHARGE), new ItemStack(ModItems.BURNING_POTION.get())));
            ConfiguredStructures.registerConfiguredStructures();
        });

    }

    public static void EntitySpawnPlacementRegistry() {
        EntitySpawnPlacementRegistry.register(ModEntityType.SACRED_FISH.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::checkFishSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityType.DREDEN.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OwnedEntity::checkFrostSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityType.WRAITH.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OwnedEntity::checkHostileSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityType.URBHADHACH.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OwnedEntity::checkFrostSpawnRules);
    }

    private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(ModEntityType.CHANNELLER.get(), ChannellerEntity.setCustomAttributes().build());
        event.put(ModEntityType.FANATIC.get(), FanaticEntity.setCustomAttributes().build());
        event.put(ModEntityType.ZEALOT.get(), ZealotEntity.setCustomAttributes().build());
        event.put(ModEntityType.THUG.get(), ThugEntity.setCustomAttributes().build());
        event.put(ModEntityType.CRIMSON_SPIDER.get(), CrimsonSpiderEntity.setCustomAttributes().build());
        event.put(ModEntityType.DISCIPLE.get(), DiscipleEntity.setCustomAttributes().build());
        event.put(ModEntityType.BELDAM.get(), BeldamEntity.setCustomAttributes().build());
        event.put(ModEntityType.OBSIDIAN_MONOLITH.get(), ObsidianMonolithEntity.setCustomAttributes().build());
        event.put(ModEntityType.APOSTLE.get(), ApostleEntity.setCustomAttributes().build());
        event.put(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.ZPIGLIN_MINION.get(), ZPiglinMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ZPiglinBruteMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.RETURNED.get(), ReturnedEntity.setCustomAttributes().build());
        event.put(ModEntityType.MALGHAST.get(), MalghastEntity.setCustomAttributes().build());
        event.put(ModEntityType.HOGLORD.get(), HogLordEntity.setCustomAttributes().build());
        event.put(ModEntityType.ENVIOKER.get(), EnviokerEntity.setCustomAttributes().build());
        event.put(ModEntityType.INQUILLAGER.get(), InquillagerEntity.setCustomAttributes().build());
        event.put(ModEntityType.CONQUILLAGER.get(), ConquillagerEntity.setCustomAttributes().build());
        event.put(ModEntityType.TORMENTOR.get(), TormentorEntity.setCustomAttributes().build());
        event.put(ModEntityType.HUSKARL.get(), HuskarlEntity.setCustomAttributes().build());
        event.put(ModEntityType.SHADE.get(), ShadeEntity.setCustomAttributes().build());
        event.put(ModEntityType.DREDEN.get(), DredenEntity.setCustomAttributes().build());
        event.put(ModEntityType.WRAITH.get(), WraithEntity.setCustomAttributes().build());
        event.put(ModEntityType.URBHADHACH.get(), UrbhadhachEntity.setCustomAttributes().build());
        event.put(ModEntityType.BOOMER.get(), BoomerEntity.setCustomAttributes().build());
        event.put(ModEntityType.DUNE_SPIDER.get(), DuneSpiderEntity.setCustomAttributes().build());
        event.put(ModEntityType.FALLEN.get(), FallenEntity.setCustomAttributes().build());
        event.put(ModEntityType.DESICCATED.get(), DesiccatedEntity.setCustomAttributes().build());
        event.put(ModEntityType.BLIGHT.get(), BlightEntity.setCustomAttributes().build());
        event.put(ModEntityType.BLIGHTLING.get(), BlightlingEntity.setCustomAttributes().build());
        event.put(ModEntityType.MARCIRE.get(), MarcireEntity.setCustomAttributes().build());
        event.put(ModEntityType.LOCUST.get(), LocustEntity.setCustomAttributes().build());
        event.put(ModEntityType.MUTATED_COW.get(), MutatedCowEntity.setCustomAttributes().build());
        event.put(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenEntity.setCustomAttributes().build());
        event.put(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepEntity.setCustomAttributes().build());
        event.put(ModEntityType.MUTATED_PIG.get(), MutatedPigEntity.setCustomAttributes().build());
        event.put(ModEntityType.MUTATED_RABBIT.get(), MutatedRabbitEntity.setCustomAttributes().build());
        event.put(ModEntityType.SACRED_FISH.get(), SacredFishEntity.setCustomAttributes().build());
        event.put(ModEntityType.PARASITE.get(), ParasiteEntity.setCustomAttributes().build());
        event.put(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexEntity.setCustomAttributes().build());
        event.put(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyVexEntity.setCustomAttributes().build());
        event.put(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.HUSK_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.DROWNED_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.SKELETON_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.STRAY_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.DREDEN_MINION.get(), DredenMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.WRAITH_MINION.get(), WraithMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.FARMER_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.UNDEAD_WOLF_MINION.get(), UndeadWolfEntity.setCustomAttributes().build());
        event.put(ModEntityType.PHANTOM_MINION.get(), PhantomMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.ILLUSION_CLONE.get(), IllusionCloneEntity.setCustomAttributes().build());
        event.put(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionEntity.setCustomAttributes().build());
        event.put(ModEntityType.FEL_FLY.get(), FelFlyEntity.setCustomAttributes().build());
        event.put(ModEntityType.ROT_TREE.get(), RottreantEntity.setCustomAttributes().build());
        event.put(ModEntityType.TAMED_SPIDER.get(), LoyalSpiderEntity.setCustomAttributes().build());
        event.put(ModEntityType.VIZIER.get(), VizierEntity.setCustomAttributes().build());
        event.put(ModEntityType.IRK.get(), IrkEntity.setCustomAttributes().build());
        event.put(ModEntityType.SCORCH.get(), ScorchEntity.setCustomAttributes().build());
        event.put(ModEntityType.SKULL_LORD.get(), SkullLordEntity.setCustomAttributes().build());
        event.put(ModEntityType.BONE_LORD.get(), BoneLordEntity.setCustomAttributes().build());
        event.put(ModEntityType.LASER.get(), LaserEntity.setCustomAttributes().build());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        if (event.getName() != null) {
            RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, event.getName());
            if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD)){
                if (MainConfig.DarkManorGen.get()) {
                    if (event.getCategory() == Biome.Category.FOREST
                            || event.getCategory() == Biome.Category.ICY
                            || event.getCategory() == Biome.Category.TAIGA) {
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_DARK_MANOR);
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
                if (MainConfig.RuinedRitualGen.get()){
                    if (event.getCategory() == Biome.Category.PLAINS
                            || event.getCategory() == Biome.Category.FOREST
                            || event.getCategory() == Biome.Category.TAIGA
                            || event.getCategory() == Biome.Category.SAVANNA){
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.RUINED_RITUAL_STANDARD);
                    }
                    if (event.getCategory() == Biome.Category.JUNGLE){
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.RUINED_RITUAL_JUNGLE);
                    }
                }
                if (MainConfig.PortalOutpostGen.get()) {
                    if (event.getCategory() != Biome.Category.NETHER
                            && event.getCategory() != Biome.Category.OCEAN
                            && event.getCategory() != Biome.Category.RIVER) {
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PORTAL_OUTPOST);
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
                if (MainConfig.GloomTreeGen.get()) {
                    if (event.getCategory() == Biome.Category.SWAMP) {
                        event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.GLOOM_TREE);
                    }
                }
                if (MainConfig.MurkTreeGen.get()) {
                    if (event.getCategory() == Biome.Category.FOREST
                            || event.getCategory() == Biome.Category.TAIGA
                            || event.getCategory() == Biome.Category.EXTREME_HILLS) {
                        event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.MURK_TREE);
                    }
                }
            } else if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.NETHER)){
                if (MainConfig.CrimsonShrineGen.get()) {
                    if (biomeRegistryKey == Biomes.CRIMSON_FOREST) {
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_CRIMSON_SHRINE);
                    }
                }
                if (MainConfig.WarpedShrineGen.get()) {
                    if (biomeRegistryKey == Biomes.WARPED_FOREST) {
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_WARPED_SHRINE);
                    }
                }
                if (MainConfig.ValleyShrineGen.get()) {
                    if (biomeRegistryKey == Biomes.SOUL_SAND_VALLEY) {
                        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_VALLEY_SHRINE);
                    }
                }
            }
        }
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
            tempMap.putIfAbsent(ModStructures.CRIMSON_SHRINE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.CRIMSON_SHRINE.get()));
            tempMap.putIfAbsent(ModStructures.WARPED_SHRINE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.WARPED_SHRINE.get()));
            tempMap.putIfAbsent(ModStructures.VALLEY_SHRINE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.VALLEY_SHRINE.get()));
            tempMap.putIfAbsent(ModStructures.DARK_MANOR.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.DARK_MANOR.get()));
            tempMap.putIfAbsent(ModStructures.PORTAL_OUTPOST.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.PORTAL_OUTPOST.get()));
            tempMap.putIfAbsent(ModStructures.CURSED_GRAVEYARD.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.CURSED_GRAVEYARD.get()));
            tempMap.putIfAbsent(ModStructures.SALVAGED_FORT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.SALVAGED_FORT.get()));
            tempMap.putIfAbsent(ModStructures.DECREPIT_FORT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.DECREPIT_FORT.get()));
            tempMap.putIfAbsent(ModStructures.RUINED_RITUAL.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.RUINED_RITUAL.get()));

            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    public static final ItemGroup TAB = new ItemGroup("goetyTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GOLDTOTEM.get());
        }
    };
}
