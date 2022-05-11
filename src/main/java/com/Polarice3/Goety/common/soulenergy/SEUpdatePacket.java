package com.Polarice3.Goety.common.soulenergy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SEUpdatePacket {
    private final UUID PlayerUUID;
    private CompoundNBT tag;

    public SEUpdatePacket(UUID uuid, CompoundNBT tag) {
        this.PlayerUUID = uuid;
        this.tag = tag;
    }

    public SEUpdatePacket(PlayerEntity player) {
        this.PlayerUUID = player.getUUID();
        player.getCapability(SEProvider.CAPABILITY, null).ifPresent((soulEnergy) -> {
            this.tag = (CompoundNBT) SEProvider.CAPABILITY.getStorage().writeNBT(SEProvider.CAPABILITY, soulEnergy, null);
        });
    }

    public static void encode(SEUpdatePacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.PlayerUUID);
        buffer.writeNbt(packet.tag);
    }

    public static SEUpdatePacket decode(PacketBuffer buffer) {
        return new SEUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(SEUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Minecraft.getInstance().level;
            assert world != null;
            PlayerEntity player = world.getPlayerByUUID(packet.PlayerUUID);
            if (player != null) {
                player.getCapability(SEProvider.CAPABILITY).ifPresent((soulEnergy) -> {
                    SEProvider.CAPABILITY.getStorage().readNBT(SEProvider.CAPABILITY, soulEnergy, null, packet.tag);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
