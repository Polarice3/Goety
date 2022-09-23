package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.MutatedEntity;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.soulenergy.SEImp;
import com.Polarice3.Goety.common.soulenergy.SEProvider;
import com.Polarice3.Goety.common.soulenergy.SEUpdatePacket;
import com.Polarice3.Goety.compat.minecolonies.MinecoloniesLoaded;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityClassification;
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
        } else if (killer instanceof OwnedEntity){
            OwnedEntity summonedEntity = (OwnedEntity) killer;
            if (summonedEntity.getTrueOwner() instanceof PlayerEntity){
                player = (PlayerEntity) summonedEntity.getTrueOwner();
            }
        }
        if (player != null) {
            if (!(victim instanceof OwnedEntity)) {
                if (victim.getMobType() == CreatureAttribute.UNDEAD) {
                    increaseSouls(player, MainConfig.UndeadSouls.get() * SoulMultiply(killer));
                } else if (victim.getMobType() == CreatureAttribute.ARTHROPOD) {
                    increaseSouls(player, MainConfig.AnthropodSouls.get() * SoulMultiply(killer));
                } else if (victim instanceof AbstractRaiderEntity) {
                    increaseSouls(player, MainConfig.IllagerSouls.get() * SoulMultiply(killer));
                } else if (victim instanceof VillagerEntity && !victim.isBaby()) {
                    increaseSouls(player, MainConfig.VillagerSouls.get() * SoulMultiply(killer));
                } else if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity || victim instanceof MutatedEntity) {
                    increaseSouls(player, MainConfig.PiglinSouls.get() * SoulMultiply(killer));
                } else if (victim instanceof EnderDragonEntity) {
                    increaseSouls(player, MainConfig.EnderDragonSouls.get() * SoulMultiply(killer));
                } else if (victim instanceof PlayerEntity) {
                    increaseSouls(player, MainConfig.PlayerSouls.get() * SoulMultiply(killer));
                } else if (MinecoloniesLoaded.MINECOLONIES.isLoaded()
                        && victim.getType().getDescriptionId().contains("minecolonies")
                        && victim.getType().getCategory() != EntityClassification.MISC){
                    increaseSouls(player, MainConfig.VillagerSouls.get() * SoulMultiply(killer));
                } else {
                    increaseSouls(player, MainConfig.DefaultSouls.get() * SoulMultiply(killer));
                }
            }
        }
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

    public static int SoulMultiply(LivingEntity livingEntity){
        ItemStack weapon= livingEntity.getMainHandItem();
        int i = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOULEATER.get(), weapon);
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
