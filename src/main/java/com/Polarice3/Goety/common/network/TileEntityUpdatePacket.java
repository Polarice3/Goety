package com.Polarice3.Goety.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TileEntityUpdatePacket {
    BlockPos worldPosition;
    CompoundNBT tag;

    public TileEntityUpdatePacket(BlockPos pos, CompoundNBT tag) {
        this.worldPosition = pos;
        this.tag = tag;
    }

    public static void encode(TileEntityUpdatePacket object, PacketBuffer buffer) {
        buffer.writeBlockPos(object.worldPosition);
        buffer.writeNbt(object.tag);
    }

    public static TileEntityUpdatePacket decode(PacketBuffer buffer) {
        return new TileEntityUpdatePacket(buffer.readBlockPos(), buffer.readNbt());
    }

    public static void consume(TileEntityUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world;
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
                world = Minecraft.getInstance().level;
            else {
                if (ctx.get().getSender() == null) return;
                world = ctx.get().getSender().level;
            }

            world.getBlockEntity(packet.worldPosition).load(world.getBlockState(packet.worldPosition), packet.tag);
            world.getBlockEntity(packet.worldPosition).setChanged();
        });
        ctx.get().setPacketHandled(true);
    }
}
