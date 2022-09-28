package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.hostile.dead.BoomerEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.MarcireEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
            if (event.getEntityLiving() instanceof MonsterEntity) {
                MonsterEntity monster = (MonsterEntity) event.getEntityLiving();
                if (event.getWorld().getBlockState(monster.blockPosition().below()).is(ModTags.Blocks.DEAD_SANDS)) {
                    if (event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION) {
                        MobUtil.deadSandConvert(monster, true);
                    } else {
                        if (event.getWorld().getRandom().nextFloat() < 0.55F) {
                            MobUtil.deadSandConvert(monster, true);
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
            if (livingEntity instanceof CreeperEntity) {
                if (BlockFinder.isDeadBlock(world, livingEntity.blockPosition())) {
                    if (livingEntity.tickCount % 20 == 0) {
                        livingEntity.hurt(ModDamageSource.DESICCATE, livingEntity.getMaxHealth()/4);
                    }
                }
            }
        }
        if (livingEntity instanceof DrownedEntity) {
            BlockState blockState = livingEntity.level.getBlockState(livingEntity.blockPosition().below());
            if (blockState.getBlock() instanceof IDeadBlock) {
                if (livingEntity.tickCount % 20 == 0) {
                    livingEntity.hurt(ModDamageSource.DESICCATE, livingEntity.getMaxHealth()/4);
                }
            }
        }
        if (livingEntity instanceof IDeadMob){
            if (BlockFinder.isWet(world, livingEntity.blockPosition()) && !BlockFinder.isDeadBlock(world, livingEntity.blockPosition())){
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
        if (MainConfig.DeadSandSpread.get()) {
            if (ModDamageSource.desiccateAttacks(event.getSource()) && MobUtil.nonDesiccateExplodeEntities(killed)){
                ExplosionUtil.deadSandExplode(world, killed, killed.getX(), killed.getY(), killed.getZ(), 1.0F, DeadSandExplosion.Mode.SPREAD);
            }
        }
        if (MainConfig.DeadSandMobs.get()){
            if (killed instanceof MonsterEntity) {
                if (ModDamageSource.desiccateAttacks(event.getSource())) {
                    MobUtil.deadSandConvert(killed, false);
                }
            }
        }
        if (killed instanceof IDeadMob){
            LivingEntity deadMob = (LivingEntity) killed;
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity){
                LivingEntity killer = (LivingEntity) event.getSource().getEntity();
                LootTable loottable = killer.level.getServer().getLootTables().get(ModLootTables.DEAD_MOBS);
                LootContext.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), deadMob);
                LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                loottable.getRandomItems(ctx).forEach(deadMob::spawnAtLocation);
                if (deadMob instanceof MarcireEntity){
                    float chance = 0.025F;
                    chance += (float) EnchantmentHelper.getMobLooting(killer)/100;
                    if (deadMob.getRandom().nextFloat() < chance){
                        deadMob.spawnAtLocation(new ItemStack(ModItems.FORBIDDEN_FRAGMENT.get()));
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
    public static void PotionApplicableEvents(PotionEvent.PotionApplicableEvent event){
        if (event.getPotionEffect().getEffect() == ModEffects.DESICCATE.get()){
            if (event.getEntityLiving() instanceof IDeadMob){
                event.setResult(Event.Result.DENY);
            }
            if (event.getEntityLiving() instanceof PlayerEntity){
                if (!MobUtil.playerValidity((PlayerEntity) event.getEntityLiving(), true)){
                    event.setResult(Event.Result.DENY);
                }
            }
            if (event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD){
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
