package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

import static com.Polarice3.Goety.common.items.GoldTotemItem.SOULSAMOUNT;

public class ArcaTileEntity extends TileEntity implements IClearable {
    private GameProfile owner;
    private ItemStack item = ItemStack.EMPTY;

    public ArcaTileEntity() {
        super(ModTileEntityType.ARCA.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        item = ItemStack.of(tag.getCompound("item"));
        if (tag.contains("Owner", 10)) {
            this.setOwner(NBTUtil.readGameProfile(tag.getCompound("Owner")));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = super.save(tag);
        tag.put("item", item.save(new CompoundNBT()));
        if (this.owner != null) {
            CompoundNBT compoundnbt = new CompoundNBT();
            NBTUtil.writeGameProfile(compoundnbt, this.owner);
            tag.put("Owner", compoundnbt);
        }
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

    public void setOwner(@Nullable GameProfile pOwner) {
        this.owner = pOwner;
    }

    public GameProfile getOwnerProfile() {
        return this.owner;
    }

    public int getSouls(){
        if (this.item.getItem() == ModItems.GOLDTOTEM.get()) {
            assert this.item.getTag() != null;
            return this.item.getTag().getInt(SOULSAMOUNT);
        } else {
            return 0;
        }
    }

    public void decreaseSouls(int souls) {
        if (this.item.getItem() != ModItems.GOLDTOTEM.get()) {
            return;
        }
        assert this.item.getTag() != null;
        int Soulcount = this.item.getTag().getInt(SOULSAMOUNT);
        if (!this.item.isEmpty()) {
            if (Soulcount > 0){
                Soulcount -= souls;
                this.item.getTag().putInt(SOULSAMOUNT, Soulcount);
            }
        }
    }

    public void makeWorkParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            new ParticleUtil(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            new ParticleUtil(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
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