package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CSoulEnergyPacket {
    private final int souls;
    private final boolean increase;

    public CSoulEnergyPacket(int souls, boolean increase) {
        this.souls = souls;
        this.increase = increase;
    }

    public static void encode(CSoulEnergyPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.souls);
        buffer.writeBoolean(packet.increase);
    }

    public static CSoulEnergyPacket decode(PacketBuffer buffer) {
        return new CSoulEnergyPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void consume(CSoulEnergyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {
                if (packet.increase) {
                    if (SEHelper.getSEActive(player)) {
                        SEHelper.increaseSESouls(player, packet.souls);
                        SEHelper.sendSEUpdatePacket(player);
                    } else {
                        ItemStack foundStack = GoldTotemFinder.FindTotem(player);
                        if (foundStack != null) {
                            ITotem.increaseSouls(foundStack, packet.souls);
                        }
                    }
                } else {
                    if (SEHelper.getSEActive(player)) {
                        SEHelper.decreaseSESouls(player, packet.souls);
                        SEHelper.sendSEUpdatePacket(player);
                    } else {
                        ItemStack foundStack = GoldTotemFinder.FindTotem(player);
                        if (foundStack != null){
                            ITotem.decreaseSouls(foundStack, packet.souls);
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
