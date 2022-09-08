package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CTotemDeathPacket {
    private final UUID LivingEntityUUID;

    public CTotemDeathPacket(UUID uuid) {
        this.LivingEntityUUID = uuid;
    }

    public static void encode(CTotemDeathPacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
    }

    public static CTotemDeathPacket decode(PacketBuffer buffer) {
        return new CTotemDeathPacket(buffer.readUUID());
    }

    public static void consume(CTotemDeathPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                if (playerEntity.getUUID() == packet.LivingEntityUUID) {
                    new ParticleUtil(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0.0F, 0.0F, 0.0F);
                    new SoundUtil(playerEntity.blockPosition(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    Minecraft.getInstance().gameRenderer.displayItemActivation(GoldTotemFinder.FindTotem(playerEntity));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
