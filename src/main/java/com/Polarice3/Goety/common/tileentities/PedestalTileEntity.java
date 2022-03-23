package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.TileEntityUpdatePacket;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.PacketDistributor;

public class PedestalTileEntity extends TileEntity implements IClearable {
    private ItemStack item = ItemStack.EMPTY;

    public PedestalTileEntity() {
        super(ModTileEntityType.PEDESTAL.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        item = ItemStack.of(tag.getCompound("item"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = super.save(tag);
        tag.put("item", item.save(new CompoundNBT()));
        return tag;
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 3, this.getUpdateTag());
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
        this.setChanged();
    }

    public ActionResultType onUse(PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            if (!item.isEmpty()) {
                player.addItem(item);
                item = ItemStack.EMPTY;
                return ActionResultType.SUCCESS;
            } else if (!player.getItemInHand(hand).isEmpty() && item.isEmpty()) {
                item = player.getItemInHand(hand).copy();
                item.setCount(1);
                player.getItemInHand(hand).shrink(1);
                if (player.getItemInHand(hand).isEmpty()) player.setItemInHand(hand, ItemStack.EMPTY);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public void synchronize() {
        this.setChanged();
        assert level != null;
        if (level.isClientSide)
            ModNetwork.INSTANCE.sendToServer(new TileEntityUpdatePacket(worldPosition, save(new CompoundNBT())));
        else
            ModNetwork.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new TileEntityUpdatePacket(worldPosition, save(new CompoundNBT())));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        load(this.getBlockState(), tag);
    }

    public void clearContent() {
        this.item = ItemStack.EMPTY;
        new ParticleUtil(ParticleTypes.SMOKE, this.getBlockPos().getX(), this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ(), 0.0F, 5.0E-4D, 0.0F);
        this.synchronize();
    }

    public void setRemoved() {
        super.setRemoved();
    }
}
