package com.Polarice3.Goety.common.network.packets.client;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CStopAttackPacket {
    public static void encode(CStopAttackPacket packet, PacketBuffer buffer) {
    }

    public static CStopAttackPacket decode(PacketBuffer buffer) {
        return new CStopAttackPacket();
    }

    public static void consume(CStopAttackPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                if (playerEntity.level instanceof ServerWorld){
                    ServerWorld serverWorld = (ServerWorld) playerEntity.level;
                    for (Entity entity : serverWorld.getAllEntities()){
                        if (entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() == playerEntity){
                            if (((OwnedEntity) entity).getTarget() != null) {
                                ((OwnedEntity) entity).setTarget(null);
                                entity.playSound(ModSounds.CAST_SPELL.get(), 1.0F, 1.0F);
                                serverWorld.broadcastEntityEvent(entity, (byte) 20);
                            }
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
