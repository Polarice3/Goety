package com.Polarice3.Goety.utils;

import net.minecraft.entity.player.PlayerEntity;

public class BootsUtil {

    public static void enableStepHeight(PlayerEntity player) {
        player.getPersistentData().putBoolean("WanderBoots", true);
        BootsUtil.enableStepHeightInternal(player);
    }

    public static void disableStepHeight(PlayerEntity player) {
        if (player.getPersistentData().contains("WanderBoots") && player.getPersistentData().getBoolean("WanderBoots")) {
            BootsUtil.disableStepHeightInternal(player);
            player.getPersistentData().putBoolean("WanderBoots", false);
        }
    }

    private static void disableStepHeightInternal(PlayerEntity player) {
        player.maxUpStep = 0.6F;
    }

    private static void enableStepHeightInternal(PlayerEntity player) {
        player.maxUpStep = 1.0F + (1F / 16F);
    }
}
