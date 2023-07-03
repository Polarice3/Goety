package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.client.armors.ModArmorMaterial;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.capabilities.lichdom.ILichdom;
import com.Polarice3.Goety.common.capabilities.lichdom.LichProvider;
import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEProvider;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsProvider;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ai.TargetHostileOwnedGoal;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.hostile.BoneLordEntity;
import com.Polarice3.Goety.common.entities.hostile.DredenEntity;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.BeldamEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.ChannellerEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.ICultist;
import com.Polarice3.Goety.common.entities.hostile.dead.FallenEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.HuntingIllagerEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.entities.utilities.StormEntity;
import com.Polarice3.Goety.common.items.GrandTorchItem;
import com.Polarice3.Goety.common.items.ModItemTiers;
import com.Polarice3.Goety.common.items.curios.GraveGloveItem;
import com.Polarice3.Goety.common.items.equipment.*;
import com.Polarice3.Goety.compat.patchouli.PatchouliLoaded;
import com.Polarice3.Goety.init.*;
import com.Polarice3.Goety.utils.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.GossipType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerCapabilityAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Goety.MOD_ID, "lichdom"), new LichProvider());
            event.addCapability(new ResourceLocation(Goety.MOD_ID, "soulenergy"), new SEProvider());
        }
        if (event.getObject() instanceof LoyalSpiderEntity){
            event.addCapability(new ResourceLocation(Goety.MOD_ID, "spiderlevels"), new SpiderLevelsProvider());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World world = event.getWorld();
        if (entity instanceof LivingEntity && !event.getWorld().isClientSide()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                LichdomHelper.sendLichUpdatePacket(player);
                SEHelper.sendSEUpdatePacket(player);
                CompoundNBT playerData = player.getPersistentData();
                CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                if (data.getBoolean("goety:isLich")){
                    ILichdom lichdom = LichdomHelper.getCapability(player);
                    lichdom.setLichdom(true);
                    LichdomHelper.sendLichUpdatePacket(player);
                }
            }
            if (entity instanceof LoyalSpiderEntity){
                LoyalSpiderEntity loyalSpiderEntity = (LoyalSpiderEntity) entity;
                if (loyalSpiderEntity.getPlayer() != null) {
                    SpiderLevelsHelper.sendSpiderLevelsUpdatePacket(loyalSpiderEntity.getPlayer(), loyalSpiderEntity);
                }
            }

        }
        if (entity instanceof AbstractSkeletonEntity){
            AbstractSkeletonEntity skeletonEntity = (AbstractSkeletonEntity) entity;
            skeletonEntity.goalSelector.addGoal(4, new AvoidEntityGoal<>(skeletonEntity, UndeadWolfEntity.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof GolemEntity && !(entity instanceof IMob)){
            GolemEntity golemEntity = (GolemEntity) entity;
            golemEntity.targetSelector.addGoal(3, new TargetHostileOwnedGoal<>(golemEntity, OwnedEntity.class));
        }
        if (entity instanceof StormEntity){
            if (!entity.level.isClientSide){
                ServerWorld serverWorld = (ServerWorld) entity.level;
                serverWorld.setWeatherParameters(0, 6000, true, true);
            }
        }
        if (entity instanceof AbstractRaiderEntity){
            AbstractRaiderEntity raider = (AbstractRaiderEntity) entity;
            if (world instanceof ServerWorld) {
                IServerWorld serverWorld = (IServerWorld) world;
                if (raider.hasActiveRaid()) {
                    Raid raid = raider.getCurrentRaid();
                    if (raid != null && raid.isActive() && !raid.isBetweenWaves() && !raid.isOver() && !raid.isStopped()) {
                        PlayerEntity player = EntityFinder.getNearbyPlayer(world, raid.getCenter());
                        if (player != null) {
                            if (MainConfig.CultistRaid.get()) {
                                if (raider instanceof WitchEntity) {
                                    int badOmen = MathHelper.clamp(raid.getBadOmenLevel(), 0, 5) + 1;
                                    for (int k1 = 0; k1 < badOmen; ++k1) {
                                        AbstractCultistEntity cultist;
                                        if (world.random.nextFloat() > 0.25F) {
                                            cultist = ModEntityType.FANATIC.get().create(world);
                                        } else {
                                            cultist = ModEntityType.ZEALOT.get().create(world);
                                        }
                                        if (cultist != null) {
                                            cultist.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                            cultist.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                            serverWorld.addFreshEntity(cultist);
                                        }
                                    }
                                }
                            }
                            if (MainConfig.IllagerRaid.get()) {
                                if (SEHelper.getSoulAmountInt(player) >= (MainConfig.IllagerAssaultSEThreshold.get() * 2)) {
                                    int badOmen = MathHelper.clamp(raid.getBadOmenLevel(), 0, 5) + 1;
                                    int pillager = world.random.nextInt((int) 12 / badOmen);
                                    if (pillager == 0) {
                                        if (raider.getType() == EntityType.PILLAGER) {
                                            HuntingIllagerEntity illager;
                                            if (world.random.nextBoolean()){
                                                illager = ModEntityType.CONQUILLAGER.get().create(world);
                                            } else {
                                                illager = raider.convertTo(ModEntityType.CONQUILLAGER.get(), false);
                                            }
                                            if (illager != null) {
                                                if (world.random.nextInt(4) == 0) {
                                                    illager.setRider(true);
                                                }
                                                illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                                illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                                serverWorld.addFreshEntity(illager);
                                            }
                                        }
                                        if (raider.getType() == EntityType.EVOKER) {
                                            EnviokerEntity illager = ModEntityType.ENVIOKER.get().create(world);
                                            if (illager != null) {
                                                illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                                illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                                serverWorld.addFreshEntity(illager);
                                            }
                                        }
                                    }
                                    int vindicator = world.random.nextInt((int) 12 / badOmen);
                                    if (vindicator == 0) {
                                        if (raid.getGroupsSpawned() > 3) {
                                            if (raider.getType() == EntityType.VINDICATOR) {
                                                HuntingIllagerEntity illager;
                                                if (world.random.nextBoolean()){
                                                    illager = ModEntityType.INQUILLAGER.get().create(world);
                                                } else {
                                                    illager = raider.convertTo(ModEntityType.INQUILLAGER.get(), false);
                                                }
                                                if (illager != null) {
                                                    illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                                    if (world.random.nextInt(4) == 0) {
                                                        illager.setRider(true);
                                                    }
                                                    illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                                    serverWorld.addFreshEntity(illager);
                                                }
                                            }
                                        }
                                        if (raider.getType() == EntityType.RAVAGER) {
                                            EnviokerEntity envioker = ModEntityType.ENVIOKER.get().create(world);
                                            if (envioker != null) {
                                                if (world.random.nextInt(4) == 0) {
                                                    envioker.setRider(true);
                                                }
                                                envioker.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                                envioker.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                                serverWorld.addFreshEntity(envioker);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();

        ILichdom capability2 = event.getOriginal().getCapability(LichProvider.CAPABILITY).resolve().get();

        player.getCapability(LichProvider.CAPABILITY)
                .ifPresent(lichdom ->
                        lichdom.setLichdom(capability2.getLichdom()));

        ISoulEnergy capability3 = event.getOriginal().getCapability(SEProvider.CAPABILITY).resolve().get();

        player.getCapability(SEProvider.CAPABILITY)
                .ifPresent(soulEnergy ->
                        soulEnergy.setSEActive(capability3.getSEActive()));
        player.getCapability(SEProvider.CAPABILITY)
                .ifPresent(soulEnergy ->
                        soulEnergy.setSoulEnergy(capability3.getSoulEnergy()));
        player.getCapability(SEProvider.CAPABILITY)
                .ifPresent(soulEnergy ->
                        soulEnergy.setArcaBlock(capability3.getArcaBlock()));
        player.getCapability(SEProvider.CAPABILITY)
                .ifPresent(soulEnergy ->
                        soulEnergy.setArcaBlockDimension(capability3.getArcaBlockDimension()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void naturalSpawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, event.getName());
            if (biome != null) {
                if (MainConfig.GoldenKingSpawn.get()) {
                    if (biome.getBiomeCategory() == Biome.Category.OCEAN) {
                        event.getSpawns().getSpawner(EntityClassification.WATER_AMBIENT).add(new MobSpawnInfo.Spawners(ModEntityType.SACRED_FISH.get(), 1, 1, 1));
                    }
                }
                boolean flag = false;
                boolean flag2 = false;
                if (MainConfig.InterDimensionalMobs.get()){
                    flag = true;
                    flag2 = true;
                } else {
                    if (BlockFinder.biomeIsInOverworld(event.getName())) {
                        flag = true;
                    }
                    if (BlockFinder.biomeIsInVanillaDim(event.getName())){
                        flag2 = true;
                    }
                }
                if (flag){
                    if (biome.getPrecipitation() == Biome.RainType.SNOW) {
                        if (MainConfig.DredenSpawnWeight.get() > 0) {
                            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                                    .add(new MobSpawnInfo.Spawners(ModEntityType.DREDEN.get(),
                                            MainConfig.DredenSpawnWeight.get(), 1, 1));
                        }
                        if (MainConfig.UrbhadhachSpawnWeight.get() > 0) {
                            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                                    .add(new MobSpawnInfo.Spawners(ModEntityType.URBHADHACH.get(),
                                            MainConfig.UrbhadhachSpawnWeight.get(), 1, 1));
                        }
                    }
                }
                if (flag2){
                    if (biome.getBiomeCategory() != Biome.Category.MUSHROOM && biome.getBiomeCategory() != Biome.Category.NONE
                            && biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NETHER){
                        if (MainConfig.WraithSpawnWeight.get() > 0){
                            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                                    .add(new MobSpawnInfo.Spawners(ModEntityType.WRAITH.get(),
                                            MainConfig.WraithSpawnWeight.get(), 1, 1));
                        }
                    } else if (biomeRegistryKey == Biomes.SOUL_SAND_VALLEY){
                        if (MainConfig.WraithSpawnWeight.get() > 0){
                            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                                    .add(new MobSpawnInfo.Spawners(ModEntityType.WRAITH.get(),
                                            2, 1, 1));
                            event.getSpawns().addMobCharge(ModEntityType.WRAITH.get(), 0.7D, 0.15D);
                        }
                    }
                }
            }
        }
    }

    private static final Map<ServerWorld, IllagerSpawner> ILLAGER_SPAWNER_MAP = new HashMap<>();
    private static final Map<ServerWorld, PilgrimSpawner> PILGRIM_SPAWNER_MAP = new HashMap<>();
    private static final Map<ServerWorld, EffectsEvent> EFFECTS_EVENT_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            ILLAGER_SPAWNER_MAP.put(serverWorld, new IllagerSpawner());
            PILGRIM_SPAWNER_MAP.put(serverWorld, new PilgrimSpawner());
            EFFECTS_EVENT_MAP.put(serverWorld, new EffectsEvent());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            ILLAGER_SPAWNER_MAP.remove(serverWorld);
            PILGRIM_SPAWNER_MAP.remove(serverWorld);
            EFFECTS_EVENT_MAP.remove(serverWorld);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            IllagerSpawner illagerSpawner = ILLAGER_SPAWNER_MAP.get(serverWorld);
            PilgrimSpawner pilgrimSpawner = PILGRIM_SPAWNER_MAP.get(serverWorld);
            EffectsEvent effectsEvent = EFFECTS_EVENT_MAP.get(serverWorld);
            if (illagerSpawner != null){
                illagerSpawner.tick(serverWorld);
            }
            if (pilgrimSpawner != null){
                pilgrimSpawner.tick(serverWorld);
            }
            if (effectsEvent != null){
                effectsEvent.tick(serverWorld);
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerFirstEntersWorld(PlayerEvent.PlayerLoggedInEvent event){
        CompoundNBT playerData = event.getPlayer().getPersistentData();
        CompoundNBT data;

        if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            data = new CompoundNBT();
        } else {
            data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }
        if (!event.getPlayer().level.isClientSide) {
            if (MainConfig.StarterTotem.get()) {
                if (!data.getBoolean("goety:gotTotem")) {
                    event.getPlayer().addItem(new ItemStack(ModItems.GOLDTOTEM.get()));
                    data.putBoolean("goety:gotTotem", true);
                    playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                }
            }
            if (PatchouliLoaded.PATCHOULI.isLoaded()){
                if (MainConfig.StarterBook.get()){
                    if (!data.getBoolean("goety:starterBook")) {
                        ItemStack book = PatchouliAPI.get().getBookStack(Goety.location("black_book"));
                        event.getPlayer().addItem(book);
                        data.putBoolean("goety:starterBook", true);
                        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void SpecialSpawnEvents(LivingSpawnEvent.CheckSpawn event){
        BlockPos blockPos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if (event.getEntityLiving() instanceof AbstractCultistEntity){
            boolean spawnRule = AbstractCultistEntity.spawnCultistsRules(event.getEntityLiving().getType(), event.getWorld(), event.getSpawnReason(), blockPos, event.getWorld().getRandom());
            if (spawnRule){
                event.setResult(Event.Result.ALLOW);
            } else {
                if (event.getSpawnReason() == SpawnReason.NATURAL){
                    MobUtil.moveDownToGround(event.getEntityLiving());
                    boolean spawn = false;
                    if (!event.getWorld().getFluidState(event.getEntityLiving().blockPosition().below()).isEmpty()){
                        event.setResult(Event.Result.DENY);
                    } else if (event.getWorld().dimensionType().hasCeiling()){
                        if (event.getWorld().getRandom().nextFloat() <= 0.15F) {
                            spawn = true;
                        } else {
                            event.setResult(Event.Result.DENY);
                        }
                    } else {
                        spawn = true;
                    }
                    if (spawn){
                        if (event.getEntityLiving() instanceof ChannellerEntity) {
                            if (event.getWorld().getRandom().nextFloat() <= 0.25F) {
                                event.setResult(Event.Result.ALLOW);
                            } else {
                                event.setResult(Event.Result.DENY);
                            }
                        } else {
                            event.setResult(Event.Result.ALLOW);
                        }
                    }
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
        if (event.getEntityLiving() instanceof SpellcastingIllagerEntity || event.getEntityLiving() instanceof WitchEntity){
            if (event.getSpawnReason() == SpawnReason.STRUCTURE){
                event.getEntityLiving().addTag(ConstantPaths.structureMob());
            }
        }
        if (event.getEntityLiving() instanceof SacredFishEntity){
            if (event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION){
                if (event.getWorld().getRandom().nextFloat() <= 0.025F){
                    event.setResult(Event.Result.ALLOW);
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void ZombieEvents(ZombieEvent.SummonAidEvent event){
        if (event.getSummoner() instanceof FallenEntity){
            event.setCustomSummonedAid(ModEntityType.FALLEN.get().create(event.getWorld()));
        }
        if (event.getSummoner() instanceof HuskarlEntity){
            event.setCustomSummonedAid(ModEntityType.HUSKARL.get().create(event.getWorld()));
        }
    }

    @SubscribeEvent
    public static void LivingEffects(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity != null){
            if (livingEntity.getMobType() == CreatureAttribute.UNDEAD){
                if (BlockFinder.isDeadBlock(livingEntity.level, livingEntity.blockPosition())){
                    livingEntity.clearFire();
                }
            }
            if (livingEntity.hasEffect(ModEffects.BURN_HEX.get())){
                if (livingEntity.hasEffect(Effects.FIRE_RESISTANCE)){
                    livingEntity.removeEffectNoUpdate(Effects.FIRE_RESISTANCE);
                }
            }
            if (livingEntity.hasEffect(ModEffects.NECROPOWER.get())){
                livingEntity.clearFire();
            }
            if (livingEntity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (RobeArmorFinder.FindBootsofWander(player)){
                    PlayerUtil.enableStepHeight(player);
                } else {
                    PlayerUtil.disableStepHeight(player);
                }
                if (player.getMainHandItem().getItem() instanceof WarpedSpearItem && player.getOffhandItem().isEmpty()){
                    PlayerUtil.enableSpearReach(player);
                } else {
                    PlayerUtil.disableSpearReach(player);
                }
            }
            if (livingEntity instanceof PiglinEntity){
                if (!livingEntity.level.isClientSide) {
                    PiglinEntity piglinEntity = (PiglinEntity) livingEntity;
                    Brain<?> brain = piglinEntity.getBrain();
                    Optional<LivingEntity> avoidUndead = Optional.empty();
                    for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())) {
                        if (livingentity instanceof ZPiglinMinionEntity) {
                            avoidUndead = Optional.of(livingentity);
                        }
                    }
                    if (avoidUndead.isPresent()) {
                        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, avoidUndead);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.hasEffect(ModEffects.NOMINE.get())){
            if (BlockFinder.NoBreak(event.getState()) && !(event.getState().getBlock() == ModBlocks.GUARDIAN_OBELISK.get())){
                if (!player.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) player.level;
                    ServerParticleUtil.blockBreakParticles(ParticleTypes.HAPPY_VILLAGER, event.getPos(), event.getState(), serverWorld);
                }
                player.playSound(SoundEvents.ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                event.setCanceled(true);
            }
        }
        if (player.getMainHandItem().getItem() instanceof PhilosophersMaceItem){
            if (event.getState().getBlock().getDescriptionId().contains("nether_gold")){
                if (!player.level.isClientSide) {
                    if (player.level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !player.level.restoringBlockSnapshots) {
                        ItemStack itemStack = new ItemStack(event.getState().getBlock().asItem());
                        double d0 = (double) (player.level.random.nextFloat() * 0.5F) + 0.25D;
                        double d1 = (double) (player.level.random.nextFloat() * 0.5F) + 0.25D;
                        double d2 = (double) (player.level.random.nextFloat() * 0.5F) + 0.25D;
                        ItemEntity itementity = new ItemEntity(player.level, (double) event.getPos().getX() + d0, (double) event.getPos().getY() + d1, (double) event.getPos().getZ() + d2, itemStack);
                        itementity.setDefaultPickUpDelay();
                        player.level.addFreshEntity(itementity);
                    }
                    player.level.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlacingBlock(BlockEvent.EntityPlaceEvent event){
    }

    @SubscribeEvent
    public static void onConversion(LivingConversionEvent.Post event){
        if (event.getOutcome() instanceof BeldamEntity){
            if (!event.getOutcome().level.isClientSide){
                ServerWorld serverWorld = (ServerWorld) event.getOutcome().level;
                for (int i = 0; i < 5; ++i) {
                    double d0 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    double d1 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    double d2 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, event.getOutcome().getRandomX(1.0D), event.getOutcome().getRandomY() + 1.0D, event.getOutcome().getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                }
            }
            event.getOutcome().playSound(SoundEvents.WITCH_CELEBRATE, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        World world = player.level;
        int zombies = 0;
        int skeletons = 0;
        int wolves = 0;
        if (world instanceof ServerWorld){
            for (Entity entity : ((ServerWorld) world).getAllEntities()){
                if (entity instanceof OwnedEntity){
                    OwnedEntity summonedEntity = (OwnedEntity) entity;
                    if (summonedEntity.getTrueOwner() == player && summonedEntity.isAlive()){
                        if (summonedEntity instanceof ZombieMinionEntity || summonedEntity instanceof ZPiglinMinionEntity){
                            ++zombies;
                            if (zombies > SpellConfig.ZombieLimit.get()){
                                if (summonedEntity.tickCount % 20 == 0){
                                    summonedEntity.hurt(DamageSource.STARVE, summonedEntity.getMaxHealth()/4);
                                }
                            }
                        }
                        if (summonedEntity instanceof AbstractSMEntity){
                            ++skeletons;
                            if (skeletons > SpellConfig.SkeletonLimit.get()){
                                if (summonedEntity.tickCount % 20 == 0){
                                    summonedEntity.hurt(DamageSource.STARVE, summonedEntity.getMaxHealth()/4);
                                }
                            }
                        }
                        if (summonedEntity instanceof UndeadWolfEntity){
                            ++wolves;
                            if (wolves > SpellConfig.UndeadWolfLimit.get()){
                                if (summonedEntity.tickCount % 20 == 0){
                                    summonedEntity.hurt(DamageSource.STARVE, summonedEntity.getMaxHealth()/4);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (MainConfig.VillagerHate.get()) {
            if (RobeArmorFinder.FindAnySet(player)) {
                for (VillagerEntity villager : player.level.getEntitiesOfClass(VillagerEntity.class, player.getBoundingBox().inflate(16.0D))) {
                    if (villager.getPlayerReputation(player) > -25 && villager.getPlayerReputation(player) < 100) {
                        if (player.tickCount % 20 == 0) {
                            villager.getGossips().add(player.getUUID(), GossipType.MINOR_NEGATIVE, 25);
                        }
                    }
                }
            }
        }
        if (RobeArmorFinder.FindBootsofWander(player)){
            FluidState fluidstate = player.level.getFluidState(player.blockPosition());
            if (player.isInWater() && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate.getType()) && !player.hasEffect(Effects.DOLPHINS_GRACE)){
                player.setDeltaMovement(player.getDeltaMovement().x * 1.0175, player.getDeltaMovement().y, player.getDeltaMovement().z * 1.0175);
            }
        }
        if (RobeArmorFinder.FindNecroBootsofWander(player)){
            BlockPos blockPos = new BlockPos(player.getX(), player.getBoundingBox().minY - 0.5000001D, player.getZ());
            BlockState blockState = player.level.getBlockState(blockPos);
            if (blockState.is(BlockTags.SOUL_SPEED_BLOCKS) && !player.isSpectator()) {
                if (blockState.getBlock().getSpeedFactor() <= 0.4F && !EnchantmentHelper.hasSoulSpeed(player)) {
                    player.setDeltaMovement(player.getDeltaMovement().multiply(1.5F, 1.0F, 1.5F));
                }
            }
        }
        if (RobeArmorFinder.FindNecroSet(player)) {
            BlockState blockState = player.level.getBlockState(player.blockPosition().below());
            if (!LichdomHelper.isLich(player)) {
                if (!(blockState.getBlock() instanceof IDeadBlock)) {
                    if (!world.isClientSide) {
                        boolean flag = false;
                        if (world.isDay() && !world.isRaining() && !world.isThundering()){
                            BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                            if (world.canSeeSky(blockpos)) {
                                player.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
                                player.addEffect(new EffectInstance(Effects.HUNGER, 20, 1));
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }
                        if (flag){
                            if (player.tickCount % 50 == 0){
                                if (player.getHealth() < player.getMaxHealth()) {
                                    player.heal(1.0F);
                                }
                            }
                            player.addEffect(new EffectInstance(Effects.REGENERATION, 20, 0, false, false));
                        }
                    }
                } else {
                    if (player.tickCount % 50 == 0){
                        if (player.getHealth() < player.getMaxHealth()) {
                            player.heal(1.0F);
                        }
                    }
                    player.addEffect(new EffectInstance(Effects.REGENERATION, 20, 0, false, false));
                    if (player.hasEffect(Effects.WEAKNESS)) {
                        player.removeEffect(Effects.WEAKNESS);
                    }
                    if (player.hasEffect(Effects.HUNGER)) {
                        player.removeEffect(Effects.HUNGER);
                    }
                }
            }
        }
        if (RobeArmorFinder.FindFelHelm(player)) {
            if (!LichdomHelper.isLich(player)) {
                player.addEffect(new EffectInstance(ModEffects.FEL_VISION.get(), 100, 0, false, false, false));
            }
        } else {
            if (player.hasEffect(ModEffects.FEL_VISION.get())){
                player.removeEffect(ModEffects.FEL_VISION.get());
            }
        }
        if (RobeArmorFinder.FindFelHelm(player) && RobeArmorFinder.FindLeggings(player)){
            BlockFinder.WebMovement(player);
        }
        if (RobeArmorFinder.FindFelArmor(player)){
            BlockFinder.BushMovement(player);
        }
        if (SEHelper.getSoulAmountInt(player) > MainConfig.IllagerAssaultSEThreshold.get() * 2){
            for (AbstractRaiderEntity pillagerEntity : player.level.getEntitiesOfClass(AbstractRaiderEntity.class, player.getBoundingBox().inflate(32))){
                if (pillagerEntity.getTarget() == player) {
                    if (!pillagerEntity.isAggressive()) {
                        pillagerEntity.setAggressive(true);
                    }
                }
            }
        }

        ModifiableAttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        boolean scythe = player.getMainHandItem().getItem() instanceof DarkScytheItem;

        float increaseAttackSpeed0 = 0.25F;
        AttributeModifier attributemodifier0 = new AttributeModifier(UUID.fromString("0c091f42-8c6d-4fde-96e9-148115731cbf"), "Two Handed Scythe", increaseAttackSpeed0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        boolean flag0 = scythe && player.getOffhandItem().isEmpty();
        if (attackSpeed != null){
            if (flag0){
                if (!attackSpeed.hasModifier(attributemodifier0)){
                    attackSpeed.addPermanentModifier(attributemodifier0);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier0)){
                    attackSpeed.removeModifier(attributemodifier0);
                }
            }
        }

        float increaseAttackSpeed = 0.5F;
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString("d4818bbc-54ed-4ecf-95a3-a15fbf71b31d"), "Scythe Proficiency", increaseAttackSpeed, AttributeModifier.Operation.MULTIPLY_TOTAL);
        boolean flag = CuriosFinder.findGlove(player).getItem() instanceof GraveGloveItem && scythe;
        if (attackSpeed != null){
            if (flag){
                if (!attackSpeed.hasModifier(attributemodifier)){
                    attackSpeed.addPermanentModifier(attributemodifier);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier)){
                    attackSpeed.removeModifier(attributemodifier);
                }
            }
        }
        if (PlayerUtil.starAmuletActive(player)){
            player.abilities.flying &= player.isCreative();
        }
    }

    @SubscribeEvent
    public static void OnLivingFall(LivingFallEvent event){
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (RobeArmorFinder.FindNecroBootsofWander(player) || RobeArmorFinder.FindBootsofWander(player)){
                event.setDistance(event.getDistance()/2);
            }
            if (RobeArmorFinder.FindFelBootsofWander(player)){
                Vector3d vector3d = player.getDeltaMovement();
                if (!player.isShiftKeyDown() && !player.isCrouching()) {
                    if (event.getDistance() >= 2.0F) {
                        double jump = MathHelper.sqrt(event.getDistance())/2.0D;
                        player.setDeltaMovement(vector3d.x, jump, vector3d.z);
                        player.hurtMarked = true;
                        player.playSound(SoundEvents.SLIME_BLOCK_FALL, 0.5F, 1.0F);
                        if (!player.level.isClientSide()) {
                            player.hasImpulse = true;
                            event.setCanceled(true);
                            player.setOnGround(false);
                        }
                    }
                }

                if (event.getDamageMultiplier() != 0){
                    event.setDamageMultiplier(0);
                    if (event.getDistance() >= 2.0F) {
                        player.getItemBySlot(EquipmentSlotType.FEET).hurtAndBreak((int) (event.getDistance()/2), player, (p_233653_0_) -> {
                            p_233653_0_.broadcastBreakEvent(EquipmentSlotType.FEET);
                        });
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void OnLivingJump(LivingEvent.LivingJumpEvent event){
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (RobeArmorFinder.FindBootsofWander(player)){
                float f = 0.625F;
                if (player.hasEffect(Effects.JUMP)){
                    f += 0.1F * (float)(Objects.requireNonNull(player.getEffect(Effects.JUMP)).getAmplifier() + 1);
                }
                Vector3d vector3d = player.getDeltaMovement();
                player.setDeltaMovement(vector3d.x, f, vector3d.z);
            }
        }

    }

    @SubscribeEvent
    public static void TargetSelection(LivingSetAttackTargetEvent event){
        LivingEntity attacker = event.getEntityLiving();
        LivingEntity target = event.getTarget();
        if (attacker instanceof MobEntity){
            MobEntity mobAttacker = (MobEntity) attacker;
            if (target != null){
                if (target instanceof PlayerEntity){
                    if (mobAttacker.getLastHurtByMob() instanceof IOwned && !(mobAttacker instanceof ApostleEntity)){
                        mobAttacker.setTarget(mobAttacker.getLastHurtByMob());
                    }
                    if (RobeArmorFinder.FindNecroSet(target) && RobeArmorFinder.FindNecroBootsofWander(target)){
                        boolean undead = mobAttacker.getMobType() == CreatureAttribute.UNDEAD && mobAttacker.getMaxHealth() < 50.0F && !(mobAttacker instanceof OwnedEntity && !(mobAttacker instanceof IMob)) && !(mobAttacker instanceof BoneLordEntity);
                        if (undead){
                            if (mobAttacker.getLastHurtByMob() != target){
                                mobAttacker.setTarget(null);
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                }
                if (mobAttacker.getTags().contains(ConstantPaths.skullLordMinion())){
                    if (target instanceof SkullLordEntity || target instanceof BoneLordEntity){
                        mobAttacker.setTarget(null);
                    }
                }
                if ((mobAttacker.getMobType() == CreatureAttribute.UNDEAD && !(mobAttacker instanceof IOwned) && mobAttacker.getMaxHealth() < 100.0F) || mobAttacker instanceof CreeperEntity){
                    if (target instanceof ApostleEntity){
                        mobAttacker.setTarget(null);
                    }
                }
                if (mobAttacker.getMobType() == CreatureAttribute.ARTHROPOD){
                    if (RobeArmorFinder.FindFelHelm(target)){
                        if (mobAttacker.getLastHurtByMob() != target){
                            mobAttacker.setTarget(null);
                        } else {
                            mobAttacker.setLastHurtByMob(target);
                        }
                    }
                }
                if (mobAttacker instanceof CreeperEntity){
                    if (RobeArmorFinder.FindFelArmor(target)){
                        if (mobAttacker.getLastHurtByMob() != target){
                            mobAttacker.setTarget(null);
                        } else {
                            mobAttacker.setLastHurtByMob(target);
                        }
                    }
                }
                if (mobAttacker instanceof SlimeEntity){
                    if (RobeArmorFinder.FindFelBootsofWander(target)){
                        if (mobAttacker.getLastHurtByMob() != target){
                            mobAttacker.setTarget(null);
                        } else {
                            mobAttacker.setLastHurtByMob(target);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        if (RobeArmorFinder.FindFelArmor(victim)){
            if (event.getSource().isExplosion()){
                event.setAmount((float) (event.getAmount() / 1.5F));
            }
            if (event.getSource().isFire()){
                event.setAmount((float) (event.getAmount() * 1.5F));
            }
        }
        for (EquipmentSlotType equipmentSlotType: EquipmentSlotType.values()){
            if (equipmentSlotType.getType() == EquipmentSlotType.Group.ARMOR){
                ItemStack itemStack = victim.getItemBySlot(equipmentSlotType);
                if (itemStack.getItem() instanceof ArmorItem){
                    ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                    if (armorItem.getMaterial() == ModArmorMaterial.FROST){
                        if (ModDamageSource.frostAttacks(event.getSource())){
                            float damage = 1.0F;
                            switch (equipmentSlotType){
                                case HEAD:
                                case FEET:
                                    damage -= 0.1F;
                                    break;
                                case CHEST:
                                    damage -= 0.5F;
                                    break;
                                case LEGS:
                                    damage -= 0.3F;
                                    break;
                            }
                            event.setAmount(event.getAmount() * damage);
                        }
                        if (event.getSource().isFire()) {
                            float damage = 1.0F;
                            switch (equipmentSlotType){
                                case HEAD:
                                case LEGS:
                                case FEET:
                                    damage -= 0.1F;
                                    break;
                                case CHEST:
                                    damage -= 0.2F;
                                    break;
                            }
                            event.setAmount(event.getAmount() * damage);
                            float damage2 = event.getAmount() / 4.0F;
                            if (damage2 < 1.0F) {
                                damage2 = 1.0F;
                            }
                            ItemHelper.hurtAndBreak(itemStack, (int) damage2, victim);
                        }
                    }
                }
            }
        }
        if (victim.level.getDifficulty() == Difficulty.HARD){
            if (victim.hasEffect(ModEffects.BURN_HEX.get())){
                EffectInstance effectInstance = victim.getEffect(ModEffects.BURN_HEX.get());
                int i = 2;
                if (effectInstance != null) {
                    i = effectInstance.getAmplifier() + 2;
                }
                if (event.getSource().isFire()){
                    event.setAmount(event.getAmount() * i);
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof LivingEntity){
            LivingEntity livingAttacker = (LivingEntity) event.getSource().getDirectEntity();
            if (ModDamageSource.physicalAttacks(event.getSource())) {
                if (livingAttacker.getMainHandItem().getItem() instanceof TieredItem) {
                    TieredItem weapon = (TieredItem) livingAttacker.getMainHandItem().getItem();
                    if (weapon.getTier() == ModItemTiers.FROST) {
                        if (MobUtil.immuneToFrost(victim)) {
                            event.setAmount(event.getAmount() / 2.0F);
                        } else {
                            int i = 0;
                            if (weapon instanceof FrostScytheItem) {
                                i = 1;
                            }
                            victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, i));
                        }
                        if (MobUtil.extraFrostDamage(victim)) {
                            event.setAmount(event.getAmount() * 2.0F);
                        }
                    }
                    if (weapon instanceof DarkScytheItem){
                        victim.playSound(ModSounds.SCYTHE_HIT_MEATY.get(), 1.0F, 1.0F);
                    }
                    if (weapon instanceof DeathScytheItem) {
                        if (!victim.hasEffect(ModEffects.SAPPED.get())) {
                            victim.addEffect(new EffectInstance(ModEffects.SAPPED.get(), 20));
                            victim.playSound(SoundEvents.SHIELD_BREAK, 2.0F, 1.0F);
                        } else {
                            if (victim.level.random.nextFloat() <= 0.1F) {
                                EffectsUtil.amplifyEffect(victim, ModEffects.SAPPED.get(), 20);
                                victim.playSound(SoundEvents.SHIELD_BREAK, 2.0F, 1.0F);
                            } else {
                                EffectsUtil.resetDuration(victim, ModEffects.SAPPED.get(), 20);
                            }
                        }
                    }
                }
            }
        }
        if (ModDamageSource.frostAttacks(event.getSource())){
            if (MobUtil.extraFrostDamage(victim)){
                event.setAmount(event.getAmount() * 2);
            }
        }
    }

    @SubscribeEvent
    public static void AttackEvent(LivingAttackEvent event){
        LivingEntity victim = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (RobeArmorFinder.FindFelBootsofWander(victim)){
            if (attacker instanceof SlimeEntity){
                if (((SlimeEntity) attacker).getTarget() != victim){
                    event.setCanceled(true);
                }
            }
        }
        if (ModDamageSource.frostAttacks(event.getSource())){
            if (MobUtil.immuneToFrost(victim)){
                event.setCanceled(true);
            }
        }
        if (RobeArmorFinder.FindFelArmor(victim)){
            if (event.getSource() == DamageSource.SWEET_BERRY_BUSH){
                event.setCanceled(true);
            }
        }
        if (SpellConfig.MinionsMasterImmune.get()){
            if (attacker instanceof IOwned){
                if (((IOwned) attacker).getTrueOwner() == victim){
                    event.setCanceled(true);
                }
            }
        }
        if (SpellConfig.OwnerAttackCancel.get()){
            if (attacker != null) {
                if (victim instanceof IOwned) {
                    if (((IOwned) victim).getTrueOwner() == attacker) {
                        event.setCanceled(true);
                    }
                }
            }
        }
        if (direct instanceof LivingEntity && ModDamageSource.physicalAttacks(event.getSource())){
            float chance = 0.0F;
            LivingEntity livingEntity = (LivingEntity) direct;
            if (livingEntity.getMainHandItem().getItem() instanceof GrandTorchItem){
                chance += 0.5F;
            } else if (livingEntity.getOffhandItem().getItem() instanceof GrandTorchItem){
                chance += 0.25F;
            }
            if (livingEntity.level.random.nextFloat() <= chance){
                victim.setSecondsOnFire(5);
            }
        }
        if (event.getSource() instanceof NoKnockBackDamageSource){
            NoKnockBackDamageSource damageSource = (NoKnockBackDamageSource) event.getSource();
            if (damageSource.getOwner() != null) {
                if (damageSource.getOwner() instanceof LivingEntity) {
                    victim.setLastHurtByMob((LivingEntity) damageSource.getOwner());
                }
                if (damageSource.getOwner() instanceof PlayerEntity) {
                    victim.setLastHurtByPlayer((PlayerEntity) damageSource.getOwner());
                }
                if (damageSource.getOwner() instanceof ServerPlayerEntity) {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) damageSource.getOwner(), victim, event.getSource(), event.getAmount(), event.getAmount(), false);
                }
            }
        }
        if (victim instanceof WitchEntity){
            double d0 = victim.getAttributeValue(Attributes.FOLLOW_RANGE);
            AxisAlignedBB axisalignedbb = AxisAlignedBB.unitCubeFromLowerCorner(victim.position()).inflate(d0, 10.0D, d0);
            List<MobEntity> list = victim.level.getLoadedEntitiesOfClass(MobEntity.class, axisalignedbb);

            for (MobEntity mob : list){
                if (victim.getLastHurtByMob() != null && !mob.isAlliedTo(victim.getLastHurtByMob()) && !victim.isAlliedTo(victim.getLastHurtByMob())) {
                    if (mob instanceof AbstractCultistEntity) {
                        mob.setTarget(victim.getLastHurtByMob());
                    }
                }
            }
        }
        if (direct instanceof AbstractArrowEntity){
            AbstractArrowEntity arrowEntity = (AbstractArrowEntity) direct;
            if (arrowEntity.getTags().contains(ConstantPaths.rainArrow()) || arrowEntity.getOwner() instanceof ApostleEntity){
                if (arrowEntity.getOwner() != null) {
                    if (victim instanceof OwnedEntity) {
                        OwnedEntity ownedEntity = (OwnedEntity) victim;
                        if (ownedEntity.getTrueOwner() != null) {
                            if (ownedEntity.getTrueOwner() == arrowEntity.getOwner()) {
                                event.setCanceled(true);
                            }
                        }
                    }
                    if (victim == arrowEntity.getOwner()){
                        event.setCanceled(true);
                    }
                }
            }
            if (victim instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) victim;
                if (PlayerUtil.starAmuletActive(player)){
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void PlayerAttackEvent(AttackEntityEvent event){
        if (SpellConfig.OwnerAttackCancel.get()) {
            if (event.getTarget() instanceof IOwned){
                if (((IOwned) event.getTarget()).getTrueOwner() == event.getPlayer()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void DamageEvent(LivingDamageEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (PlayerUtil.starAmuletActive(player)){
                if (event.getSource().getDirectEntity() instanceof AbstractArrowEntity){
                    event.setCanceled(true);
                }
            }
        }

        if (entity.hasEffect(ModEffects.CURSED.get())){
            EffectInstance effectInstance = entity.getEffect(ModEffects.CURSED.get());
            if (effectInstance != null) {
                int i = effectInstance.getAmplifier() + 1;
                event.setAmount(event.getAmount() * (1.0F + i));
            }
        }
        if (entity.hasEffect(ModEffects.SAPPED.get())){
            EffectInstance effectInstance = entity.getEffect(ModEffects.SAPPED.get());
            float original = event.getAmount();
            if (effectInstance != null) {
                int i = effectInstance.getAmplifier() + 1;
                original += event.getAmount() * 0.2F * i;
                event.setAmount(original);
            }
        }

        if (event.getSource().getEntity() instanceof IOwned){
            IOwned summonedEntity = (IOwned) event.getSource().getEntity();
            if (summonedEntity.getTrueOwner() != null){
                if (summonedEntity.getTrueOwner() == entity){
                    event.setCanceled(true);
                }
            }
        }

        if (event.getSource().getDirectEntity() instanceof FangEntity){
            FangEntity fangEntity = (FangEntity) event.getSource().getDirectEntity();
            if (fangEntity.getOwner() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) fangEntity.getOwner();
                if (fangEntity.isAbsorbing()) {
                    player.heal(event.getAmount());
                }
            }
        }

        if (event.getSource().getDirectEntity() instanceof AbstractDredenEntity){
            ((AbstractDredenEntity) event.getSource().getDirectEntity()).heal(event.getAmount());
        }
    }

    @SubscribeEvent
    public static void VisibilityEvent(LivingEvent.LivingVisibilityEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (event.getLookingEntity() instanceof LivingEntity){
            LivingEntity looker = (LivingEntity) event.getLookingEntity();
            boolean undead = looker.getMobType() == CreatureAttribute.UNDEAD && looker.getMaxHealth() < 50.0F && !(looker instanceof OwnedEntity && !(looker instanceof IMob)) && !(looker instanceof BoneLordEntity);
            if (RobeArmorFinder.FindNecroHelm(entity)){
                if (undead){
                    event.modifyVisibility(0.5);
                }
            }
            if (RobeArmorFinder.FindNecroArmor(entity)){
                if (undead){
                    event.modifyVisibility(0.3);
                }
            }
            if (RobeArmorFinder.FindNecroBootsofWander(entity)){
                if (undead){
                    event.modifyVisibility(0.2);
                }
            }
            if (entity.isInvisible()){
                if (RobeArmorFinder.FindIllusionHelm(entity)){
                    event.modifyVisibility(0.1);
                }
                if (RobeArmorFinder.FindIllusionArmor(entity)){
                    event.modifyVisibility(0.4);
                }
                if (RobeArmorFinder.FindIllusionLeggings(entity)){
                    event.modifyVisibility(0.3);
                }
                if (RobeArmorFinder.FindIllusionBootsofWander(entity)){
                    event.modifyVisibility(0.2);
                }
            }
        }
    }

    @SubscribeEvent
    public static void KnockBackEvents(LivingKnockBackEvent event){
        LivingEntity knocked = event.getEntityLiving();
        DamageSource lastDamage = knocked.getLastDamageSource();
        if (lastDamage != null) {
            if (lastDamage instanceof NoKnockBackDamageSource){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void SpecialDeath(LivingDeathEvent event){
        Entity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        World world = killed.getCommandSenderWorld();
        if (killed instanceof CreatureEntity){
            if (((CreatureEntity) killed).hasEffect(ModEffects.GOLDTOUCHED.get())){
                if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    int amp = Objects.requireNonNull(((CreatureEntity) killed).getEffect(ModEffects.GOLDTOUCHED.get())).getAmplifier() + 1;
                    for (int i = 0; i < (killed.level.random.nextInt(3) + 1) * amp; ++i) {
                        killed.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                    }
                }
            }
        }
        if (killed instanceof BatEntity){
            if (killer instanceof LivingEntity) {
                if (RobeArmorFinder.FindFelHelm((LivingEntity) killer)) {
                    if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                        killed.spawnAtLocation(new ItemStack(ModItems.DEADBAT.get()));
                    }
                }
            }
        }
        if (killed instanceof MonsterEntity){
            if (killer instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) killer;
                if (killed instanceof SpellcastingIllagerEntity || killed instanceof WitchEntity) {
                    if (killed.getTags().contains(ConstantPaths.structureMob())) {
                        float chance = 0.025F;
                        chance += (float) EnchantmentHelper.getMobLooting(player) / 100;
                        if (world.random.nextFloat() <= chance) {
                            killed.spawnAtLocation(new ItemStack(ModItems.FORBIDDEN_FRAGMENT.get()));
                        }
                    }
                }
            }
        }
        if (killed instanceof SkeletonEntity){
            SkeletonEntity skeletonEntity = (SkeletonEntity) killed;
            if (killer instanceof DredenEntity){
                StrayEntity strayEntity = (skeletonEntity).convertTo(EntityType.STRAY, false);
                if (strayEntity != null) {
                    strayEntity.finalizeSpawn((IServerWorld) killed.level, killed.level.getCurrentDifficultyAt(strayEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(skeletonEntity, strayEntity);
                    strayEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                }
            }
            if (killer instanceof DredenMinionEntity){
                DredenMinionEntity dredenMinion = (DredenMinionEntity) killer;
                StrayMinionEntity strayEntity = ((SkeletonEntity) killed).convertTo(ModEntityType.STRAY_MINION.get(), false);
                if (strayEntity != null) {
                    strayEntity.finalizeSpawn((IServerWorld) killed.level, killed.level.getCurrentDifficultyAt(strayEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                    strayEntity.setLimitedLife(10 * (15 + strayEntity.level.random.nextInt(45)));
                    if (dredenMinion.getTrueOwner() != null){
                        strayEntity.setTrueOwner(dredenMinion.getTrueOwner());
                    }
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) killed, strayEntity);
                    strayEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                }
            }
        }
        if (killer instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) killer;
            int r1 = world.random.nextInt(4);
            int r2 = world.random.nextInt(16);
            int looting = MathHelper.clamp(EnchantmentHelper.getMobLooting(player), 0, 3);
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)){
                if (killed instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity) killed;
                    if (player.getMainHandItem().getItem() instanceof AxeItem && ModDamageSource.physicalAttacks(event.getSource())) {
                        if (livingEntity.getMobType() != CreatureAttribute.UNDEAD) {
                            if (livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof SpellcastingIllagerEntity || livingEntity instanceof ChannellerEntity || livingEntity instanceof PlayerEntity) {
                                if (r1 - looting <= 0) {
                                    livingEntity.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
                                }
                            } else if (livingEntity instanceof PatrollerEntity) {
                                if (r2 - looting <= 0) {
                                    livingEntity.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
                                }
                            }
                        }
                    }
                    Entity entity = event.getSource().getDirectEntity();
                    if (entity instanceof FangEntity){
                        if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player)) >= 3) {
                                if (r1 == 0) {
                                    if (livingEntity.getType() == EntityType.SKELETON) {
                                        livingEntity.spawnAtLocation(new ItemStack(Items.SKELETON_SKULL));
                                    }
                                    if (livingEntity.getType() == EntityType.ZOMBIE) {
                                        livingEntity.spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD));
                                    }
                                    if (livingEntity.getType() == EntityType.CREEPER) {
                                        livingEntity.spawnAtLocation(new ItemStack(Items.CREEPER_HEAD));
                                    }
                                    if (livingEntity.getType() == EntityType.WITHER_SKELETON) {
                                        livingEntity.spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL));
                                    }
                                    if (MainConfig.TallSkullDrops.get()) {
                                        if (livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof AbstractIllagerEntity) {
                                            livingEntity.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
                                        }
                                        if (livingEntity instanceof WitchEntity || livingEntity instanceof ICultist) {
                                            livingEntity.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
                                        }
                                    }
                                }
                                if (livingEntity instanceof PlayerEntity) {
                                    PlayerEntity player1 = (PlayerEntity) livingEntity;
                                    CompoundNBT tag = new CompoundNBT();
                                    tag.putString("SkullOwner", player1.getDisplayName().getString());
                                    ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                                    head.setTag(tag);
                                    livingEntity.spawnAtLocation(head);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void ExtraExpDrop(LivingExperienceDropEvent event){
        if (event.getAttackingPlayer() != null) {
            if (event.getAttackingPlayer().hasEffect(ModEffects.COSMIC.get())) {
                int a = Objects.requireNonNull(event.getAttackingPlayer().getEffect(ModEffects.COSMIC.get())).getAmplifier() + 2;
                int a1 = MathHelper.clamp(a, 2, 8);
                event.setDroppedExperience(event.getDroppedExperience() * a1);
            }
        }
        if (event.getEntityLiving().hasEffect(ModEffects.NECROPOWER.get())){
            event.setDroppedExperience(event.getDroppedExperience() * 2);
        }
    }

    @SubscribeEvent
    public static void SpellLoot(LootingLevelEvent event){
        if (event.getDamageSource() != null) {
            if (event.getEntityLiving() != null) {
                if (!event.getEntityLiving().level.isClientSide) {
                    int looting = 0;
                    if (event.getDamageSource() instanceof NoKnockBackDamageSource){
                        NoKnockBackDamageSource damageSource = (NoKnockBackDamageSource) event.getDamageSource();
                        if (damageSource.getOwner() != null){
                            if (damageSource.getOwner() instanceof PlayerEntity) {
                                PlayerEntity player = (PlayerEntity) damageSource.getOwner();
                                if (player != null) {
                                    if (EnchantmentHelper.getMobLooting(player) != 0){
                                        looting = EnchantmentHelper.getMobLooting(player);
                                    } else if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                                        if (CuriosFinder.findRing(player).isEnchanted()) {
                                            looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                                        }
                                    }
                                    event.setLootingLevel(event.getLootingLevel() + looting);
                                }
                            }
                            if (damageSource.getOwner() instanceof IOwned) {
                                IOwned ownedEntity = (IOwned) damageSource.getOwner();
                                if (ownedEntity != null) {
                                    if (ownedEntity.getTrueOwner() instanceof PlayerEntity) {
                                        PlayerEntity player = (PlayerEntity) ownedEntity.getTrueOwner();
                                        if (player != null) {
                                            if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                                                if (CuriosFinder.findRing(player).isEnchanted()) {
                                                    looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                                                }
                                            }
                                            event.setLootingLevel(event.getLootingLevel() + looting);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (event.getDamageSource().getEntity() != null) {
                        if (event.getDamageSource().getEntity() instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) event.getDamageSource().getEntity();
                            if (player != null) {
                                Entity spell = event.getDamageSource().getDirectEntity();
                                if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                                    if (CuriosFinder.findRing(player).isEnchanted()) {
                                        looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                                    }
                                }
                                if (looting > EnchantmentHelper.getMobLooting(player)) {
                                    if (spell != null) {
                                        if (!(spell instanceof LivingEntity)) {
                                            event.setLootingLevel(event.getLootingLevel() + looting);
                                        }
                                    }
                                    if (ModDamageSource.breathAttacks(event.getDamageSource())) {
                                        event.setLootingLevel(event.getLootingLevel() + looting);
                                    }
                                }
                            }
                        }
                        if (event.getDamageSource().getEntity() instanceof IOwned) {
                            IOwned ownedEntity = (IOwned) event.getDamageSource().getEntity();
                            if (ownedEntity != null) {
                                if (ownedEntity instanceof LivingEntity) {
                                    if (ownedEntity.getTrueOwner() instanceof PlayerEntity) {
                                        PlayerEntity player = (PlayerEntity) ownedEntity.getTrueOwner();
                                        if (player != null) {
                                            if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                                                if (CuriosFinder.findRing(player).isEnchanted()) {
                                                    looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                                                }
                                            }
                                            if (looting > EnchantmentHelper.getMobLooting((LivingEntity) ownedEntity)) {
                                                event.setLootingLevel(event.getLootingLevel() + looting);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void DropEvents(LivingDropsEvent event){
        if (event.getEntityLiving() != null) {
            LivingEntity living = event.getEntityLiving();
            if (living instanceof RavagerEntity){
                event.getDrops().add(ItemHelper.itemEntityDrop(living, new ItemStack(ModItems.SAVAGE_TOOTH.get(), living.level.random.nextInt(2))));
            }
            if (living instanceof BeldamEntity){
                if (living.level.getServer() != null) {
                    LootTable loottable = living.level.getServer().getLootTables().get(ModLootTables.CULTISTS);
                    LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), living);
                    LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                    loottable.getRandomItems(ctx).forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                }
            }
            if (MainConfig.TallSkullDrops.get()) {
                if (living instanceof AbstractVillagerEntity || living instanceof AbstractIllagerEntity || living instanceof WitchEntity || living instanceof ICultist) {
                    if (living.level.getServer() != null) {
                        LootTable loottable = living.level.getServer().getLootTables().get(ModLootTables.TALL_SKULL);
                        LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), living);
                        LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                        loottable.getRandomItems(ctx).forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void finishItemEvents(LivingEntityUseItemEvent.Finish event){
        if (event.getItem().getItem() == Items.MILK_BUCKET){
            if (event.getEntityLiving().hasEffect(ModEffects.ILLAGUE.get())){
                int duration = Objects.requireNonNull(event.getEntityLiving().getEffect(ModEffects.ILLAGUE.get())).getDuration();
                int amp = Objects.requireNonNull(event.getEntityLiving().getEffect(ModEffects.ILLAGUE.get())).getAmplifier();
                if (duration > 0){
                    if (amp <= 0) {
                        EffectsUtil.halveDuration(event.getEntityLiving(), ModEffects.ILLAGUE.get(), duration, false, false);
                    } else {
                        EffectsUtil.deamplifyEffect(event.getEntityLiving(), ModEffects.ILLAGUE.get(), duration, false, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void SleepEvents(PlayerWakeUpEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.isSleepingLongEnough()) {
            if (player.hasEffect(ModEffects.ILLAGUE.get())) {
                int duration = Objects.requireNonNull(player.getEffect(ModEffects.ILLAGUE.get())).getDuration();
                int amp = Objects.requireNonNull(player.getEffect(ModEffects.ILLAGUE.get())).getAmplifier();
                if (duration > 0){
                    if (amp <= 0) {
                        EffectsUtil.halveDuration(player, ModEffects.ILLAGUE.get(), duration, false, false);
                    } else {
                        EffectsUtil.deamplifyEffect(player, ModEffects.ILLAGUE.get(), duration, false, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void ExplosionDetonateEvent(ExplosionEvent.Detonate event){
        if (event.getExplosion() != null) {
            event.getAffectedEntities().removeIf(entity -> (entity instanceof ItemEntity && ((ItemEntity) entity).getItem().getItem() == ModItems.UNHOLY_BLOOD.get()));
            if (event.getExplosion().getSourceMob() != null) {
                if (event.getExplosion().getSourceMob() instanceof ApostleEntity) {
                    event.getAffectedEntities().removeIf(entity -> (entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() instanceof ApostleEntity) || (entity == event.getExplosion().getSourceMob()));
                }
                if (event.getExplosion().getSourceMob() instanceof OwnedEntity) {
                    OwnedEntity sourceMob = (OwnedEntity) event.getExplosion().getSourceMob();
                    if (sourceMob.getTrueOwner() instanceof ApostleEntity) {
                        event.getAffectedEntities().removeIf(entity -> (entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() instanceof ApostleEntity) || entity == sourceMob.getTrueOwner());
                    }
                }
                if (event.getExplosion().getSourceMob() instanceof CreeperlingMinionEntity) {
                    CreeperlingMinionEntity creeperlingMinion = (CreeperlingMinionEntity) event.getExplosion().getSourceMob();
                    event.getAffectedEntities().removeIf(entity -> entity instanceof LivingEntity && creeperlingMinion.getTrueOwner() == (LivingEntity) entity && RobeArmorFinder.FindFelArmor((LivingEntity) entity));
                }
            }
        }
    }

    @SubscribeEvent
    public static void ProjectileImpactEvent(ProjectileImpactEvent.Arrow event){
        AbstractArrowEntity arrowEntity = event.getArrow();
        if (event.getArrow().getTags().contains(ConstantPaths.rainArrow())){
            arrowEntity.remove();
        }
    }

    @SubscribeEvent
    public static void onEffectRemoval(PotionEvent.PotionRemoveEvent event){
        if (event.getEntityLiving() != null && event.getPotion() != null) {
            if (!event.getEntityLiving().level.isClientSide) {
                if (event.getPotion() == ModEffects.FEL_VISION.get()) {
                    if (event.getEntityLiving().hasEffect(Effects.NIGHT_VISION)) {
                        event.getEntityLiving().removeEffect(Effects.NIGHT_VISION);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(PotionEvent.PotionExpiryEvent event){
        if (event.getEntityLiving() != null && event.getPotionEffect() != null) {
            if (!event.getEntityLiving().level.isClientSide) {
                if (event.getPotionEffect().getEffect() == ModEffects.FEL_VISION.get()) {
                    if (event.getEntityLiving().hasEffect(Effects.NIGHT_VISION)) {
                        event.getEntityLiving().removeEffect(Effects.NIGHT_VISION);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(PotionEvent.PotionApplicableEvent event){
        if (event.getPotionEffect().getEffect() == Effects.FIRE_RESISTANCE){
            if (event.getEntityLiving().hasEffect(ModEffects.BURN_HEX.get())){
                event.setResult(Event.Result.DENY);
            }
        }
        if (event.getPotionEffect().getEffect() == ModEffects.SAPPED.get()){
            if (event.getEntityLiving().hasEffect(ModEffects.CURSED.get())){
                event.setResult(Event.Result.DENY);
            }
        }
        if (event.getPotionEffect().getEffect() == ModEffects.ILLAGUE.get()){
            if (event.getEntity() instanceof PatrollerEntity || event.getEntity().getType().is(EntityTypeTags.RAIDERS) || event.getEntity() instanceof IDeadMob){
                event.setResult(Event.Result.DENY);
            }
        }
        if (event.getPotionEffect().getEffect() == Effects.BLINDNESS){
            if (RobeArmorFinder.FindIllusionHelm(event.getEntityLiving())){
                event.setResult(Event.Result.DENY);
            }
        }
        if (event.getPotionEffect().getEffect() == ModEffects.NECROPOWER.get()){
            if (event.getEntityLiving().getMobType() != CreatureAttribute.UNDEAD){
                if (event.getEntityLiving() instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                    if (!LichdomHelper.isLich(player)){
                        event.setResult(Event.Result.DENY);
                    }
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionAddedEvents(PotionEvent.PotionAddedEvent event){
        Effect effect = event.getPotionEffect().getEffect();
        if (effect == ModEffects.BURN_HEX.get()){
            if (event.getEntityLiving().hasEffect(Effects.FIRE_RESISTANCE)){
                event.getEntityLiving().removeEffect(Effects.FIRE_RESISTANCE);
            }
        }
        if (effect == ModEffects.CURSED.get()){
            if (event.getEntityLiving().hasEffect(ModEffects.SAPPED.get())){
                event.getEntityLiving().removeEffect(ModEffects.SAPPED.get());
            }
        }
    }

    @SubscribeEvent
    public static void Mutation(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == ModEffects.COSMIC.get()){
            World world = event.getEntityLiving().level;
            LivingEntity livingEntity = event.getEntityLiving();
            if (!world.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) world;
                for (int k = 0; k < 50; ++k) {
                    float f = world.random.nextFloat() * 4.0F;
                    float f1 = world.random.nextFloat() * ((float) Math.PI * 2F);
                    double d1 = (double) (MathHelper.cos(f1) * f);
                    double d2 = 0.01D + world.random.nextDouble() * 0.5D;
                    double d3 = (double) (MathHelper.sin(f1) * f);
                    serverWorld.sendParticles(ParticleTypes.DRAGON_BREATH, (double) livingEntity.blockPosition().getX() + d1 * 0.1D, (double) livingEntity.blockPosition().getY() + 0.3D, (double) livingEntity.blockPosition().getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                }
            }
            if (livingEntity instanceof CowEntity){
                MutatedCowEntity mutatedCowEntity = new MutatedCowEntity(ModEntityType.MUTATED_COW.get(), world);
                mutatedCowEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                mutatedCowEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedCowEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (livingEntity.hasCustomName()) {
                    mutatedCowEntity.setCustomName(livingEntity.getCustomName());
                    mutatedCowEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }
                mutatedCowEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedCowEntity);
                livingEntity.remove();
            } else if (livingEntity instanceof ChickenEntity){
                MutatedChickenEntity mutatedChickenEntity = new MutatedChickenEntity(ModEntityType.MUTATED_CHICKEN.get(), world);
                mutatedChickenEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                mutatedChickenEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedChickenEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (livingEntity.hasCustomName()) {
                    mutatedChickenEntity.setCustomName(livingEntity.getCustomName());
                    mutatedChickenEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }
                mutatedChickenEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedChickenEntity);
                livingEntity.remove();
            } else if (livingEntity instanceof SheepEntity){
                MutatedSheepEntity mutatedSheepEntity = new MutatedSheepEntity(ModEntityType.MUTATED_SHEEP.get(), world);
                mutatedSheepEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                mutatedSheepEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedSheepEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (livingEntity.hasCustomName()) {
                    mutatedSheepEntity.setCustomName(livingEntity.getCustomName());
                    mutatedSheepEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }
                mutatedSheepEntity.setColor(((SheepEntity) livingEntity).getColor());
                mutatedSheepEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedSheepEntity);
                livingEntity.remove();
            } else if (livingEntity instanceof PigEntity){
                MutatedPigEntity mutatedPigEntity = new MutatedPigEntity(ModEntityType.MUTATED_PIG.get(), world);
                mutatedPigEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                mutatedPigEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedPigEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (livingEntity.hasCustomName()) {
                    mutatedPigEntity.setCustomName(livingEntity.getCustomName());
                    mutatedPigEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }
                mutatedPigEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedPigEntity);
                livingEntity.remove();
            } else if (livingEntity instanceof RabbitEntity){
                RabbitEntity rabbit = (RabbitEntity) livingEntity;
                MutatedRabbitEntity mutatedRabbitEntity = new MutatedRabbitEntity(ModEntityType.MUTATED_RABBIT.get(), world);
                mutatedRabbitEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                mutatedRabbitEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedRabbitEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (livingEntity.hasCustomName()) {
                    mutatedRabbitEntity.setCustomName(livingEntity.getCustomName());
                    mutatedRabbitEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }
                if (rabbit.getRabbitType() != 99) {
                    mutatedRabbitEntity.setRabbitType(rabbit.getRabbitType());
                } else {
                    mutatedRabbitEntity.setRabbitType(1);
                }
                mutatedRabbitEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedRabbitEntity);
                livingEntity.remove();
            }
        }
    }

}
