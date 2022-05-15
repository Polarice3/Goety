package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantmentsType;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.neutral.MutatedEntity;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.soulenergy.SEImp;
import com.Polarice3.Goety.common.soulenergy.SEProvider;
import com.Polarice3.Goety.common.soulenergy.SEUpdatePacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

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

    public static BlockPos getArcaBlock(PlayerEntity player){
        return getCapability(player).getArcaBlock();
    }

    public static boolean decreaseSESouls(PlayerEntity player, int souls){
        return getCapability(player).decreaseSE(souls);
    }

    public static boolean increaseSESouls(PlayerEntity player, int souls){
        return getCapability(player).increaseSE(souls);
    }

    public static void handleKill(LivingEntity killer, LivingEntity victim) {
        PlayerEntity player = null;
        if (killer instanceof PlayerEntity){
            player = (PlayerEntity) killer;
        } else if (killer instanceof SummonedEntity){
            SummonedEntity summonedEntity = (SummonedEntity) killer;
            if (summonedEntity.getTrueOwner() instanceof PlayerEntity){
                player = (PlayerEntity) summonedEntity.getTrueOwner();
            }
        } else if (killer instanceof FriendlyVexEntity){
            FriendlyVexEntity friendlyVex = (FriendlyVexEntity) killer;
            if (friendlyVex.getTrueOwner() instanceof PlayerEntity){
                player = (PlayerEntity) friendlyVex.getTrueOwner();
            }
        }
        if (player != null) {
            if (getSEActive(player)) {
                if (!(victim instanceof SummonedEntity || victim instanceof FriendlyVexEntity)) {
                    if (victim.getMobType() == CreatureAttribute.UNDEAD) {
                        increaseSESouls(player, MainConfig.UndeadSouls.get() * SoulMultiply(killer));
                    } else if (victim.getMobType() == CreatureAttribute.ARTHROPOD) {
                        increaseSESouls(player, MainConfig.AnthropodSouls.get() * SoulMultiply(killer));
                    } else if (victim instanceof AbstractRaiderEntity) {
                        increaseSESouls(player, MainConfig.IllagerSouls.get() * SoulMultiply(killer));
                    } else if (victim instanceof VillagerEntity && !victim.isBaby()) {
                        increaseSESouls(player, MainConfig.VillagerSouls.get() * SoulMultiply(killer));
                    } else if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity || victim instanceof MutatedEntity) {
                        increaseSESouls(player, MainConfig.PiglinSouls.get() * SoulMultiply(killer));
                    } else if (victim instanceof EnderDragonEntity) {
                        increaseSESouls(player, MainConfig.EnderDragonSouls.get() * SoulMultiply(killer));
                    } else if (victim instanceof PlayerEntity) {
                        increaseSESouls(player, MainConfig.PlayerSouls.get() * SoulMultiply(killer));
                    } else {
                        increaseSESouls(player, MainConfig.DefaultSouls.get() * SoulMultiply(killer));
                    }
                    SEHelper.sendSEUpdatePacket(player);
                }
            }
        }
    }

    public static int SoulMultiply(LivingEntity livingEntity){
        ItemStack weapon= livingEntity.getMainHandItem();
        int i = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantmentsType.SOULEATER.get(), weapon);
        if (i > 0){
            return i + 1;
        } else {
            return 1;
        }
    }

    public static void sendSEUpdatePacket(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            ModNetwork.sendTo(player, new SEUpdatePacket(player));
        }
    }
}
