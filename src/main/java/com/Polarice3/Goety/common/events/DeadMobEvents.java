package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.DeadPileBlock;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.hostile.dead.*;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeadMobEvents {

    @SubscribeEvent
    public static void SpawnEvents(LivingSpawnEvent.CheckSpawn event){
        if (MainConfig.DeadSandMobs.get()) {
            if (event.getEntityLiving() instanceof IMob && event.getEntityLiving() instanceof MobEntity) {
                MobEntity mob = (MobEntity) event.getEntityLiving();
                if (event.getWorld().getBlockState(mob.blockPosition().below()).is(ModTags.Blocks.DEAD_SANDS)) {
                    if (event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION) {
                        MobUtil.deadSandConvert(mob, true);
                    } else {
                        if (event.getWorld().getRandom().nextFloat() < 0.55F) {
                            MobUtil.deadSandConvert(mob, true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingEvents(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.level;
        List<BlockState> result = new ArrayList<>();
        if (MainConfig.DeadSandMobs.get()) {
            if (livingEntity instanceof CreeperEntity || livingEntity instanceof SnowGolemEntity || livingEntity instanceof DrownedEntity) {
                if (BlockFinder.isDeadBlock(world, livingEntity.blockPosition())) {
                    if (livingEntity.tickCount % 20 == 0) {
                        livingEntity.hurt(ModDamageSource.DESICCATE, livingEntity.getMaxHealth()/4);
                    }
                }
            }
        }
        if (livingEntity.isEyeInFluid(ModTags.Fluids.QUICKSAND)){
            if (!(livingEntity instanceof IDeadMob)){
                if (livingEntity.tickCount % 20 == 0) {
                    livingEntity.hurt(ModDamageSource.DESICCATE, 2.0F);
                }
            }
        }
        if (livingEntity.hasEffect(ModEffects.DESICCATE.get())){
            int amp = livingEntity.getEffect(ModEffects.DESICCATE.get()).getAmplifier() + 1;
            if (livingEntity.isUnderWater()){
                if (livingEntity.hurt(DamageSource.DROWN, 2.0F * amp)){
                    livingEntity.removeEffect(ModEffects.DESICCATE.get());
                }
                livingEntity.setAirSupply(0);

            }
        }
        if (livingEntity instanceof IDeadMob){
            if (BlockFinder.isInRain(world, livingEntity.blockPosition()) && !BlockFinder.isDeadBlock(world, livingEntity.blockPosition())){
                if (livingEntity.tickCount % 20 == 0){
                    livingEntity.hurt(DamageSource.DROWN, 2.0F);
                }
            }
            if (livingEntity.isInWater()){
                if (livingEntity.tickCount % 20 == 0){
                    livingEntity.hurt(DamageSource.DROWN, 2.0F);
                }
            }
            int radius = livingEntity instanceof LocustEntity ? 8 : 4;
            BlockState blockState;
            Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                    livingEntity.blockPosition().offset(-radius, -radius, -radius),
                    livingEntity.blockPosition().offset(radius, radius, radius));
            for (BlockPos blockToCheck : blocksToCheck) {
                blockState = world.getBlockState(blockToCheck);
                if (blockState.getBlock() instanceof IDeadBlock) {
                    result.add(blockState);
                }
            }
            if (livingEntity.tickCount % 20 == 0) {
                if (result.size() < 4) {
                    if (world.random.nextFloat() < 0.5F) {
                        livingEntity.hurt(DamageSource.STARVE, 2.0F);
                        livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
                        livingEntity.addEffect(new EffectInstance(ModEffects.SAPPED.get(), 20, 1));
                    }
                } else {
                    if (world.random.nextFloat() < 0.5F) {
                        livingEntity.heal(1.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvents(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        if (victim != null) {
            if (event.getSource().getEntity() instanceof IDeadMob) {
                if (event.getSource().getMsgId().equals("mob")) {
                    IDeadMob deadMob = (IDeadMob) event.getSource().getEntity();
                    deadMob.desiccateTarget(victim);
                }
            }
            if (ModDamageSource.desiccateAttacks(event.getSource())) {
                if (victim instanceof IDeadMob) {
                    event.setCanceled(true);
                } else {
                    victim.playSound(SoundEvents.STONE_BREAK, 1.5F, 1.0F);
                    if (!victim.level.isClientSide) {
                        ServerWorld serverWorld = (ServerWorld) victim.level;
                        IParticleData particleData = new BlockParticleData(ParticleTypes.BLOCK, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                        ServerParticleUtil.emitterParticles(serverWorld, victim, particleData);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void DamageEvents(LivingDamageEvent event){
        LivingEntity victim = event.getEntityLiving();
        if (victim instanceof IDeadMob){
            if (ModDamageSource.desiccateAttacks(event.getSource())){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void TargetEvents(LivingSetAttackTargetEvent event){
        if (event.getEntityLiving() instanceof WitherEntity){
            if (event.getTarget() instanceof IDeadMob){
                ((MobEntity) event.getEntityLiving()).setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void DeathEvents(LivingDeathEvent event){
        Entity killed = event.getEntity();
        World world = killed.getCommandSenderWorld();
        if (MainConfig.DeadSandSpread.get() && world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            if (ModDamageSource.desiccateAttacks(event.getSource()) && MobUtil.nonDesiccateExplodeEntities(killed)){
                ExplosionUtil.deadSandExplode(world, killed, killed.getX(), killed.getY(), killed.getZ(), 1.0F, DeadSandExplosion.Mode.SPREAD);
            }
        }
        if (MainConfig.DeadSandMobs.get()){
            if (killed instanceof MobEntity) {
                if (ModDamageSource.desiccateAttacks(event.getSource())) {
                    MobUtil.deadSandConvert(killed, false);
                }
            }
        }
        if (killed instanceof IDeadMob){
            LivingEntity deadMob = (LivingEntity) killed;
            BlockState blockstate = ModBlocks.DEAD_PILE.get().defaultBlockState();
            if (deadMob.level.random.nextFloat() <= 0.25F){
                if (deadMob.getMaxHealth() >= 20.0F) {
                    for (int l = 0; l < 4; ++l) {
                        int i = MathHelper.floor(deadMob.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                        int j = MathHelper.floor(deadMob.getY());
                        int k = MathHelper.floor(deadMob.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                        BlockPos blockpos = new BlockPos(i, j, k);
                        if (deadMob.level.getBlockState(blockpos).getBlock() == ModBlocks.DEAD_PILE.get()){
                            int i1 = deadMob.level.getBlockState(blockpos).getValue(DeadPileBlock.LAYERS);
                            if (i1 < 8){
                                deadMob.level.setBlockAndUpdate(blockpos, blockstate.setValue(DeadPileBlock.LAYERS, blockstate.getValue(DeadPileBlock.LAYERS) + 1));
                            }
                        } else if (deadMob.level.isEmptyBlock(blockpos) && blockstate.canSurvive(deadMob.level, blockpos)) {
                            deadMob.level.setBlockAndUpdate(blockpos, blockstate);
                        }
                    }
                } else {
                    BlockPos blockpos = deadMob.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, deadMob.blockPosition());
                    if (deadMob.level.isEmptyBlock(blockpos) && blockstate.canSurvive(deadMob.level, blockpos)) {
                        deadMob.level.setBlockAndUpdate(blockpos, blockstate);
                    }
                }
            }
            if (deadMob.isInWater() && !(deadMob instanceof BoomerEntity)){
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(world, deadMob.getX(), deadMob.getY(), deadMob.getZ());
                areaeffectcloudentity.setRadius(2.5F);
                areaeffectcloudentity.setRadiusOnUse(-0.5F);
                areaeffectcloudentity.setWaitTime(10);
                areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
                areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());
                areaeffectcloudentity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
                world.addFreshEntity(areaeffectcloudentity);
            }
        }
    }

    @SubscribeEvent
    public static void DropEvents(LivingDropsEvent event){
        LivingEntity living = event.getEntityLiving();
        if (ModDamageSource.desiccateAttacks(event.getSource())
                || event.getSource().getEntity() instanceof IDeadMob
                || living instanceof IDeadMob){
            for (ItemEntity itemEntity : event.getDrops()){
                if (itemEntity.getItem().getItem().getFoodProperties() != null) {
                    if (itemEntity.getItem().getItem().getFoodProperties().getEffects().isEmpty()) {
                        itemEntity.setItem(new ItemStack(Items.ROTTEN_FLESH));
                    }
                }
            }
        }
        if (living instanceof IDeadMob){
            if (living.level.getServer() != null) {
                if (living instanceof MarcireEntity || living instanceof BlightEntity) {
                    LootTable loottable = living.level.getServer().getLootTables().get(ModLootTables.DEAD_MOBS_2);
                    LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), living);
                    LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                    loottable.getRandomItems(ctx).forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                } else if (!(living instanceof BlightlingEntity)) {
                    LootTable loottable = living.level.getServer().getLootTables().get(ModLootTables.DEAD_MOBS);
                    LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), living);
                    LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                    loottable.getRandomItems(ctx).forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplicableEvents(PotionEvent.PotionApplicableEvent event){
        if (event.getPotionEffect().getEffect() == ModEffects.DESICCATE.get()){
            if (event.getEntityLiving() instanceof IDeadMob){
                event.setResult(Event.Result.DENY);
            }
            if (event.getEntityLiving() instanceof PlayerEntity){
                if (LichdomHelper.isLich((PlayerEntity) event.getEntityLiving())){
                    event.setResult(Event.Result.DENY);
                }
            }
            if (event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD){
                event.setResult(Event.Result.DENY);
            }
            if (event.getEntityLiving().isSensitiveToWater()){
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
