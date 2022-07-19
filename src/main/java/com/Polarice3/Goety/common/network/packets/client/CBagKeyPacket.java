package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.client.inventory.container.FocusBagContainer;
import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import com.Polarice3.Goety.utils.FocusBagFinder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class CBagKeyPacket {

    public static void encode(CBagKeyPacket packet, PacketBuffer buffer) {
    }

    public static CBagKeyPacket decode(PacketBuffer buffer) {
        return new CBagKeyPacket();
    }

    public static void consume(CBagKeyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                ItemStack stack = FocusBagFinder.findBag(playerEntity);

                if (!stack.isEmpty()){
                    SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                            (id, inventory, player) -> new FocusBagContainer(id, inventory, FocusBagItemHandler.get(stack), stack), stack.getDisplayName());
                    NetworkHooks.openGui(playerEntity, provider, (buffer) -> {});
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
