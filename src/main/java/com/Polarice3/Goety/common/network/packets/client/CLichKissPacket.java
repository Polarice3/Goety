package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.common.spells.cantrips.LichKissCantrip;
import com.Polarice3.Goety.utils.LichdomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CLichKissPacket {
    public static void encode(CLichKissPacket packet, PacketBuffer buffer) {
    }

    public static CLichKissPacket decode(PacketBuffer buffer) {
        return new CLichKissPacket();
    }

    public static void consume(CLichKissPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                if (LichdomHelper.isLich(playerEntity)) {
                    new LichKissCantrip().sendRay(playerEntity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
