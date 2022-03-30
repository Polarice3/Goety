package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.ApostleEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyProvider;
import com.Polarice3.Goety.common.items.SoulWand;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
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
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerCapabilityAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Goety.MOD_ID, "infamy"), new InfamyProvider());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && !event.getWorld().isClientSide()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                InfamyHelper.sendInfamyUpdatePacket(player);
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
        }
    }

    @SubscribeEvent
    public static void openBagandWand(InputEvent.KeyInputEvent event){
        KeyPressed.setWand(ModRegistry.keyBindings[0].isDown());
        KeyPressed.setWandandbag(ModRegistry.keyBindings[1].isDown());
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
            if (!GoldTotemFinder.FindTotem(player).isEmpty()) {
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack(), event.getPartialTicks());
            }
        }
    }

    private static final Map<ServerWorld, IllagerSpawner> ILLAGER_SPAWNER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new IllagerSpawner());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            ILLAGER_SPAWNER_MAP.remove(evt.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            IllagerSpawner spawner2 = ILLAGER_SPAWNER_MAP.get(serverWorld);
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
                event.getPlayer().addItem(new ItemStack(ModRegistry.GOLDTOTEM.get()));
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
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                World world = player.level;
                if (RobeArmorFinder.FindNecroSet(player)) {
                    BlockState blockState = player.level.getBlockState(player.blockPosition().below());
                    if (!(blockState.getBlock() instanceof IDeadBlock)) {
                        if (!world.isClientSide && world.isDay()) {
                            float f = player.getBrightness();
                            BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                            if (f > 0.5F && world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(blockpos)) {
                                player.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 1));
                                player.addEffect(new EffectInstance(Effects.HUNGER, 100, 1));
                            }
                        }
                    } else {
                        if (player.hasEffect(Effects.WEAKNESS)) {
                            player.removeEffect(Effects.WEAKNESS);
                        }
                        if (player.hasEffect(Effects.HUNGER)) {
                            player.removeEffect(Effects.HUNGER);
                        }
                    }
                }
                if (RobeArmorFinder.FindArachnoSet(player)) {
                    if (!world.isClientSide) {
                        if (world.isDay()) {
                            float f = player.getBrightness();
                            BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                            if (f > 0.5F && world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(blockpos)) {
                                player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 0, false, false));
                            } else {
                                player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 600, 0, false, false));
                            }
                        } else {
                            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 600, 0, false, false));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.hasEffect(ModRegistry.NOMINE.get())){
            if (event.getState().getMaterial() == Material.STONE && !(event.getState().getBlock() == ModRegistry.GUARDIAN_OBELISK.get())){
                new SoundUtil(event.getPos(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlacingBlock(BlockEvent.EntityPlaceEvent event){
    }

    @SubscribeEvent
    public static void onPlayerEquipment(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (KeyPressed.openWandandBag() && player.getMainHandItem().getItem() instanceof SoulWand){
            SoulWand.BagonKeyPressed(player.getMainHandItem(), player);
        }
        if (KeyPressed.openWand() && player.getMainHandItem().getItem() instanceof SoulWand){
            SoulWand.onKeyPressed(player.getMainHandItem(), player);
        }
        if (RobeArmorFinder.FindBootsofWander(player)){
            FluidState fluidstate = player.level.getFluidState(player.blockPosition());
            if (player.isInWater() && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate.getType()) && !player.hasEffect(Effects.DOLPHINS_GRACE)){
                player.setDeltaMovement(player.getDeltaMovement().x * 1.0175, player.getDeltaMovement().y, player.getDeltaMovement().z * 1.0175);
            }
        }
        if (player.hasEffect(ModRegistry.LAUNCH.get())){
            player.setDeltaMovement(player.getDeltaMovement().add(0, 1, 0));
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
                if (RobeArmorFinder.FindArachnoSet(event.getTarget())) {
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
        if (entity.hasEffect(ModRegistry.CURSED.get())){
            EffectInstance effectInstance = entity.getEffect(ModRegistry.CURSED.get());
            assert effectInstance != null;
            int i = effectInstance.getAmplifier() + 1;
            event.setAmount(event.getAmount() * 0.5F + i);
        }
        if (RobeArmorFinder.FindArachnoArmor(entity) && event.getSource().getEntity() != null){
            BlockPos blockpos = entity.blockPosition();
            SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), entity.level);
            summonedentity.setOwnerId(entity.getUUID());
            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
            summonedentity.setLimitedLife(180);
            entity.level.addFreshEntity(summonedentity);
        }
    }

    @SubscribeEvent
    public static void SpecialDeath(LivingDeathEvent event){
        Entity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        if (killed instanceof CreatureEntity){
            if (((CreatureEntity) killed).hasEffect(ModRegistry.GOLDTOUCHED.get())){
                int amp = Objects.requireNonNull(((CreatureEntity) killed).getEffect(ModRegistry.GOLDTOUCHED.get())).getAmplifier() + 1;
                for(int i = 0; i < killed.level.random.nextInt(4) + amp * amp; ++i) {
                    killed.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                }
            }
        }
        if (killed instanceof BatEntity){
            if (killer instanceof LivingEntity) {
                if (RobeArmorFinder.FindArachnoSet((LivingEntity) killer)) {
                    killed.spawnAtLocation(new ItemStack(ModRegistry.DEADBAT.get()));
                }
            }
        }
        if (killed instanceof AbstractIllagerEntity){
            if (killer instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) killer;
                if (!GoldTotemFinder.FindTotem(player).isEmpty() || RobeArmorFinder.FindArmor(player)){
                    if (killed instanceof PillagerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.PillagerInfamy.get());
                    } else
                    if (killed instanceof VindicatorEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.VindicatorInfamy.get());
                    } else
                    if (killed instanceof EvokerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.EvokerInfamy.get());
                    } else
                    if (killed instanceof IllusionerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.IllusionerInfamy.get());
                    } else
                    if (killed instanceof EnviokerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.EnviokerInfamy.get());
                    } else
                    if (killed instanceof InquillagerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.InquillagerInfamy.get());
                    } else
                    if (killed instanceof ConquillagerEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.ConquillagerInfamy.get());
                    } else
                    if (killed instanceof VizierEntity){
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.VizierInfamy.get());
                    } else {
                        InfamyHelper.increaseInfamy((PlayerEntity) killer, MainConfig.OtherInfamy.get());
                    }
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
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void ExtraExpDrop(LivingExperienceDropEvent event){
        if (event.getAttackingPlayer() != null) {
            if (event.getAttackingPlayer().hasEffect(ModRegistry.COSMIC.get())) {
                int a = Objects.requireNonNull(event.getAttackingPlayer().getEffect(ModRegistry.COSMIC.get())).getAmplifier() + 2;
                int a1 = MathHelper.clamp(a, 2, 8);
                event.setDroppedExperience(event.getDroppedExperience() * a1);
            }
        }
        if (event.getEntityLiving().hasEffect(ModRegistry.NECROPOWER.get())){
            event.setDroppedExperience(event.getDroppedExperience() * 2);
        }
    }

    @SubscribeEvent
    public static void PotionAddedEvents(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == Effects.LEVITATION){
            if (event.getEntityLiving().getMainHandItem().getItem() == ModRegistry.EMPTYCORE.get()){
                event.getEntityLiving().getMainHandItem().setCount(0);
                event.getEntityLiving().setItemInHand(Hand.MAIN_HAND, new ItemStack(ModRegistry.AIRYCORE.get()));
            }
            if (event.getEntityLiving().getOffhandItem().getItem() == ModRegistry.EMPTYCORE.get()){
                event.getEntityLiving().getOffhandItem().setCount(0);
                event.getEntityLiving().setItemInHand(Hand.OFF_HAND, new ItemStack(ModRegistry.AIRYCORE.get()));
            }
        }
        if (event.getPotionEffect().getEffect() == Effects.HERO_OF_THE_VILLAGE){
            if (event.getEntityLiving() instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if (!GoldTotemFinder.FindTotem(player).isEmpty()){
                    InfamyHelper.increaseInfamy(player, 100);
                }
            }
        }
    }

    @SubscribeEvent
    public static void Mutation(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == ModRegistry.COSMIC.get()){
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
