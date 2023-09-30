package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LichEvents {

    @SubscribeEvent
    public static void onPlayerLichdom(TickEvent.PlayerTickEvent event){
        List<BlockState> result = new ArrayList<>();
        PlayerEntity player = event.player;
        World world = player.level;
        if (LichdomHelper.isLich(player)){
            player.getFoodData().setFoodLevel(17);
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            boolean burn = false;
            if (!world.isClientSide && world.isDay()) {
                float f = player.getBrightness();
                BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                if (f > 0.5F && world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(blockpos)) {
                    burn = true;
                }
            }

            if (!MainConfig.DeadSandDarkSky.get()) {
                BlockState blockState;
                Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                        player.blockPosition().offset(-4, -4, -4),
                        player.blockPosition().offset(4, 4, 4));
                for (BlockPos blockToCheck : blocksToCheck) {
                    blockState = world.getBlockState(blockToCheck);
                    if (blockState.getBlock() instanceof IDeadBlock) {
                        result.add(blockState);
                    }
                }
                if (result.size() >= 16){
                    player.clearFire();
                    burn = false;
                }
            }

            if (burn){
                ItemStack helmet = player.getItemBySlot(EquipmentSlotType.HEAD);
                if (!helmet.isEmpty()) {
                    if (!player.isCreative()) {
                        if (!player.hasEffect(Effects.FIRE_RESISTANCE)) {
                            if (MainConfig.LichDamageHelmet.get()) {
                                if (helmet.isDamageableItem()) {
                                    helmet.setDamageValue(helmet.getDamageValue() + world.random.nextInt(2));
                                    if (helmet.getDamageValue() >= helmet.getMaxDamage()) {
                                        player.broadcastBreakEvent(EquipmentSlotType.HEAD);
                                        player.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                                    }
                                }
                            }
                        }
                    }
                    burn = false;
                }
                if (burn){
                    if (player.hasEffect(Effects.FIRE_RESISTANCE) && !player.isCreative() && !player.isSpectator()){
                        player.addEffect(new EffectInstance(ModEffects.SAPPED.get(), 100, 1, false, false));
                    } else {
                        player.setSecondsOnFire(8);
                    }
                }
            }
            player.getActiveEffects().removeIf(effectInstance -> {
                Effect effect = effectInstance.getEffect();
                if (!new ZombieEntity(world).canBeAffected(effectInstance)){
                    return true;
                } else return effect == Effects.BLINDNESS || effect == Effects.CONFUSION
                        || effect == Effects.HUNGER || effect == Effects.SATURATION
                        || effect == ModEffects.DESICCATE.get();
            });
            if (player.hasEffect(ModEffects.SOUL_HUNGER.get())){
                if (SEHelper.getSoulsAmount(player, MainConfig.MaxSouls.get())){
                    player.removeEffect(ModEffects.SOUL_HUNGER.get());
                }
            }
            if (MainConfig.LichSoulHeal.get()) {
                if (!player.isOnFire()) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        if (player.tickCount % ModMathHelper.secondsToTicks(MainConfig.LichHealSeconds.get()) == 0 && SEHelper.getSoulsAmount(player, MainConfig.LichHealCost.get())) {
                            player.heal(MainConfig.LichHealAmount.get().floatValue());
                            Vector3d vector3d = player.getDeltaMovement();
                            if (!player.level.isClientSide) {
                                ServerWorld serverWorld = (ServerWorld) player.level;
                                serverWorld.sendParticles(ParticleTypes.SOUL, player.getRandomX(0.5D), player.getRandomY(), player.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D, 0.5F);
                            }
                            SEHelper.decreaseSouls(player, MainConfig.LichHealCost.get());
                        }
                    }
                }
            }
            if (MainConfig.LichVillagerHate.get()) {
                for (VillagerEntity villager : player.level.getEntitiesOfClass(VillagerEntity.class, player.getBoundingBox().inflate(16.0D))) {
                    if (villager.getPlayerReputation(player) > -100 && villager.getPlayerReputation(player) < 100) {
                        if (player.tickCount % 20 == 0) {
                            villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
                        }
                    }
                }
                for (IronGolemEntity ironGolem : player.level.getEntitiesOfClass(IronGolemEntity.class, player.getBoundingBox().inflate(16.0D))) {
                    if (!ironGolem.isPlayerCreated() && ironGolem.getTarget() != player && EntityPredicate.DEFAULT.range(16.0F).test(ironGolem, player)) {
                        ironGolem.setTarget(player);
                    }
                }
            }
            if (player.isAlive()){
                player.setAirSupply(player.getMaxAirSupply());
                if (MainConfig.LichNightVision.get()) {
                    player.addEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void SpecialPotionEffects(PotionEvent.PotionApplicableEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (LichdomHelper.isLich(player)){
                if (!new ZombieEntity(player.level).canBeAffected(event.getPotionEffect())){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.BLINDNESS){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.CONFUSION){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.HUNGER){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.SATURATION){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == ModEffects.DESICCATE.get()){
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void UndeadFriendly(LivingSetAttackTargetEvent event){
        if (MainConfig.LichUndeadFriends.get()) {
            if (event.getEntityLiving() instanceof IMob && event.getEntityLiving() instanceof MobEntity) {
                if (event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD) {
                    if (event.getTarget() != null) {
                        if (event.getTarget() instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) event.getTarget();
                            if (LichdomHelper.isLich(player)) {
                                if (MainConfig.LichPowerfulFoes.get()) {
                                    if (event.getEntityLiving().getMaxHealth() < 100) {
                                        ((MobEntity) event.getEntityLiving()).setTarget(null);
                                        if (event.getEntityLiving() instanceof IAngerable){
                                            ((IAngerable) event.getEntityLiving()).stopBeingAngry();
                                        }
                                    }
                                } else {
                                    ((MobEntity) event.getEntityLiving()).setTarget(null);
                                    if (event.getEntityLiving() instanceof IAngerable){
                                        ((IAngerable) event.getEntityLiving()).stopBeingAngry();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (event.getEntityLiving() instanceof IDeadMob){
                if (event.getTarget() != null) {
                    if (event.getTarget() instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) event.getTarget();
                        if (LichdomHelper.isLich(player)) {
                            ((MonsterEntity) event.getEntityLiving()).setTarget(null);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (LichdomHelper.isLich(player)){
                if (event.getSource().getEntity() instanceof LivingEntity){
                    LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                    if (attacker.getMainHandItem().isEnchanted()){
                        ItemStack weapon = attacker.getMainHandItem();
                        event.setAmount((float) (EnchantmentHelper.getDamageBonus(weapon, CreatureAttribute.UNDEAD) + attacker.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                    }
                }
                if (event.getSource() == DamageSource.DROWN){
                    event.setCanceled(true);
                }
                if (MainConfig.LichMagicResist.get()) {
                    if (event.getSource().isMagic()) {
                        event.setAmount(event.getAmount() * 0.15F);
                    }
                }
                if (ModDamageSource.frostAttacks(event.getSource())){
                    event.setAmount(event.getAmount()/2);
                }
                if (MainConfig.LichUndeadFriends.get()) {
                    if (RobeArmorFinder.FindNecroSet(player) && event.getSource().getEntity() != null) {
                        if (event.getSource().getEntity() instanceof LivingEntity) {
                            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                            for (MonsterEntity undead : player.level.getEntitiesOfClass(MonsterEntity.class, player.getBoundingBox().inflate(16))) {
                                if (undead.getMobType() == CreatureAttribute.UNDEAD) {
                                    if (undead.getTarget() != player) {
                                        if (MainConfig.LichPowerfulFoes.get()) {
                                            if (undead.getMaxHealth() < 100.0F) {
                                                undead.setTarget(attacker);
                                            }
                                        } else {
                                            undead.setTarget(attacker);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            if (LichdomHelper.isLich(player)){
                if (ModDamageSource.physicalAttacks(event.getSource()) && event.getEntityLiving() != player){
                    if (player.getMainHandItem().isEmpty()) {
                        event.getEntityLiving().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1200));
                    }
                    if (event.getEntityLiving().getMobType() != CreatureAttribute.UNDEAD && player.getMainHandItem().getItem().is(ModTags.Items.LICH_WITHER_ITEMS)){
                        event.getEntityLiving().addEffect(new EffectInstance(Effects.WITHER, ModMathHelper.secondsToTicks(5)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void AttackEvent(LivingAttackEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (LichdomHelper.isLich(player)) {
                if (event.getSource() == DamageSource.DROWN){
                    event.setCanceled(true);
                }
            }
        }
    }

}
