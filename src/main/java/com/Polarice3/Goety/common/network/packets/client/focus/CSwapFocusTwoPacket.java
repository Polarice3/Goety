package com.Polarice3.Goety.common.network.packets.client.focus;

import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CSwapFocusTwoPacket {
    public int swapWith;

    public CSwapFocusTwoPacket(int swapWith){
        this.swapWith = swapWith;
    }

    public static void encode(CSwapFocusTwoPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.swapWith);
    }

    public static CSwapFocusTwoPacket decode(PacketBuffer buffer) {
        return new CSwapFocusTwoPacket(buffer.readInt());
    }

    public static void consume(CSwapFocusTwoPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> swapFocus(packet.swapWith, ctx.get().getSender()));
    }

    public static void swapFocus(int swapSlot, PlayerEntity player) {
        ItemStack wand = WandUtil.findWand(player);

        SoulUsingItemHandler wandHandler = SoulUsingItemHandler.get(wand);

        ItemStack wandFocus = wandHandler.getSlot();

        ItemStack invFocus = player.inventory.getItem(swapSlot);
        player.inventory.setItem(swapSlot, wandFocus);
        wandHandler.extractItem();
        wandHandler.insertItem(invFocus);
        if (player instanceof ServerPlayerEntity){
            ((ServerPlayerEntity) player).connection.send(new SPlaySoundPacket(ModSounds.FOCUS_PICK.getId(), SoundCategory.PLAYERS, player.position(), 1.0F, 1.0F));
        }
    }
}
