package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.hostile.dead.FallenEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.HuntingIllagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.entities.utilities.StormEntity;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyProvider;
import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.common.lichdom.LichProvider;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.soulenergy.SEProvider;
import com.Polarice3.Goety.common.spider.SpiderLevelsProvider;
import com.Polarice3.Goety.compat.patchouli.PatchouliLoaded;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.GossipType;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerCapabilityAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Goety.MOD_ID, "infamy"), new InfamyProvider());
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
        if (entity instanceof LivingEntity && !event.getWorld().isClientSide()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                InfamyHelper.sendInfamyUpdatePacket(player);
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
        if (entity instanceof StormEntity){
            if (!entity.level.isClientSide){
                ServerWorld serverWorld = (ServerWorld) entity.level;
                serverWorld.setWeatherParameters(0, 6000, true, true);
            }
        }
        if (entity instanceof AbstractRaiderEntity){
            if (MainConfig.IllagerRaid.get()) {
                AbstractRaiderEntity raider = (AbstractRaiderEntity) entity;
                World world = event.getWorld();
                if (world instanceof ServerWorld) {
                    IServerWorld serverWorld = (IServerWorld) world;
                    if (raider.hasActiveRaid()) {
                        Raid raid = raider.getCurrentRaid();
                        if (raid != null) {
                            PlayerEntity player = EntityFinder.getNearbyPlayer(world, raid.getCenter());
                            if (player != null) {
                                IInfamy infamy = InfamyHelper.getCapability(player);
                                if (infamy.getInfamy() >= (MainConfig.InfamyThreshold.get() * 2)) {
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
                                            assert illager != null;
                                            if (world.random.nextInt(4) == 0){
                                                illager.setRider(true);
                                            }
                                            illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                            illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                            serverWorld.addFreshEntity(illager);
                                        }
                                        if (raider.getType() == EntityType.EVOKER) {
                                            EnviokerEntity illager = ModEntityType.ENVIOKER.get().create(world);
                                            assert illager != null;
                                            illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                            illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                            serverWorld.addFreshEntity(illager);
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
                                                assert illager != null;
                                                illager.moveTo(raider.getX(), raider.getY(), raider.getZ());
                                                if (world.random.nextInt(4) == 0){
                                                    illager.setRider(true);
                                                }
                                                illager.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(raider.blockPosition()), SpawnReason.EVENT, null, null);
                                                serverWorld.addFreshEntity(illager);
                                            }
                                        }
                                        if (raider.getType() == EntityType.RAVAGER) {
                                            EnviokerEntity envioker = ModEntityType.ENVIOKER.get().create(world);
                                            assert envioker != null;
                                            if (world.random.nextInt(4) == 0){
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

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();

        IInfamy capability = event.getOriginal().getCapability(InfamyProvider.CAPABILITY).resolve().get();

        player.getCapability(InfamyProvider.CAPABILITY)
                .ifPresent(infamy ->
                        infamy.setInfamy(capability.getInfamy() > 0 ? capability.getInfamy() - MainConfig.DeathLoseInfamy.get() : 0));

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
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getBiomeCategory() == Biome.Category.OCEAN) {
                    event.getSpawns().getSpawner(EntityClassification.WATER_AMBIENT).add(new MobSpawnInfo.Spawners(ModEntityType.SACRED_FISH.get(), 1, 1, 1));
                }
            }
        }
    }

    private static final Map<ServerWorld, IllagerSpawner> ILLAGER_SPAWNER_MAP = new HashMap<>();
    private static final Map<ServerWorld, EffectsEvent> EFFECTS_EVENT_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.put((ServerWorld) event.getWorld(), new IllagerSpawner());
            EFFECTS_EVENT_MAP.put((ServerWorld) event.getWorld(), new EffectsEvent());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.remove(event.getWorld());
            EFFECTS_EVENT_MAP.remove(event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            IllagerSpawner illagerSpawner = ILLAGER_SPAWNER_MAP.get(serverWorld);
            EffectsEvent effectsEvent = EFFECTS_EVENT_MAP.get(serverWorld);
            if (illagerSpawner != null){
                illagerSpawner.tick(serverWorld);
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
        if (event.getEntityLiving() instanceof AbstractCultistEntity){
            SpawnReason spawnReason = event.getSpawnReason();
            if (PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.FANATIC.get(), event.getWorld(), spawnReason, event.getEntityLiving().blockPosition(), event.getWorld().getRandom())){
                event.setResult(Event.Result.ALLOW);
            }
        }
        if (event.getEntityLiving() instanceof SpellcastingIllagerEntity || event.getEntityLiving() instanceof WitchEntity){
            if (event.getSpawnReason() == SpawnReason.STRUCTURE){
                event.getEntityLiving().addTag(ConstantPaths.structureMob());
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
                BlockState blockState = livingEntity.level.getBlockState(livingEntity.blockPosition().below());
                if (blockState.getBlock() instanceof IDeadBlock){
                    livingEntity.clearFire();
                }
            }
            if (livingEntity.hasEffect(ModEffects.APOSTLE_CURSE.get())){
                if (livingEntity.hasEffect(Effects.FIRE_RESISTANCE)){
                    livingEntity.removeEffectNoUpdate(Effects.FIRE_RESISTANCE);
                }
            }
            if (livingEntity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (RobeArmorFinder.FindBootsofWander(player)){
                    BootsUtil.enableStepHeight(player);
                } else {
                    BootsUtil.disableStepHeight(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.hasEffect(ModEffects.NOMINE.get())){
            if (BlockFinder.NoBreak(event.getState()) && !(event.getState().getBlock() == ModBlocks.GUARDIAN_OBELISK.get())){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlacingBlock(BlockEvent.EntityPlaceEvent event){
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        World world = player.level;
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
        if (RobeArmorFinder.FindNecroSet(player)) {
            BlockState blockState = player.level.getBlockState(player.blockPosition().below());
            if (!LichdomHelper.isLich(player)) {
                if (!(blockState.getBlock() instanceof IDeadBlock)) {
                    if (!world.isClientSide) {
                        if (world.isDay() && !world.isRaining() && !world.isThundering()){
                            BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                            if (world.canSeeSky(blockpos)) {
                                player.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
                                player.addEffect(new EffectInstance(Effects.HUNGER, 20, 1));
                            } else {
                                if (player.tickCount % 50 == 0){
                                    if (player.getHealth() < player.getMaxHealth()) {
                                        player.heal(1.0F);
                                    }
                                }
                                player.addEffect(new EffectInstance(Effects.REGENERATION, 20, 0, false, false));
                            }
                        } else {
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
            boolean blind = false;
            if (!world.isClientSide && world.isDay()) {
                float f = player.getBrightness();
                BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                if (f > 0.5F && world.canSeeSky(blockpos)) {
                    blind = true;
                }
            }
            if (!LichdomHelper.isLich(player)) {
                if (!world.isClientSide) {
                    if (blind) {
                        if (player.hasEffect(Effects.NIGHT_VISION)){
                            player.removeEffect(Effects.NIGHT_VISION);
                        }
                        player.addEffect(new EffectInstance(Effects.BLINDNESS, 40, 0, false, false));
                    } else {
                        if (player.hasEffect(Effects.BLINDNESS)){
                            player.removeEffect(Effects.BLINDNESS);
                        }
                        player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 210, 0, false, false));
                    }
                }
            }
        }
        if (player.hasEffect(ModEffects.SOUL_SHIELD.get())){
            for (AbstractArrowEntity arrowEntity: player.level.getEntitiesOfClass(AbstractArrowEntity.class, player.getBoundingBox().inflate(2.0D))){
                if (arrowEntity.getOwner() != player && !(arrowEntity.getOwner() instanceof ApostleEntity)){
                    arrowEntity.remove();
                }
            }
        }
        if (RobeArmorFinder.FindFelArmor(player)){
            BlockFinder.WebMovement(player);
        }
        IInfamy infamy = InfamyHelper.getCapability(player);
        int i = infamy.getInfamy();
        if (i < 0){
            infamy.setInfamy(0);
        }
        if (i > MainConfig.InfamyThreshold.get() * 2){
            for (AbstractRaiderEntity pillagerEntity : player.level.getEntitiesOfClass(AbstractRaiderEntity.class, player.getBoundingBox().inflate(32))){
                if (pillagerEntity.getTarget() == player) {
                    if (!pillagerEntity.isAggressive()) {
                        pillagerEntity.setAggressive(true);
                    }
                }
            }
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
                event.setDamageMultiplier(0);
                if (!player.isCrouching()) {
                    if (event.getDistance() >= 1.27F) {
                        double jump = MathHelper.sqrt(event.getDistance())/2;
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
                    f += 0.1F * (float)(player.getEffect(Effects.JUMP).getAmplifier() + 1);
                }
                Vector3d vector3d = player.getDeltaMovement();
                player.setDeltaMovement(vector3d.x, f, vector3d.z);
            }
        }

    }

    @SubscribeEvent
    public static void TargetSelection(LivingSetAttackTargetEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        LivingEntity target = event.getTarget();
        if (livingEntity instanceof MobEntity){
            MobEntity mob = (MobEntity) livingEntity;
            if (target != null){
                if (target instanceof PlayerEntity){
                    if (mob.getLastHurtByMob() instanceof OwnedEntity){
                        mob.setTarget(mob.getLastHurtByMob());
                    }
                    if (RobeArmorFinder.FindNecroSet(target)){
                        boolean undead = mob.getMobType() == CreatureAttribute.UNDEAD && mob.getMaxHealth() < 50.0F && !(mob instanceof ICultistMinion);
                        if (undead){
                            if (mob.getLastHurtByMob() != target){
                                mob.setTarget(null);
                            } else {
                                mob.setLastHurtByMob(target);
                            }
                        }
                    }
                }
                if (mob.getMobType() == CreatureAttribute.UNDEAD || mob instanceof CreeperEntity){
                    if (target instanceof ApostleEntity){
                        mob.setTarget(null);
                    }
                }
                if (mob.getMobType() == CreatureAttribute.ARTHROPOD){
                    if (RobeArmorFinder.FindFelHelm(target)){
                        if (mob.getLastHurtByMob() != target){
                            mob.setTarget(null);
                        } else {
                            mob.setLastHurtByMob(target);
                        }
                    }
                }
                if (mob instanceof CreeperEntity){
                    if (RobeArmorFinder.FindFelArmor(target)){
                        if (mob.getLastHurtByMob() != target){
                            mob.setTarget(null);
                        } else {
                            mob.setLastHurtByMob(target);
                        }
                    }
                }
                if (mob instanceof SlimeEntity){
                    if (RobeArmorFinder.FindFelBootsofWander(target)){
                        if (mob.getLastHurtByMob() != target){
                            mob.setTarget(null);
                        } else {
                            mob.setLastHurtByMob(target);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        if (victim.hasEffect(ModEffects.CURSED.get())){
            EffectInstance effectInstance = victim.getEffect(ModEffects.CURSED.get());
            assert effectInstance != null;
            int i = effectInstance.getAmplifier() + 1;
            event.setAmount(event.getAmount() * (1.0F + i));
        }
        if (RobeArmorFinder.FindFelArmor(victim)){
            if (attacker instanceof CreeperlingMinionEntity){
                CreeperlingMinionEntity creeperlingMinion = (CreeperlingMinionEntity) attacker;
                if (creeperlingMinion.getTrueOwner() == victim){
                    event.setAmount(0);
                }
            }
            if (event.getSource().isExplosion()){
                event.setAmount((float) (event.getAmount()/1.5));
            }
        }
        if (attacker instanceof FangEntity){
            FangEntity fangEntity = (FangEntity) attacker;
            if (fangEntity.getOwner() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) fangEntity.getOwner();
                if (fangEntity.isAbsorbing()) {
                    player.heal(event.getAmount());
                }
            }
        }
        if (attacker instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) attacker;
            if (event.getSource() instanceof ModDamageSource) {
                ModDamageSource modDamageSource = (ModDamageSource) event.getSource();
                if (modDamageSource.isBreath()) {
                    float f1 = (float) livingEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    if (f1 > 0.0F) {
                        (victim).knockback(f1 * 0.5F, (double) MathHelper.sin(victim.yRot * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(victim.yRot * ((float) Math.PI / 180F))));
                        victim.setDeltaMovement(victim.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void AttackEvent(LivingAttackEvent event){
        LivingEntity victim = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        if (RobeArmorFinder.FindFelBootsofWander(victim)){
            if (attacker instanceof SlimeEntity){
                if (((SlimeEntity) attacker).getTarget() != victim){
                    event.setCanceled(true);
                }
            }
        }
        if (MainConfig.MinionsMasterImmune.get()){
            if (attacker instanceof OwnedEntity){
                if (((OwnedEntity) attacker).getTrueOwner() == victim){
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void DamageEvent(LivingDamageEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity.hasEffect(ModEffects.SOUL_SHIELD.get())){
            if (event.getSource().getDirectEntity() instanceof AbstractArrowEntity){
                event.setCanceled(true);
            }
        }
        if (event.getSource().getEntity() instanceof OwnedEntity){
            OwnedEntity summonedEntity = (OwnedEntity) event.getSource().getEntity();
            if (summonedEntity.getTrueOwner() != null){
                if (summonedEntity.getTrueOwner() == entity){
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void VisibilityEvent(LivingEvent.LivingVisibilityEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (event.getLookingEntity() instanceof LivingEntity){
            LivingEntity looker = (LivingEntity) event.getLookingEntity();
            boolean undead = looker.getMobType() == CreatureAttribute.UNDEAD && looker.getMaxHealth() < 50.0F;
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
                    for (int i = 0; i < killed.level.random.nextInt(4) + amp * amp; ++i) {
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
                if (killed instanceof BeldamEntity){
                    BeldamEntity beldam = (BeldamEntity) killed;
                    LootTable loottable = player.level.getServer().getLootTables().get(ModLootTables.CULTISTS);
                    LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), beldam);
                    LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                    loottable.getRandomItems(ctx).forEach(beldam::spawnAtLocation);
                }
            }
        }
        if (killed instanceof AbstractIllagerEntity){
            if (killer instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) killer;
                if (!GoldTotemFinder.FindTotem(player).isEmpty() || RobeArmorFinder.FindArmor(player)){
                    if (killed instanceof PillagerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.PillagerInfamy.get());
                    } else
                    if (killed instanceof VindicatorEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.VindicatorInfamy.get());
                    } else
                    if (killed instanceof EvokerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.EvokerInfamy.get());
                    } else
                    if (killed instanceof IllusionerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.IllusionerInfamy.get());
                    } else
                    if (killed instanceof EnviokerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.EnviokerInfamy.get());
                    } else
                    if (killed instanceof InquillagerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.InquillagerInfamy.get());
                    } else
                    if (killed instanceof ConquillagerEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.ConquillagerInfamy.get());
                    } else
                    if (killed instanceof VizierEntity){
                        InfamyHelper.increaseInfamy(player, MainConfig.VizierInfamy.get());
                    } else {
                        InfamyHelper.increaseInfamy(player, MainConfig.OtherInfamy.get());
                    }
                    InfamyHelper.sendInfamyUpdatePacket(player);
                }
            }
            if (killer instanceof OwnedEntity) {
                OwnedEntity summonedEntity = (OwnedEntity) killer;
                if (summonedEntity.getTrueOwner() != null) {
                    if (summonedEntity.getTrueOwner() instanceof PlayerEntity){
                        PlayerEntity player = (PlayerEntity) summonedEntity.getTrueOwner();
                        if (killed instanceof PillagerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.PillagerInfamy.get());
                        } else if (killed instanceof VindicatorEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.VindicatorInfamy.get());
                        } else if (killed instanceof EvokerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.EvokerInfamy.get());
                        } else if (killed instanceof IllusionerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.IllusionerInfamy.get());
                        } else if (killed instanceof EnviokerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.EnviokerInfamy.get());
                        } else if (killed instanceof InquillagerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.InquillagerInfamy.get());
                        } else if (killed instanceof ConquillagerEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.ConquillagerInfamy.get());
                        } else if (killed instanceof VizierEntity) {
                            InfamyHelper.increaseInfamy(player, MainConfig.VizierInfamy.get());
                        } else {
                            InfamyHelper.increaseInfamy(player, MainConfig.OtherInfamy.get());
                        }
                        InfamyHelper.sendInfamyUpdatePacket(player);
                    }
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
                    if (player.getMainHandItem().getItem() instanceof AxeItem && event.getSource().getDirectEntity() == player) {
                        if (livingEntity.getMobType() != CreatureAttribute.UNDEAD) {
                            if (livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof SpellcastingIllagerEntity || livingEntity instanceof ChannellerEntity || livingEntity instanceof PlayerEntity) {
                                if (r1 - looting == 0) {
                                    livingEntity.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
                                }
                            } else if (livingEntity instanceof PatrollerEntity) {
                                if (r2 - looting == 0) {
                                    livingEntity.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
                                }
                            }
                        }
                    }
                    if (livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof AbstractIllagerEntity || livingEntity instanceof WitchEntity || livingEntity instanceof ICultist){
                        LootTable loottable = player.level.getServer().getLootTables().get(ModLootTables.TALL_SKULL);
                        LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), livingEntity);
                        LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                        loottable.getRandomItems(ctx).forEach(livingEntity::spawnAtLocation);
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
                                    if (livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof AbstractIllagerEntity){
                                        livingEntity.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
                                    }
                                    if (livingEntity instanceof WitchEntity || livingEntity instanceof ICultist){
                                        livingEntity.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
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
        if (!event.getEntityLiving().level.isClientSide) {
            int looting = 0;
            if (event.getDamageSource().getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getDamageSource().getEntity();
                Entity spell = event.getDamageSource().getDirectEntity();
                if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                    if (CuriosFinder.findRing(player).isEnchanted()){
                        looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                    }
                }
                if (spell instanceof FangEntity) {
                    event.setLootingLevel(event.getLootingLevel() + looting);
                }
                if (spell instanceof DamagingProjectileEntity){
                    event.setLootingLevel(event.getLootingLevel() + looting);
                }
                if (event.getDamageSource() instanceof ModDamageSource) {
                    ModDamageSource modDamageSource = (ModDamageSource) event.getDamageSource();
                    if (modDamageSource.isBreath()) {
                        event.setLootingLevel(event.getLootingLevel() + looting);
                    }
                }
            }
            if (event.getDamageSource().getEntity() instanceof OwnedEntity) {
                OwnedEntity ownedEntity = (OwnedEntity) event.getDamageSource().getEntity();
                if (ownedEntity.getTrueOwner() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) ownedEntity.getTrueOwner();
                    if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                        if (CuriosFinder.findRing(player).isEnchanted()){
                            looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                        }
                    }
                    if (player != null) {
                        event.setLootingLevel(event.getLootingLevel() + looting);
                    }
                }
            }
            if (event.getDamageSource().getEntity() instanceof LoyalSpiderEntity) {
                LoyalSpiderEntity loyalSpiderEntity = (LoyalSpiderEntity) event.getDamageSource().getEntity();
                if (loyalSpiderEntity.getTrueOwner() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) loyalSpiderEntity.getTrueOwner();
                    if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                        if (CuriosFinder.findRing(player).isEnchanted()){
                            looting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                        }
                    }
                    if (player != null) {
                        event.setLootingLevel(event.getLootingLevel() + looting);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionAddedEvents(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == Effects.HERO_OF_THE_VILLAGE){
            if (event.getEntityLiving() instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if (!GoldTotemFinder.FindTotem(player).isEmpty() || RobeArmorFinder.FindAnySet(player)){
                    InfamyHelper.increaseInfamy(player, 100);
                    InfamyHelper.sendInfamyUpdatePacket(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void Mutation(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == ModEffects.COSMIC.get()){
            World world = event.getEntityLiving().level;
            LivingEntity entity = event.getEntityLiving();
            for(int i = 0; i < world.random.nextInt(35) + 10; ++i) {
                new ParticleUtil(ParticleTypes.DRAGON_BREATH, entity.getX(), entity.getEyeY(), entity.getZ(), 0.0F, 0.0F, 0.0F);
            }
            if (entity instanceof CowEntity){
                MutatedCowEntity mutatedCowEntity = new MutatedCowEntity(ModEntityType.MUTATED_COW.get(), world);
                mutatedCowEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedCowEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedCowEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedCowEntity.setCustomName(entity.getCustomName());
                    mutatedCowEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedCowEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedCowEntity);
                entity.remove();
            } else if (entity instanceof ChickenEntity){
                MutatedChickenEntity mutatedChickenEntity = new MutatedChickenEntity(ModEntityType.MUTATED_CHICKEN.get(), world);
                mutatedChickenEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedChickenEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedChickenEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedChickenEntity.setCustomName(entity.getCustomName());
                    mutatedChickenEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedChickenEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedChickenEntity);
                entity.remove();
            } else if (entity instanceof SheepEntity){
                MutatedSheepEntity mutatedSheepEntity = new MutatedSheepEntity(ModEntityType.MUTATED_SHEEP.get(), world);
                mutatedSheepEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedSheepEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedSheepEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedSheepEntity.setCustomName(entity.getCustomName());
                    mutatedSheepEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedSheepEntity.setColor(((SheepEntity) entity).getColor());
                mutatedSheepEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedSheepEntity);
                entity.remove();
            } else if (entity instanceof PigEntity){
                MutatedPigEntity mutatedPigEntity = new MutatedPigEntity(ModEntityType.MUTATED_PIG.get(), world);
                mutatedPigEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedPigEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedPigEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedPigEntity.setCustomName(entity.getCustomName());
                    mutatedPigEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedPigEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedPigEntity);
                entity.remove();
            } else if (entity instanceof RabbitEntity){
                RabbitEntity rabbit = (RabbitEntity) entity;
                MutatedRabbitEntity mutatedRabbitEntity = new MutatedRabbitEntity(ModEntityType.MUTATED_RABBIT.get(), world);
                mutatedRabbitEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedRabbitEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedRabbitEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedRabbitEntity.setCustomName(entity.getCustomName());
                    mutatedRabbitEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                if (rabbit.getRabbitType() != 99) {
                    mutatedRabbitEntity.setRabbitType(rabbit.getRabbitType());
                } else {
                    mutatedRabbitEntity.setRabbitType(1);
                }
                mutatedRabbitEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedRabbitEntity);
                entity.remove();
            }
        }
    }

}
