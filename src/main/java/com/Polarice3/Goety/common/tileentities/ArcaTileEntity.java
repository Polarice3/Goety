package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.TileEntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.Polarice3.Goety.common.items.GoldTotemItem.MAXSOULS;
import static com.Polarice3.Goety.common.items.GoldTotemItem.SOULSAMOUNT;

public class ArcaTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
    private UUID ownerUUID;
    private ItemStack item = ItemStack.EMPTY;

    public ArcaTileEntity() {
        super(ModTileEntityType.ARCA.get());
    }

    @Override
    public void tick() {
        if (!this.item.isEmpty()){
            if (this.item.getTag() == null){
                CompoundNBT compound = this.item.getOrCreateTag();
                compound.putInt(SOULSAMOUNT, 0);
            }
            if (this.item.getTag().getInt(SOULSAMOUNT) > MAXSOULS){
                this.item.getTag().putInt(SOULSAMOUNT, MAXSOULS);
            }
            if (this.item.getTag().getInt(SOULSAMOUNT) <= 0){
                this.item.getTag().putInt(SOULSAMOUNT, 0);
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.setItem(ItemStack.of(tag.getCompound("item")));
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.level.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = super.save(tag);
        tag.put("item", this.getItem().save(new CompoundNBT()));
        if (this.getOwnerId() != null) {
            tag.putUUID("Owner", this.getOwnerId());
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

    @Nullable
    public UUID getOwnerId() {
        return this.ownerUUID;
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.ownerUUID = p_184754_1_;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public PlayerEntity getPlayer(){
        return (PlayerEntity) this.getTrueOwner();
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
            TileEntityHelper.sendArcaUpdatePacket(this.getPlayer());
        }
    }

    public void setSouls(int souls) {
        if (this.item.getItem() != ModItems.GOLDTOTEM.get()) {
            return;
        }
        assert this.item.getTag() != null;
        if (!this.item.isEmpty()) {
            this.item.getTag().putInt(SOULSAMOUNT, souls);
            TileEntityHelper.sendArcaUpdatePacket(this.getPlayer());
        }
    }

    public void makeWorkParticles() {
        if (this.getSouls() <= 0){
            return;
        }
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            for (int p = 0; p < 4; ++p) {
                new ParticleUtil(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0, 0, 0);
                new ParticleUtil(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
            }
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
