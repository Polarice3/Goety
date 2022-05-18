package com.Polarice3.Goety.common.network;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.infamy.InfamyUpdatePacket;
import com.Polarice3.Goety.common.lichdom.LichUpdatePacket;
import com.Polarice3.Goety.common.soulenergy.SEUpdatePacket;
import com.Polarice3.Goety.common.spider.SpiderLevelsUpdatePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static int nextID() {
        return id++;
    }

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Goety.MOD_ID, "channel"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), InfamyUpdatePacket.class, InfamyUpdatePacket::encode, InfamyUpdatePacket::decode, InfamyUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), LichUpdatePacket.class, LichUpdatePacket::encode, LichUpdatePacket::decode, LichUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), SEUpdatePacket.class, SEUpdatePacket::encode, SEUpdatePacket::decode, SEUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), SpiderLevelsUpdatePacket.class, SpiderLevelsUpdatePacket::encode, SpiderLevelsUpdatePacket::decode, SpiderLevelsUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), EntityUpdatePacket.class, EntityUpdatePacket::encode, EntityUpdatePacket::decode, EntityUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), TileEntityUpdatePacket.class, TileEntityUpdatePacket::encode, TileEntityUpdatePacket::decode, TileEntityUpdatePacket::consume);
    }

    public static <MSG> void sendTo(PlayerEntity player, MSG msg) {
        ModNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
    }

    public static <MSG> void sendToServer(MSG msg) {
        ModNetwork.INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sentToTrackingChunk(Chunk chunk, MSG msg) {
        ModNetwork.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
    }
}
