package com.Polarice3.Goety.common.network.packets.server;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SFocusCooldownPacket {
    private final Item item;
    private final int duration;

    public SFocusCooldownPacket(Item p_132000_, int p_132001_) {
        this.item = p_132000_;
        this.duration = p_132001_;
    }

    public static void encode(SFocusCooldownPacket packet, PacketBuffer buffer) {
        buffer.writeVarInt(Item.getId(packet.item));
        buffer.writeVarInt(packet.duration);
    }

    public static SFocusCooldownPacket decode(PacketBuffer buffer) {
        return new SFocusCooldownPacket(Item.byId(buffer.readVarInt()), buffer.readVarInt());
    }

    public static void consume(SFocusCooldownPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Goety.PROXY.getPlayer();
            if (player != null) {
                if (packet.duration == 0) {
                    SEHelper.getFocusCoolDown(player).removeCooldown(player.level, packet.item);
                } else {
                    SEHelper.addCooldown(player, packet.item, packet.duration);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
