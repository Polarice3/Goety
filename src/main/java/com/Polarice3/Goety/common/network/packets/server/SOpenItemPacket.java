package com.Polarice3.Goety.common.network.packets.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SOpenItemPacket {
    private ItemStack stack;

    public SOpenItemPacket(ItemStack stackIn) {
        this.stack = stackIn;
    }

    public static void encode(SOpenItemPacket packet, PacketBuffer buffer) {
        buffer.writeItem(packet.stack);
    }

    public static SOpenItemPacket decode(PacketBuffer buffer) {
        return new SOpenItemPacket(buffer.readItem());
    }

    public static void consume(SOpenItemPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        });
        ctx.get().setPacketHandled(true);
    }
}
