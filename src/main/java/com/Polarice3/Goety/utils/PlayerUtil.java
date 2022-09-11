package com.Polarice3.Goety.utils;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class PlayerUtil {
    protected static final UUID REACH_UUID = UUID.fromString("e7d3f68d-4ac0-4117-8294-0b16d16135ff");
    private static final AttributeModifier REACH_MODIFIER = new AttributeModifier(REACH_UUID, "SpearReach", 4.0D, AttributeModifier.Operation.ADDITION);

    public static void enableStepHeight(PlayerEntity player) {
        player.getPersistentData().putBoolean("WanderBoots", true);
        PlayerUtil.enableStepHeightInternal(player);
    }

    public static void disableStepHeight(PlayerEntity player) {
        if (player.getPersistentData().contains("WanderBoots") && player.getPersistentData().getBoolean("WanderBoots")) {
            PlayerUtil.disableStepHeightInternal(player);
            player.getPersistentData().putBoolean("WanderBoots", false);
        }
    }

    private static void disableStepHeightInternal(PlayerEntity player) {
        player.maxUpStep = 0.6F;
    }

    private static void enableStepHeightInternal(PlayerEntity player) {
        player.maxUpStep = 1.0F + (0.0625F);
    }

    public static void enableSpearReach(PlayerEntity player){
        player.getPersistentData().putBoolean("SpearReach", true);
        PlayerUtil.enableSpearReachInternal(player);
    }

    public static void disableSpearReach(PlayerEntity player) {
        if (player.getPersistentData().contains("SpearReach") && player.getPersistentData().getBoolean("SpearReach")) {
            PlayerUtil.disableSpearReachInternal(player);
            player.getPersistentData().putBoolean("SpearReach", false);
        }
    }

    public static void enableSpearReachInternal(PlayerEntity player){
        ModifiableAttributeInstance reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reach != null) {
            if (!reach.hasModifier(REACH_MODIFIER)) {
                reach.addPermanentModifier(REACH_MODIFIER);
            }
        }
    }

    public static void disableSpearReachInternal(PlayerEntity player){
        ModifiableAttributeInstance reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reach != null) {
            if (reach.hasModifier(REACH_MODIFIER)){
                reach.removePermanentModifier(REACH_UUID);
            }
        }
    }
}
