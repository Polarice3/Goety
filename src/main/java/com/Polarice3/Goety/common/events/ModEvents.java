package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.blocks.ArcaBlock;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.BoomerEntity;
import com.Polarice3.Goety.common.entities.hostile.DuneSpiderEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.ChannellerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.entities.utilities.StormEntity;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyProvider;
import com.Polarice3.Goety.common.items.FocusBagItem;
import com.Polarice3.Goety.common.items.SoulWand;
import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.common.lichdom.LichProvider;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.soulenergy.SEProvider;
import com.Polarice3.Goety.common.spider.SpiderLevelsProvider;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.init.*;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
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
    }

    @SubscribeEvent
    public static void KeyInputs(InputEvent.KeyInputEvent event){
        KeyPressed.setWand(ModKeybindings.keyBindings[0].isDown());
        KeyPressed.setWandandbag(ModKeybindings.keyBindings[1].isDown());
        KeyPressed.setBag(ModKeybindings.keyBindings[2].isDown());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getBiomeCategory() == Biome.Category.OCEAN) {
                    event.getSpawns().getSpawner(EntityClassification.MISC).add(new MobSpawnInfo.Spawners(ModEntityType.SACRED_FISH.get(), 1, 1, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderSoulEnergyHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        final PlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            if (SEHelper.getSEActive(player)){
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack());
            } else if (!GoldTotemFinder.FindTotem(player).isEmpty()) {
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack());
            }
        }
    }

    private static final Map<ServerWorld, IllagerSpawner> ILLAGER_SPAWNER_MAP = new HashMap<>();
    private static final Map<ServerWorld, IllagueEvent> ILLAGUE_EVENT_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new IllagerSpawner());
            ILLAGUE_EVENT_MAP.put((ServerWorld) evt.getWorld(), new IllagueEvent());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.remove(evt.getWorld());
            ILLAGUE_EVENT_MAP.remove(evt.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            IllagueEvent spawner = ILLAGUE_EVENT_MAP.get(serverWorld);
            IllagerSpawner spawner2 = ILLAGER_SPAWNER_MAP.get(serverWorld);
            if (spawner != null){
                spawner.tick(serverWorld);
            }
            if (spawner2 != null){
                spawner2.tick(serverWorld);
            }

        }

    }

    @SubscribeEvent
    public static void onPlayerFirstEntersWorld(PlayerEvent.PlayerLoggedInEvent event){
        if (MainConfig.StarterTotem.get() && !event.getPlayer().level.isClientSide) {
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data;

            if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                data = new CompoundNBT();
            } else {
                data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            }

            if (!data.getBoolean("goety:gotTotem")) {
                event.getPlayer().addItem(new ItemStack(ModItems.GOLDTOTEM.get()));
                data.putBoolean("goety:gotTotem", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
            }
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
            if (MainConfig.DeadSandSpread.get()) {
                if (livingEntity instanceof CreeperEntity) {
                    BlockState blockState = livingEntity.level.getBlockState(livingEntity.blockPosition().below());
                    if (blockState.getBlock() instanceof IDeadBlock) {
                        if (livingEntity.tickCount % 20 == 0) {
                            livingEntity.hurt(DamageSource.DRY_OUT, 5);
                        }
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
                new SoundUtil(event.getPos(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        if (KeyPressed.openWandandBag() && player.getMainHandItem().getItem() instanceof SoulWand){
            SoulWand.BagonKeyPressed(player.getMainHandItem(), player);
        }
        if (KeyPressed.openWand() && player.getMainHandItem().getItem() instanceof SoulWand){
            SoulWand.onKeyPressed(player.getMainHandItem(), player);
        }
        if (KeyPressed.openBag() && FocusBagFinder.findBag(player) != ItemStack.EMPTY){
            FocusBagItem.onKeyPressed(FocusBagFinder.findBag(player), player);
        }
        ISoulEnergy soulEnergy = SEHelper.getCapability(player);
        if (!soulEnergy.getSEActive() && soulEnergy.getSoulEnergy() > 0) {
            if (!world.isClientSide()){
                player.addEffect(new EffectInstance(Effects.WEAKNESS, 60));
                player.addEffect(new EffectInstance(Effects.HUNGER, 60));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60));
                if (player.tickCount % 5 == 0) {
                    SEHelper.decreaseSESouls(player, 1);
                    SEHelper.sendSEUpdatePacket(player);
                }
            }
        }
        if (soulEnergy.getArcaBlock() != null){
            if (soulEnergy.getArcaBlockDimension() == world.dimension()) {
                if (!world.isClientSide()){
                    BlockPos blockPos = soulEnergy.getArcaBlock();
                    TileEntity tileEntity = world.getBlockEntity(blockPos);
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (arcaTile.getPlayer() == player) {
                            Random pRand = world.random;
                            if (pRand.nextInt(12) == 0) {
                                for (int i = 0; i < 3; ++i) {
                                    int j = pRand.nextInt(2) * 2 - 1;
                                    int k = pRand.nextInt(2) * 2 - 1;
                                    double d0 = (double) blockPos.getX() + 0.5D + 0.25D * (double) j;
                                    double d1 = (float) blockPos.getY() + pRand.nextFloat();
                                    double d2 = (double) blockPos.getZ() + 0.5D + 0.25D * (double) k;
                                    double d3 = pRand.nextFloat() * (float) j;
                                    double d4 = ((double) pRand.nextFloat() - 0.5D) * 0.125D;
                                    double d5 = pRand.nextFloat() * (float) k;
                                    new ParticleUtil(ParticleTypes.ENCHANT, d0, d1, d2, d3, d4, d5);
                                }
                            }
                            if (!soulEnergy.getSEActive()) {
                                soulEnergy.setSEActive(true);
                                SEHelper.sendSEUpdatePacket(player);
                            }
                        } else {
                            if (soulEnergy.getSEActive()) {
                                soulEnergy.setSEActive(false);
                                SEHelper.sendSEUpdatePacket(player);
                            }
                        }
                    } else {
                        if (soulEnergy.getSEActive()) {
                            soulEnergy.setSEActive(false);
                            SEHelper.sendSEUpdatePacket(player);
                        }
                    }
                }
            } else if (soulEnergy.getArcaBlockDimension() == null){
                soulEnergy.setArcaBlockDimension(world.dimension());
                SEHelper.sendSEUpdatePacket(player);
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
        if (player.hasEffect(ModEffects.LAUNCH.get())){
            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.75, 0));
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
        if (RobeArmorFinder.FindArachnoHelm(player) && !LichdomHelper.isLich(player)) {
            if (!world.isClientSide()) {
                if (world.isDay()) {
                    float f = player.getBrightness();
                    BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                    if (f > 0.5F && world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(blockpos)) {
                        player.addEffect(new EffectInstance(Effects.BLINDNESS, 100, 0, false, false));
                    } else {
                        player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 600, 0, false, false));
                    }
                } else {
                    player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 600, 0, false, false));
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
        if (RobeArmorFinder.FindArachnoArmor(player)){
            BlockFinder.WebMovement(player);
        }
        if (RobeArmorFinder.FindArachnoBootsofWander(player)){
            BlockFinder.ClimbAnyWall(player);
        }
        IInfamy infamy = InfamyHelper.getCapability(player);
        int i = infamy.getInfamy();
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
            if (RobeArmorFinder.FindBootsofWander(player)){
                event.setDistance(event.getDistance()/2);
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
        if (event.getEntityLiving() instanceof SpiderEntity){
            if (event.getTarget() != null) {
                if (RobeArmorFinder.FindArachnoHelm(event.getTarget())) {
                    ((SpiderEntity) event.getEntityLiving()).setTarget(null);
                }
            }
        }
        if (event.getEntityLiving() instanceof MonsterEntity){
            if (event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD || event.getEntityLiving() instanceof CreeperEntity){
                if (event.getTarget() != null){
                    if (event.getTarget() instanceof ApostleEntity){
                        ((MonsterEntity) event.getEntityLiving()).setTarget(null);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity.hasEffect(ModEffects.CURSED.get())){
            EffectInstance effectInstance = entity.getEffect(ModEffects.CURSED.get());
            assert effectInstance != null;
            int i = effectInstance.getAmplifier() + 1;
            event.setAmount(event.getAmount() * (1.0F + i));
        }
        if (RobeArmorFinder.FindArachnoArmor(entity)){
            if (event.getSource().getEntity() != null) {
                BlockPos blockpos = entity.blockPosition();
                SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), entity.level);
                summonedentity.setOwnerId(entity.getUUID());
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setLimitedLife(180);
                entity.level.addFreshEntity(summonedentity);
            }
            if (event.getSource().getEntity() instanceof CreeperlingMinionEntity){
                CreeperlingMinionEntity creeperlingMinion = (CreeperlingMinionEntity) event.getSource().getEntity();
                if (creeperlingMinion.getTrueOwner() == entity){
                    event.setAmount(0);
                }
            }
        }
        if (event.getSource().getEntity() instanceof FangEntity){
            FangEntity fangEntity = (FangEntity) event.getSource().getEntity();
            if (fangEntity.getOwner() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) fangEntity.getOwner();
                if (CuriosFinder.findCurio(player).getItem() == ModItems.VAMPIRIC_AMULET.get()) {
                    player.heal(event.getAmount());
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
        if (event.getSource().getEntity() instanceof SummonedEntity){
            SummonedEntity summonedEntity = (SummonedEntity) event.getSource().getEntity();
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
            if (RobeArmorFinder.FindNecroHelm(entity)){
                if (looker.getMobType() == CreatureAttribute.UNDEAD){
                    event.modifyVisibility(0.5);
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
        if (MainConfig.DeadSandSpread.get()) {
            if (killed instanceof CreeperEntity) {
                if (event.getSource() == DamageSource.DRY_OUT) {
                    BlockState blockState = killed.level.getBlockState(killed.blockPosition().below());
                    if (blockState.getBlock() instanceof IDeadBlock) {
                        BoomerEntity boomer = ((CreeperEntity) killed).convertTo(ModEntityType.BOOMER.get(), false);
                        if (boomer != null) {
                            boomer.finalizeSpawn((IServerWorld) boomer.level, boomer.level.getCurrentDifficultyAt(boomer.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                            net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) killed, boomer);
                            new SoundUtil(boomer.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        }
                    }
                }
            }
            if (killed instanceof SpiderEntity){
                if (event.getSource() == DamageSource.CACTUS){
                    if (((SpiderEntity) killed).hasEffect(ModEffects.CURSED.get())){
                        DuneSpiderEntity duneSpiderEntity = ((SpiderEntity) killed).convertTo(ModEntityType.DUNE_SPIDER.get(), false);
                        if (duneSpiderEntity != null) {
                            duneSpiderEntity.finalizeSpawn((IServerWorld) duneSpiderEntity.level, duneSpiderEntity.level.getCurrentDifficultyAt(duneSpiderEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                            net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) killed, duneSpiderEntity);
                            new SoundUtil(duneSpiderEntity.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        if (killed instanceof BatEntity){
            if (killer instanceof LivingEntity) {
                if (RobeArmorFinder.FindArachnoSet((LivingEntity) killer)) {
                    if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                        killed.spawnAtLocation(new ItemStack(ModItems.DEADBAT.get()));
                    }
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
            if (killer instanceof SummonedEntity) {
                SummonedEntity summonedEntity = (SummonedEntity) killer;
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
            int r1 = world.random.nextInt(4);
            int r2 = world.random.nextInt(16);
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)){
                if (killed instanceof LivingEntity){
                    LivingEntity killed1 = (LivingEntity) killed;
                    if (killed1.getMobType() != CreatureAttribute.UNDEAD) {
                        if (killed1 instanceof AbstractVillagerEntity || killed1 instanceof SpellcastingIllagerEntity || killed1 instanceof ChannellerEntity) {
                            if (r1 == 0) {
                                killed1.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
                            }
                        } else if (killed1 instanceof PatrollerEntity) {
                            if (r2 == 0) {
                                killed1.spawnAtLocation(new ItemStack(ModItems.BRAIN.get()));
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
    public static void PotionAddedEvents(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == Effects.HERO_OF_THE_VILLAGE){
            if (event.getEntityLiving() instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if (!GoldTotemFinder.FindTotem(player).isEmpty()){
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
