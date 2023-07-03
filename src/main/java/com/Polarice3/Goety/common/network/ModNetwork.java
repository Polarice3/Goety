package com.Polarice3.Goety.common.network;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.capabilities.lichdom.LichUpdatePacket;
import com.Polarice3.Goety.common.capabilities.soulenergy.SEUpdatePacket;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsUpdatePacket;
import com.Polarice3.Goety.common.network.packets.client.*;
import com.Polarice3.Goety.common.network.packets.server.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class ModNetwork {
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static int nextID() {
        return id++;
    }

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Goety.MOD_ID, "channel"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), LichUpdatePacket.class, LichUpdatePacket::encode, LichUpdatePacket::decode, LichUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), SEUpdatePacket.class, SEUpdatePacket::encode, SEUpdatePacket::decode, SEUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), SpiderLevelsUpdatePacket.class, SpiderLevelsUpdatePacket::encode, SpiderLevelsUpdatePacket::decode, SpiderLevelsUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), EntityUpdatePacket.class, EntityUpdatePacket::encode, EntityUpdatePacket::decode, EntityUpdatePacket::consume);
        INSTANCE.registerMessage(nextID(), SOpenItemPacket.class, SOpenItemPacket::encode, SOpenItemPacket::decode, SOpenItemPacket::consume);
        INSTANCE.registerMessage(nextID(), SChangeFocusPacket.class, SChangeFocusPacket::encode, SChangeFocusPacket::decode, SChangeFocusPacket::consume);
        INSTANCE.registerMessage(nextID(), TotemDeathPacket.class, TotemDeathPacket::encode, TotemDeathPacket::decode, TotemDeathPacket::consume);
        INSTANCE.registerMessage(nextID(), SPlayPlayerSoundPacket.class, SPlayPlayerSoundPacket::encode, SPlayPlayerSoundPacket::decode, SPlayPlayerSoundPacket::consume);
        INSTANCE.registerMessage(nextID(), SPlayWorldSoundPacket.class, SPlayWorldSoundPacket::encode, SPlayWorldSoundPacket::decode, SPlayWorldSoundPacket::consume);
        INSTANCE.registerMessage(nextID(), SLootingExplosionPacket.class, SLootingExplosionPacket::encode, SLootingExplosionPacket::decode, SLootingExplosionPacket::consume);
        INSTANCE.registerMessage(nextID(), SApostleSmitePacket.class, SApostleSmitePacket::encode, SApostleSmitePacket::decode, SApostleSmitePacket::consume);
        INSTANCE.registerMessage(nextID(), CWandKeyPacket.class, CWandKeyPacket::encode, CWandKeyPacket::decode, CWandKeyPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CBagKeyPacket.class, CBagKeyPacket::encode, CBagKeyPacket::decode, CBagKeyPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CWandAndBagKeyPacket.class, CWandAndBagKeyPacket::encode, CWandAndBagKeyPacket::decode, CWandAndBagKeyPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CStopAttackPacket.class, CStopAttackPacket::encode, CStopAttackPacket::decode, CStopAttackPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CSoulEnergyPacket.class, CSoulEnergyPacket::encode, CSoulEnergyPacket::decode, CSoulEnergyPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CScytheStrikePacket.class, CScytheStrikePacket::encode, CScytheStrikePacket::decode, CScytheStrikePacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CLichKissPacket.class, CLichKissPacket::encode, CLichKissPacket::decode, CLichKissPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), CMagnetPacket.class, CMagnetPacket::encode, CMagnetPacket::decode, CMagnetPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
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
