package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.Goety.common.entities.hostile.IDeadMob;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.LichdomUtil;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LichEvent {

    @SubscribeEvent
    public static void onPlayerLichdom(TickEvent.PlayerTickEvent event){
        List<BlockState> result = new ArrayList<>();
        PlayerEntity player = event.player;
        World world = player.level;
        if (LichdomUtil.isLich(player)){
            player.getFoodData().setFoodLevel(17);
            boolean burn = false;
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
            if (!world.isClientSide && world.isDay()) {
                float f = player.getBrightness();
                BlockPos blockpos = player.getVehicle() instanceof BoatEntity ? (new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ())).above() : new BlockPos(player.getX(), (double) Math.round(player.getY()), player.getZ());
                if (f > 0.5F && world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(blockpos)) {
                    burn = true;
                }
            }

            if (result.size() >= 16){
                player.clearFire();
                burn = false;
            }

            if (burn){
                ItemStack helmet = player.getItemBySlot(EquipmentSlotType.HEAD);
                if (!helmet.isEmpty()) {
                    if (helmet.isDamageableItem()) {
                        helmet.setDamageValue(helmet.getDamageValue() + world.random.nextInt(2));
                        if (helmet.getDamageValue() >= helmet.getMaxDamage()) {
                            player.broadcastBreakEvent(EquipmentSlotType.HEAD);
                            player.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    }
                    burn = false;
                }
                if (burn){
                    player.setSecondsOnFire(8);
                }
            }

            if (player.hasEffect(Effects.REGENERATION)){
                player.removeEffectNoUpdate(Effects.REGENERATION);
            }
            if (player.hasEffect(Effects.POISON)){
                player.removeEffectNoUpdate(Effects.POISON);
            }
            if (player.hasEffect(Effects.HUNGER)){
                player.removeEffectNoUpdate(Effects.HUNGER);
            }
            if (player.hasEffect(Effects.SATURATION)){
                player.removeEffectNoUpdate(Effects.SATURATION);
            }
            ItemStack goldtotem = GoldTotemFinder.FindTotem(player);
            if (!goldtotem.isEmpty()){
                if (!player.isOnFire()) {
                    if (player.isHurt() || player.getHealth() < player.getMaxHealth()) {
                        if (player.tickCount % 20 == 0 && GoldTotemItem.currentSouls(goldtotem) > MainConfig.LichHealCost.get()) {
                            player.heal(1.0F);
                            Vector3d vector3d = player.getDeltaMovement();
                            new ParticleUtil(ParticleTypes.SOUL, player.getRandomX(0.5D), player.getRandomY(), player.getRandomZ(0.5D), vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D);
                            GoldTotemItem.decreaseSouls(goldtotem, MainConfig.LichHealCost.get());
                        }
                    }
                }
            }
            for (VillagerEntity villager : player.level.getEntitiesOfClass(VillagerEntity.class, player.getBoundingBox().inflate(16.0D))) {
                if (villager.getPlayerReputation(player) > -100 && villager.getPlayerReputation(player) < 100) {
                    if (player.tickCount % 20 == 0) {
                        villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
                    }
                }
            }
            if (player.isEyeInFluid(FluidTags.WATER)){
                player.setAirSupply(player.getMaxAirSupply());
            }
            if (player.isAlive()){
                if (MainConfig.LichNightVision.get()) {
                    player.addEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void SpecialPotionEffects(PotionEvent.PotionApplicableEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (LichdomUtil.isLich(player)){
                if (event.getPotionEffect().getEffect() == Effects.POISON){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.REGENERATION){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.HUNGER){
                    event.setResult(Event.Result.DENY);
                }
                if (event.getPotionEffect().getEffect() == Effects.SATURATION){
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void UndeadFriendly(LivingSetAttackTargetEvent event){
        if (MainConfig.LichUndeadFriends.get()) {
            if (event.getEntityLiving() instanceof MonsterEntity) {
                if (event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD) {
                    if (event.getTarget() != null) {
                        if (event.getTarget() instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) event.getTarget();
                            if (LichdomUtil.isLich(player)) {
                                ((MonsterEntity) event.getEntityLiving()).setTarget(null);
                            }
                        }
                    }
                }
            }
            if (event.getEntityLiving() instanceof IDeadMob){
                if (event.getTarget() != null) {
                    if (event.getTarget() instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) event.getTarget();
                        if (LichdomUtil.isLich(player)) {
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
            if (LichdomUtil.isLich(player)){
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
                if (MainConfig.LichUndeadFriends.get()) {
                    if (RobeArmorFinder.FindNecroSet(player) && event.getSource().getEntity() != null) {
                        if (event.getSource().getEntity() instanceof LivingEntity) {
                            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                            for (MonsterEntity undead : player.level.getEntitiesOfClass(MonsterEntity.class, player.getBoundingBox().inflate(16))) {
                                if (undead.getMobType() == CreatureAttribute.UNDEAD && !(undead instanceof WitherEntity)) {
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
