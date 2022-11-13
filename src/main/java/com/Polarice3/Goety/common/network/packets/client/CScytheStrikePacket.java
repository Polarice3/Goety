package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.common.items.equipment.DeathScytheItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CScytheStrikePacket {
    public static void encode(CScytheStrikePacket packet, PacketBuffer buffer) {
    }

    public static CScytheStrikePacket decode(PacketBuffer buffer) {
        return new CScytheStrikePacket();
    }

    public static void consume(CScytheStrikePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                new DeathScytheItem().strike(playerEntity.level, playerEntity);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
