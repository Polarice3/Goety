package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.TileEntityUpdatePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityHelper {
    public static void sendArcaUpdatePacket(PlayerEntity player) {
        if (GoldTotemFinder.FindArca(player) != null) {
            BlockPos blockPos = GoldTotemFinder.FindArca(player).getBlockPos();
            ModNetwork.sendTo(player, new TileEntityUpdatePacket(blockPos, GoldTotemFinder.FindArca(player).getUpdateTag()));
        }
    }

    public static void sendArcaServerUpdatePacket(PlayerEntity player) {
        if (GoldTotemFinder.FindArca(player) != null) {
            BlockPos blockPos = GoldTotemFinder.FindArca(player).getBlockPos();
            ModNetwork.sendToServer(new TileEntityUpdatePacket(blockPos, GoldTotemFinder.FindArca(player).getUpdateTag()));
        }
    }
}
