package com.Polarice3.Goety.common.network.packets.client.focus;

import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.FocusBagFinder;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CAddFocusToBagPacket {
    public CAddFocusToBagPacket(){
    }

    public static void encode(CAddFocusToBagPacket packet, PacketBuffer buffer) {
    }

    public static CAddFocusToBagPacket decode(PacketBuffer buffer) {
        return new CAddFocusToBagPacket();
    }

    public static void consume(CAddFocusToBagPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = FocusBagFinder.findBag(player);
                if (stack.getCount() <= 0) {
                    return;
                }

                ItemStack wand = WandUtil.findWand(player);

                FocusBagItemHandler bagHandler = FocusBagItemHandler.get(stack);
                SoulUsingItemHandler wandHandler = SoulUsingItemHandler.get(wand);

                ItemStack wandFocus = wandHandler.getSlot();

                for (int i = 1; i < bagHandler.getSlots(); ++i) {
                    ItemStack itemStack = bagHandler.getStackInSlot(i);
                    if (itemStack.isEmpty()) {
                        bagHandler.setStackInSlot(i, wandFocus);
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
