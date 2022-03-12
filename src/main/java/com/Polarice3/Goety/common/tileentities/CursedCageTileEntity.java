package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import static com.Polarice3.Goety.common.items.GoldTotemItem.SOULSAMOUNT;

public class CursedCageTileEntity extends TileEntity implements IClearable {
    private ItemStack item = ItemStack.EMPTY;

    public CursedCageTileEntity() {
        super(ModTileEntityType.CURSED_CAGE.get());
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

    public void decreaseSouls(int souls) {
        if (this.item.getItem() != ModRegistry.GOLDTOTEM.get()) {
            return;
        }
        assert this.item.getTag() != null;
        int Soulcount = this.item.getTag().getInt(SOULSAMOUNT);
        if (!this.item.isEmpty()) {
            Soulcount -= souls;
            this.item.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        load(this.getBlockState(), tag);
    }

    public void clearContent() {
        this.setItem(ItemStack.EMPTY);
    }

}
