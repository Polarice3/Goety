package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.client.inventory.container.SoulItemContainer;
import com.Polarice3.Goety.common.items.SoulWand;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class CWandKeyPacket {

    public static void encode(CWandKeyPacket packet, PacketBuffer buffer) {
    }

    public static CWandKeyPacket decode(PacketBuffer buffer) {
        return new CWandKeyPacket();
    }

    public static void consume(CWandKeyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                ItemStack stack = playerEntity.getMainHandItem();
                ItemStack stack2 = playerEntity.getOffhandItem();

                if (!stack.isEmpty() && stack.getItem() instanceof SoulWand) {
                    SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                            (id, inventory, player) -> new SoulItemContainer(id, inventory, SoulUsingItemHandler.get(stack), stack, playerEntity.getUsedItemHand()), stack.getDisplayName());
                    NetworkHooks.openGui(playerEntity, provider, (buffer) -> buffer.writeBoolean(playerEntity.getUsedItemHand() == Hand.MAIN_HAND));
                } else if (!stack2.isEmpty() && stack2.getItem() instanceof SoulWand){
                    SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                            (id, inventory, player) -> new SoulItemContainer(id, inventory, SoulUsingItemHandler.get(stack2), stack2, playerEntity.getUsedItemHand()), stack2.getDisplayName());
                    NetworkHooks.openGui(playerEntity, provider, (buffer) -> buffer.writeBoolean(playerEntity.getUsedItemHand() == Hand.OFF_HAND));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
