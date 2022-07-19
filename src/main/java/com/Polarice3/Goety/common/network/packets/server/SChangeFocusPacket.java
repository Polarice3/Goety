package com.Polarice3.Goety.common.network.packets.server;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SChangeFocusPacket {
    private ItemStack stack;
    private CompoundNBT tag;

    public SChangeFocusPacket(ItemStack stackIn, CompoundNBT tag) {
        this.stack = stackIn;
        this.tag = tag;
    }

    public static void encode(SChangeFocusPacket packet, PacketBuffer buffer) {
        buffer.writeItem(packet.stack);
        buffer.writeNbt(packet.tag);
    }

    public static SChangeFocusPacket decode(PacketBuffer buffer) {
        return new SChangeFocusPacket(buffer.readItem(), buffer.readNbt());
    }

    public static void consume(SChangeFocusPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            ItemStack stack = packet.stack;

            if (!stack.isEmpty()){
                stack.setTag(packet.tag);
            }
        });
        ctx.get().setPacketHandled(true);
    }


}
