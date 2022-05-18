package com.Polarice3.Goety.common.spider;

import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SpiderLevelsUpdatePacket {
    private final UUID LivingEntityUUID;
    private CompoundNBT tag;

    public SpiderLevelsUpdatePacket(UUID uuid, CompoundNBT tag) {
        this.LivingEntityUUID = uuid;
        this.tag = tag;
    }

    public SpiderLevelsUpdatePacket(LivingEntity livingEntity) {
        this.LivingEntityUUID = livingEntity.getUUID();
        livingEntity.getCapability(SpiderLevelsProvider.CAPABILITY, null).ifPresent((infamy) -> {
            this.tag = (CompoundNBT) SpiderLevelsProvider.CAPABILITY.getStorage().writeNBT(SpiderLevelsProvider.CAPABILITY, infamy, null);
        });
    }

    public static void encode(SpiderLevelsUpdatePacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
        buffer.writeNbt(packet.tag);
    }

    public static SpiderLevelsUpdatePacket decode(PacketBuffer buffer) {
        return new SpiderLevelsUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(SpiderLevelsUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
            Entity entity = EntityFinder.getEntityByUuiDGlobal(packet.LivingEntityUUID).get();
            entity.getCapability(SpiderLevelsProvider.CAPABILITY).ifPresent((infamy) -> {
                SpiderLevelsProvider.CAPABILITY.getStorage().readNBT(SpiderLevelsProvider.CAPABILITY, infamy, null, packet.tag);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
