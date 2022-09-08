package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.Polarice3.Goety.common.items.GoldTotemItem.SOULSAMOUNT;

public class CursedCageTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
    private ItemStack item = ItemStack.EMPTY;
    private int spinning;

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
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
        this.setChanged();
    }

    public int getSouls(){
        if (this.item.getItem() == ModItems.SOUL_TRANSFER.get()){
            int x = this.item.getTag().getInt("X");
            int y = this.item.getTag().getInt("Y");
            int z = this.item.getTag().getInt("Z");
            TileEntity tileEntity = level.getBlockEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof ArcaTileEntity){
                ArcaTileEntity arcaTileEntity = (ArcaTileEntity) tileEntity;
                if (arcaTileEntity.getPlayer() != null) {
                    if (SEHelper.getSEActive(arcaTileEntity.getPlayer())) {
                        return SEHelper.getSESouls(arcaTileEntity.getPlayer());
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
        if (this.item.getItem() == ModItems.GOLDTOTEM.get()) {
            assert this.item.getTag() != null;
            return this.item.getTag().getInt(SOULSAMOUNT);
        } else {
            return 0;
        }
    }

    public void decreaseSouls(int souls) {
        if (this.item.getItem() == ModItems.GOLDTOTEM.get()) {
            assert this.item.getTag() != null;
            int Soulcount = this.item.getTag().getInt(SOULSAMOUNT);
            if (!this.item.isEmpty()) {
                if (Soulcount > 0){
                    Soulcount -= souls;
                    this.item.getTag().putInt(SOULSAMOUNT, Soulcount);
                }
            }
        }
        if (this.item.getItem() == ModItems.SOUL_TRANSFER.get()) {
            int x = this.item.getTag().getInt("X");
            int y = this.item.getTag().getInt("Y");
            int z = this.item.getTag().getInt("Z");
            TileEntity tileEntity = level.getBlockEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof ArcaTileEntity){
                ArcaTileEntity arcaTileEntity = (ArcaTileEntity) tileEntity;
                if (arcaTileEntity.getPlayer() != null) {
                    if (SEHelper.getSEActive(arcaTileEntity.getPlayer())) {
                        int Soulcount = SEHelper.getSESouls(arcaTileEntity.getPlayer());
                        if (Soulcount > 0) {
                            SEHelper.decreaseSESouls(arcaTileEntity.getPlayer(), souls);
                            SEHelper.sendSEUpdatePacket(arcaTileEntity.getPlayer());
                            arcaTileEntity.makeWorkParticles();
                        }
                    }
                }
            }
        }

    }

    public int getSpinning(){
        return this.spinning;
    }

    @OnlyIn(Dist.CLIENT)
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
            this.spinning = 20;
        }
    }

    public void generateParticles() {
        if (this.getSouls() <= 0){
            return;
        }
        BlockPos blockpos = this.getBlockPos();

        if (this.level != null) {
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                double d0 = (double) blockpos.getX() + this.level.random.nextDouble();
                double d1 = (double) blockpos.getY() + this.level.random.nextDouble();
                double d2 = (double) blockpos.getZ() + this.level.random.nextDouble();
                for (int p = 0; p < 4; ++p) {
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0.0D, 5.0E-4D, 0.0D, 5.0E-4D);
                }
            }
            this.spinning = 20;
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

    @Override
    public void tick() {
        if (this.spinning > 0){
            --this.spinning;
        }
    }
}
