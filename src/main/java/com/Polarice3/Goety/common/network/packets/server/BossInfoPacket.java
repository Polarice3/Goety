package com.Polarice3.Goety.common.network.packets.server;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.events.BossBarEvent;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BossInfoPacket {
    private final UUID LivingEntityUUID;
    private final boolean add;

    public BossInfoPacket(UUID uuid, boolean add) {
        this.LivingEntityUUID = uuid;
        this.add = add;
    }

    public static void encode(BossInfoPacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
        buffer.writeBoolean(packet.add);
    }

    public static BossInfoPacket decode(PacketBuffer buffer) {
        return new BossInfoPacket(buffer.readUUID(), buffer.readBoolean());
    }

    public static void consume(BossInfoPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            PlayerEntity playerEntity = Goety.PROXY.getPlayer();

            Entity entity = EntityFinder.getEntityByUuiDGlobal(packet.LivingEntityUUID).get();
            if (playerEntity != null && entity instanceof MobEntity) {
                if (packet.add){
                    BossBarEvent.bosses.add((MobEntity) entity);
                } else {
                    BossBarEvent.bosses.remove((MobEntity) entity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
