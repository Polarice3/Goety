package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEImp;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEProvider;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEUpdatePacket;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.MutatedEntity;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.events.ArcaTeleporter;
import com.Polarice3.Goety.common.items.magic.GoldTotemItem;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.compat.minecolonies.MinecoloniesLoaded;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class SEHelper {
    public static ISoulEnergy getCapability(PlayerEntity player) {
        return player.getCapability(SEProvider.CAPABILITY).orElse(new SEImp());
    }

    public static boolean getSEActive(PlayerEntity player) {
        return getCapability(player).getSEActive();
    }

    public static void setSEActive(PlayerEntity player, boolean active){
        getCapability(player).setSEActive(active);
    }

    public static int getSESouls(PlayerEntity player){
        return getCapability(player).getSoulEnergy();
    }

    public static void setSESouls(PlayerEntity player, int souls){
        getCapability(player).setSoulEnergy(souls);
    }

    public static void setSoulsAmount(PlayerEntity player, int souls){
        if (SEHelper.getSEActive(player)){
            SEHelper.setSESouls(player, souls);
        } else if (!GoldTotemFinder.FindTotem(player).isEmpty()){
            GoldTotemItem.setSoulsamount(GoldTotemFinder.FindTotem(player), souls);
        }
    }

    public static BlockPos getArcaBlock(PlayerEntity player){
        return getCapability(player).getArcaBlock();
    }

    public static boolean decreaseSESouls(PlayerEntity player, int souls){
        return getCapability(player).decreaseSE(souls);
    }

    public static boolean increaseSESouls(PlayerEntity player, int souls){
        return getCapability(player).increaseSE(souls);
    }

    public static boolean getSoulsContainer(PlayerEntity player){
        if(SEHelper.getSEActive(player)){
            return true;
        } else return !GoldTotemFinder.FindTotem(player).isEmpty();
    }

    public static boolean getSoulsAmount(PlayerEntity player, int souls){
        if (SEHelper.getSEActive(player) && SEHelper.getSESouls(player) > souls){
            return true;
        } else {
            return !GoldTotemFinder.FindTotem(player).isEmpty() && GoldTotemItem.currentSouls(GoldTotemFinder.FindTotem(player)) > souls;
        }
    }

    public static int getSoulAmountInt(PlayerEntity player){
        if (SEHelper.getSEActive(player)){
            return SEHelper.getSESouls(player);
        } else if (!GoldTotemFinder.FindTotem(player).isEmpty()){
            return GoldTotemItem.currentSouls(GoldTotemFinder.FindTotem(player));
        }
        return 0;
    }

    public static int getSoulGiven(LivingEntity victim){
        if (victim != null){
            boolean flag = victim instanceof OwnedEntity && !(victim instanceof IMob);
            if (!flag) {
                if (victim.getMobType() == CreatureAttribute.UNDEAD) {
                    return MainConfig.UndeadSouls.get();
                } else if (victim.getMobType() == CreatureAttribute.ARTHROPOD) {
                    return MainConfig.AnthropodSouls.get();
                } else if (victim instanceof AbstractRaiderEntity) {
                    return MainConfig.IllagerSouls.get();
                } else if (victim instanceof VillagerEntity && !victim.isBaby()) {
                    return MainConfig.VillagerSouls.get();
                } else if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity || victim instanceof MutatedEntity) {
                    return MainConfig.PiglinSouls.get();
                } else if (victim instanceof EnderDragonEntity) {
                    return MainConfig.EnderDragonSouls.get();
                } else if (victim instanceof PlayerEntity) {
                    return MainConfig.PlayerSouls.get();
                } else if (MinecoloniesLoaded.MINECOLONIES.isLoaded()
                        && victim.getType().getDescriptionId().contains("minecolonies")
                        && victim.getType().getCategory() != EntityClassification.MISC){
                    return MainConfig.VillagerSouls.get();
                } else {
                    return MainConfig.DefaultSouls.get();
                }
            }
        }
        return 0;
    }

    public static void rawHandleKill(LivingEntity killer, LivingEntity victim, int soulEater) {
        PlayerEntity player = null;
        if (killer instanceof PlayerEntity){
            player = (PlayerEntity) killer;
        } else if (killer instanceof OwnedEntity){
            OwnedEntity summonedEntity = (OwnedEntity) killer;
            if (summonedEntity.getTrueOwner() instanceof PlayerEntity){
                player = (PlayerEntity) summonedEntity.getTrueOwner();
            }
        }
        if (player != null) {
            increaseSouls(player, getSoulGiven(victim) * soulEater);
        }
    }

    public static void handleKill(LivingEntity killer, LivingEntity victim) {
        SEHelper.rawHandleKill(killer, victim, SEHelper.SoulMultiply(killer));
    }

    public static void increaseSouls(PlayerEntity player, int souls){
        if (getSEActive(player)) {
            increaseSESouls(player, souls);
            SEHelper.sendSEUpdatePacket(player);
        } else {
            ItemStack foundStack = GoldTotemFinder.FindTotem(player);
            if (foundStack != null){
                GoldTotemItem.increaseSouls(foundStack, souls);
            }
        }
    }

    public static void decreaseSouls(PlayerEntity player, int souls){
        if (getSEActive(player)) {
            decreaseSESouls(player, souls);
            SEHelper.sendSEUpdatePacket(player);
        } else {
            ItemStack foundStack = GoldTotemFinder.FindTotem(player);
            if (foundStack != null){
                GoldTotemItem.decreaseSouls(foundStack, souls);
            }
        }
    }

    public static int SoulMultiply(LivingEntity livingEntity){
        ItemStack weapon= livingEntity.getMainHandItem();
        int multiply = 1;
        int i = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOUL_EATER.get(), weapon);
        if (i > 0) {
            multiply = MathHelper.clamp(i + 1, 1, 10);
        }
        int j = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOUL_TAKER.get(), weapon);
        if (j > 0) {
            multiply = MathHelper.clamp(j + 1, 1, 10);
        }
        int k = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOUL_FEEDER.get(), weapon);
        if (k > 0) {
            multiply = MathHelper.clamp(k + 1, 1, 10);
        }
        return multiply;
    }

    public static void teleportDeathArca(PlayerEntity player){
        ISoulEnergy soulEnergy = SEHelper.getCapability(player);
        BlockPos blockPos = SEHelper.getArcaBlock(player);
        BlockPos blockPos1 = new BlockPos(blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F);
        Vector3d vector3d = new Vector3d(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ());
        if (soulEnergy.getArcaBlockDimension() == player.level.dimension()) {
            player.teleportTo(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ());
        } else {
            if (soulEnergy.getArcaBlockDimension() != null) {
                if (player.getServer() != null) {
                    ServerWorld serverWorld = player.getServer().getLevel(soulEnergy.getArcaBlockDimension());
                    if (serverWorld != null) {
                        player.changeDimension(serverWorld, new ArcaTeleporter(vector3d));
                        player.teleportTo(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ());
                    }
                }
            }
        }
    }

    public static void sendSEUpdatePacket(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            ModNetwork.sendTo(player, new SEUpdatePacket(player));
        }
    }
}
