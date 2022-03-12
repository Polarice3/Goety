package com.Polarice3.Goety.common.infamy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class InfamyUpdatePacket {
    private final UUID PlayerUUID;
    private CompoundNBT tag;

    public InfamyUpdatePacket(UUID uuid, CompoundNBT tag) {
        this.PlayerUUID = uuid;
        this.tag = tag;
    }

    public InfamyUpdatePacket(PlayerEntity player) {
        this.PlayerUUID = player.getUUID();
        player.getCapability(InfamyProvider.CAPABILITY, null).ifPresent((infamy) -> {
            this.tag = (CompoundNBT) InfamyProvider.CAPABILITY.getStorage().writeNBT(InfamyProvider.CAPABILITY, infamy, null);
        });
    }

    public static void encode(InfamyUpdatePacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.PlayerUUID);
        buffer.writeNbt(packet.tag);
    }

    public static InfamyUpdatePacket decode(PacketBuffer buffer) {
        return new InfamyUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(InfamyUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Minecraft.getInstance().level;
            assert world != null;
            PlayerEntity player = world.getPlayerByUUID(packet.PlayerUUID);
            if (player != null) {
                player.getCapability(InfamyProvider.CAPABILITY).ifPresent((infamy) -> {
                    InfamyProvider.CAPABILITY.getStorage().readNBT(InfamyProvider.CAPABILITY, infamy, null, packet.tag);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
