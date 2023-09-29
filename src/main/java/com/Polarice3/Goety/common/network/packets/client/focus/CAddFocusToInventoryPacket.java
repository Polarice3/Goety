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

public class CAddFocusToInventoryPacket {
    public CAddFocusToInventoryPacket(){
    }

    public static void encode(CAddFocusToInventoryPacket packet, PacketBuffer buffer) {
    }

    public static CAddFocusToInventoryPacket decode(PacketBuffer buffer) {
        return new CAddFocusToInventoryPacket();
    }

    public static void consume(CAddFocusToInventoryPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = WandUtil.findFocus(player);
                if (stack.getCount() <= 0) {
                    return;
                }

                ItemStack wand = WandUtil.findWand(player);

                SoulUsingItemHandler wandHandler = SoulUsingItemHandler.get(wand);

                ItemStack wandFocus = wandHandler.getSlot();

                for (int i = 0; i < player.inventory.items.size(); ++i) {
                    ItemStack itemStack = player.inventory.getItem(i);
                    if (itemStack.isEmpty()) {
                        player.inventory.setItem(i, wandFocus);
                        wandHandler.extractItem();
                        break;
                    }
                }
                if (player instanceof ServerPlayerEntity){
                    ((ServerPlayerEntity) player).connection.send(new SPlaySoundPacket(ModSounds.FOCUS_PICK.getId(), SoundCategory.PLAYERS, player.position(), 1.0F, 1.0F));
                }
            }
        });
    }
}
