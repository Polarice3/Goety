package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.client.inventory.container.WandandBagContainer;
import com.Polarice3.Goety.common.items.SoulWand;
import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.utils.FocusBagFinder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class CWandAndBagKeyPacket {

    public static void encode(CWandAndBagKeyPacket packet, PacketBuffer buffer) {
    }

    public static CWandAndBagKeyPacket decode(PacketBuffer buffer) {
        return new CWandAndBagKeyPacket();
    }

    public static void consume(CWandAndBagKeyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                ItemStack bag = FocusBagFinder.findBag(playerEntity);
                ItemStack stack = playerEntity.getMainHandItem();
                ItemStack stack2 = playerEntity.getOffhandItem();

                if (!bag.isEmpty()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof SoulWand) {
                        SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                                (id, inventory, player) -> new WandandBagContainer(id, SoulUsingItemHandler.get(playerEntity.getMainHandItem()), FocusBagItemHandler.get(bag), playerEntity.getMainHandItem()), stack.getDisplayName());
                        NetworkHooks.openGui(playerEntity, provider, (buffer) -> buffer.writeBoolean(playerEntity.getUsedItemHand() == Hand.MAIN_HAND));
                    } else if (!stack2.isEmpty() && stack2.getItem() instanceof SoulWand) {
                        SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                                (id, inventory, player) -> new WandandBagContainer(id, SoulUsingItemHandler.get(playerEntity.getOffhandItem()), FocusBagItemHandler.get(bag), playerEntity.getOffhandItem()), stack2.getDisplayName());
                        NetworkHooks.openGui(playerEntity, provider, (buffer) -> buffer.writeBoolean(playerEntity.getUsedItemHand() == Hand.OFF_HAND));
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
