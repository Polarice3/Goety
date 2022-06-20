package com.Polarice3.Goety.common.lichdom;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class LichUpdatePacket {
    private final UUID PlayerUUID;
    private CompoundNBT tag;

    public LichUpdatePacket(UUID uuid, CompoundNBT tag) {
        this.PlayerUUID = uuid;
        this.tag = tag;
    }

    public LichUpdatePacket(PlayerEntity player) {
        this.PlayerUUID = player.getUUID();
        player.getCapability(LichProvider.CAPABILITY, null).ifPresent((lichdom) -> {
            this.tag = (CompoundNBT) LichProvider.CAPABILITY.getStorage().writeNBT(LichProvider.CAPABILITY, lichdom, null);
        });
    }

    public static void encode(LichUpdatePacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.PlayerUUID);
        buffer.writeNbt(packet.tag);
    }

    public static LichUpdatePacket decode(PacketBuffer buffer) {
        return new LichUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(LichUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            PlayerEntity player = Goety.PROXY.getPlayer();
            if (player != null) {
                player.getCapability(LichProvider.CAPABILITY).ifPresent((lichdom) -> {
                    LichProvider.CAPABILITY.getStorage().readNBT(LichProvider.CAPABILITY, lichdom, null, packet.tag);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
