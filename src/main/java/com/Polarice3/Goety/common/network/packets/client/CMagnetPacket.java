package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.common.magic.cantrips.MagnetCantrip;
import com.Polarice3.Goety.utils.LichdomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CMagnetPacket {
    public static void encode(CMagnetPacket packet, PacketBuffer buffer) {
    }

    public static CMagnetPacket decode(PacketBuffer buffer) {
        return new CMagnetPacket();
    }

    public static void consume(CMagnetPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                if (LichdomHelper.isLich(playerEntity)) {
                    new MagnetCantrip().callItems(playerEntity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
