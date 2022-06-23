package com.Polarice3.Goety.common.network;

import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityUpdatePacket {
    private final UUID LivingEntityUUID;
    private CompoundNBT tag;

    public EntityUpdatePacket(UUID uuid, CompoundNBT tag) {
        this.LivingEntityUUID = uuid;
        this.tag = tag;
    }

    public static void encode(EntityUpdatePacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
        buffer.writeNbt(packet.tag);
    }

    public static EntityUpdatePacket decode(PacketBuffer buffer) {
        return new EntityUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(EntityUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Entity entity = EntityFinder.getEntityByUuiDGlobal(packet.LivingEntityUUID).get();
            if (entity instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.readAdditionalSaveData(packet.tag);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
