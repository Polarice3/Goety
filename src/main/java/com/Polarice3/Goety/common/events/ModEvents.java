package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.common.items.SoulWand;
import com.Polarice3.Goety.init.ModRegistryHandler;
import com.Polarice3.Goety.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
    public static void openBagandWand(InputEvent.KeyInputEvent event){
        KeyPressed.setWand(ModRegistryHandler.keyBindings[0].isDown());
        KeyPressed.setWandandbag(ModRegistryHandler.keyBindings[1].isDown());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getBiomeCategory() == Biome.Category.NETHER) {

                } else if (biome.getBiomeCategory() == Biome.Category.THEEND) {

                } else {
                    if (biome.getBiomeCategory() == Biome.Category.OCEAN) {
                        event.getSpawns().getSpawner(EntityClassification.MISC).add(new MobSpawnInfo.Spawners(ModEntityType.SACRED_FISH.get(), 1, 1, 1));
                    }
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

    private static final Map<ServerWorld, CultistsSpawner> CULTISTS_SPAWNER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            CULTISTS_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new CultistsSpawner());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            CULTISTS_SPAWNER_MAP.remove(evt.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            CultistsSpawner spawner2 = CULTISTS_SPAWNER_MAP.get(serverWorld);
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
                event.getPlayer().addItem(new ItemStack(ModRegistryHandler.GOLDTOTEM.get()));
                data.putBoolean("goety:gotTotem", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
            }
        }
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
            if (player.isInWater() && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate.getType())){
                player.setDeltaMovement(player.getDeltaMovement().x * 1.0175, player.getDeltaMovement().y, player.getDeltaMovement().z * 1.0175);
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
               player.setDeltaMovement(player.getDeltaMovement().x, 0.625, player.getDeltaMovement().z);
            }
        }

    }

    @SubscribeEvent
    public static void TargetSelection(LivingSetAttackTargetEvent event){
        if (event.getEntityLiving() instanceof SpiderEntity){
            if (event.getTarget() != null) {
                if (RobeArmorFinder.FindArachnoHelm(event.getTarget()) && RobeArmorFinder.FindArachnoArmor(event.getTarget())) {
                    ((SpiderEntity) event.getEntityLiving()).setTarget(null);
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity.hasEffect(ModRegistryHandler.CURSED.get())){
            EffectInstance effectInstance = entity.getEffect(ModRegistryHandler.CURSED.get());
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
            if (((CreatureEntity) killed).hasEffect(ModRegistryHandler.GOLDTOUCHED.get())){
                int amp = Objects.requireNonNull(((CreatureEntity) killed).getEffect(ModRegistryHandler.GOLDTOUCHED.get())).getAmplifier() + 1;
                for(int i = 0; i < killed.level.random.nextInt(4) + amp * amp; ++i) {
                    killed.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                }
            }
        }
        if (killed instanceof BatEntity){
            if (killer instanceof LivingEntity) {
                if (RobeArmorFinder.FindArachnoHelm((LivingEntity) killer) && RobeArmorFinder.FindArachnoArmor((LivingEntity) killer)) {
                    killed.spawnAtLocation(new ItemStack(ModRegistryHandler.DEADBAT.get()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void CosmicExpDrop(LivingExperienceDropEvent event){
        if (event.getAttackingPlayer() != null) {
            if (event.getAttackingPlayer().hasEffect(ModRegistryHandler.COSMIC.get())) {
                int a = Objects.requireNonNull(event.getAttackingPlayer().getEffect(ModRegistryHandler.COSMIC.get())).getAmplifier() + 2;
                int a1 = MathHelper.clamp(a, 2, 8);
                event.setDroppedExperience(event.getDroppedExperience() * a1);
            }
        }
    }

    @SubscribeEvent
    public static void AiryFocus(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == Effects.LEVITATION){
            if (event.getEntityLiving().getMainHandItem().getItem() == ModRegistryHandler.EMPTYCORE.get()){
                event.getEntityLiving().getMainHandItem().setCount(0);
                event.getEntityLiving().setItemInHand(Hand.MAIN_HAND, new ItemStack(ModRegistryHandler.AIRYCORE.get()));
            }
            if (event.getEntityLiving().getOffhandItem().getItem() == ModRegistryHandler.EMPTYCORE.get()){
                event.getEntityLiving().getOffhandItem().setCount(0);
                event.getEntityLiving().setItemInHand(Hand.OFF_HAND, new ItemStack(ModRegistryHandler.AIRYCORE.get()));
            }
        }
    }

    @SubscribeEvent
    public static void Mutation(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == ModRegistryHandler.COSMIC.get()){
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
