package com.Polarice3.Goety.common.network.packets.server;

import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class SPlayEntitySoundPacket {
    private UUID entity;
    private SoundEvent soundEvent;
    private float volume;
    private float pitch;

    public SPlayEntitySoundPacket(UUID uuid, SoundEvent soundEvent, float volume, float pitch){
        this.entity = uuid;
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(SPlayEntitySoundPacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.entity);
        ResourceLocation resourceLocation = ForgeRegistries.SOUND_EVENTS.getKey(packet.soundEvent);
        if (resourceLocation != null){
            buffer.writeResourceLocation(resourceLocation);
        } else {
            buffer.writeResourceLocation(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.ITEM_PICKUP));
        }
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }

    public static SPlayEntitySoundPacket decode(PacketBuffer buffer) {
        return new SPlayEntitySoundPacket(buffer.readUUID(), ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation()), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(SPlayEntitySoundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            ClientWorld clientWorld = Minecraft.getInstance().level;
            if (clientWorld != null){
                Entity entity = EntityFinder.getEntityByUuiDGlobal(packet.entity).get();
                if (entity != null){
                    clientWorld.playLocalSound(entity.blockPosition(), packet.soundEvent, entity.getSoundSource(), packet.volume, packet.pitch, false);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
