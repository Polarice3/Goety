package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.client.inventory.crafting.CursedBurnerRecipes;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeType;
import com.Polarice3.Goety.common.blocks.CursedBurnerBlock;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public class CursedBurnerTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[1];
    private final int[] cookingTime = new int[1];

    public CursedBurnerTileEntity() {
        super(ModTileEntityType.CURSEDBURNER.get());
    }

    public void tick() {
        boolean flag = checkSpawner();
        assert this.level != null;
        boolean flag1 = this.level.isClientSide;
        if (flag1) {
            if (flag) {
                this.makeParticles();
            }

        } else {
            if (flag) {
                this.work();
            } else {
                for(int i = 0; i < this.items.size(); ++i) {
                    if (this.cookingProgress[i] > 0) {
                        this.cookingProgress[i] = MathHelper.clamp(this.cookingProgress[i] - 2, 0, this.cookingTime[i]);
                    }
                }
            }
        }
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CursedBurnerBlock.LIT, this.checkSpawner()), 3);
    }

    private void work() {
        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                IInventory iinventory = new Inventory(itemstack);
                assert this.level != null;
                ItemStack itemstack1 = this.level.getRecipeManager()
                        .getRecipeFor(ModRecipeType.CURSED_BURNER_RECIPES, iinventory, this.level)
                        .map((recipes) -> recipes.assemble(iinventory)).orElse(itemstack);
                if (itemstack != itemstack1){
                    this.cookingProgress[i]++;
                    this.makeWorkParticles();
                }
                if (this.cookingProgress[i] >= this.cookingTime[i]) {
                    this.items.set(i, itemstack1);
                    this.level.playSound(null, this.getBlockPos(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.markUpdated();
                    this.cookingProgress[i] = 0;
                }
            }
        }

    }

    public boolean placeItem(ItemStack pStack, int pCookTime) {
        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = pCookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, pStack.split(1));
                assert this.level != null;
                this.level.playSound(null, this.getBlockPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void makeParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.getBlockState().getValue(CursedBurnerBlock.WATERLOGGED)){
                if (t % 20L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        MINECRAFT.level.addParticle(ParticleTypes.BUBBLE, d0, d1, d2, 0, 0, 0);
                        MINECRAFT.level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0, d1, d2, 0.0D, 0.04D, 0.0D);
                    }
                }
            } else {
                if (t % 20L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        MINECRAFT.level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                        MINECRAFT.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }
    }

    private void makeWorkParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.getBlockState().getValue(CursedBurnerBlock.WATERLOGGED)){
                for (int p = 0; p < 4; ++p) {
                    MINECRAFT.level.addParticle(ParticleTypes.BUBBLE, d0, d1, d2, 0, 0, 0);
                    MINECRAFT.level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0, d1, d2, 0.0D, 0.04D, 0.0D);
                }
            } else {
                for (int p = 0; p < 6; ++p) {
                    MINECRAFT.level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                    MINECRAFT.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        this.items.clear();
        ItemStackHelper.loadAllItems(compoundNBT, this.items);
        if (compoundNBT.contains("CookingTimes", 11)) {
            int[] aint = compoundNBT.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, aint.length));
        }

        if (compoundNBT.contains("CookingTotalTimes", 11)) {
            int[] aint1 = compoundNBT.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, aint1.length));
        }

    }

    public CompoundNBT save(CompoundNBT pCompound) {
        this.saveMetadataAndItems(pCompound);
        pCompound.putIntArray("CookingTimes", this.cookingProgress);
        pCompound.putIntArray("CookingTotalTimes", this.cookingTime);
        return pCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        load(this.getBlockState(), tag);
    }

    private CompoundNBT saveMetadataAndItems(CompoundNBT pCompound) {
        super.save(pCompound);
        ItemStackHelper.saveAllItems(pCompound, this.items, true);
        return pCompound;
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 13, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.saveMetadataAndItems(new CompoundNBT());
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    private boolean checkSpawner() {
        assert this.level != null;
        return this.level.getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).is(Blocks.SPAWNER);
    }

    public Optional<CursedBurnerRecipes> getRecipes(ItemStack pStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(ModRecipeType.CURSED_BURNER_RECIPES, new Inventory(pStack), this.level);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
