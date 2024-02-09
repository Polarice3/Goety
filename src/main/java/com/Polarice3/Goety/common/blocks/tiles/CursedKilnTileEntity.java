package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.CursedKilnBlock;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

public class CursedKilnTileEntity extends TileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    private static final int[] SLOTS = IntStream.range(0, 64).toArray();
    private CursedCageTileEntity cursedCageTile;
    private final NonNullList<ItemStack> items = NonNullList.withSize(64, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[64];
    private final int[] cookingTime = new int[64];

    public CursedKilnTileEntity() {
        super(ModTileEntityType.CURSED_KILN.get());
    }

    public void tick() {
        boolean flag = checkCage();
        assert this.level != null;
        boolean flag1 = this.level.isClientSide;
        if (flag1) {
            if (flag) {
                this.makeParticles();
                if (!this.items.get(0).isEmpty()){
                    this.makeWorkParticles();
                    this.cursedCageTile.makeWorkParticles();
                }
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
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CursedKilnBlock.LIT, this.checkCage()), 3);
    }

    private void work() {
        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                IInventory iinventory = new Inventory(itemstack);
                assert this.level != null;
                ItemStack itemstack1 = this.level.getRecipeManager()
                        .getRecipeFor(IRecipeType.SMELTING, iinventory, this.level)
                        .map((recipes) -> recipes.assemble(iinventory)).orElse(itemstack);
                if (this.cursedCageTile.getSouls() > 0){
                    this.cookingProgress[i]++;
                }
                if (this.cookingProgress[i] % 20 == 0){
                    this.cursedCageTile.decreaseSouls(MainConfig.SoulKilnCost.get());
                }
                if (this.cookingProgress[i] >= this.cookingTime[i]) {
                    this.items.set(i, ItemStack.EMPTY);
                    BlockPos blockpos = this.getBlockPos();
                    InventoryHelper.dropItemStack(this.level, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
                    this.level.playSound(null, this.getBlockPos(), SoundEvents.BLAZE_SHOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                this.level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 0.75F, 1.0F);
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ItemStackHelper.removeItem(this.items, pIndex, pCount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStackHelper.takeItem(this.items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        Optional<FurnaceRecipe> optional = this.getRecipes(pStack);
        optional.ifPresent(furnaceRecipe -> this.placeItem(pStack, furnaceRecipe.getCookingTime()));
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void makeParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.getBlockState().getValue(CursedKilnBlock.WATERLOGGED)){
                if (t % 20L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        new ParticleUtil(ParticleTypes.BUBBLE, d0, d1, d2, 0, 0, 0);
                        new ParticleUtil(ParticleTypes.BUBBLE_COLUMN_UP, d0, d1, d2, 0.0D, 0.04D, 0.0D);
                    }
                }
            } else {
                if (t % 20L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        new ParticleUtil(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                        new ParticleUtil(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void makeWorkParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.getBlockState().getValue(CursedKilnBlock.WATERLOGGED)){
                for (int p = 0; p < 4; ++p) {
                    new ParticleUtil(ParticleTypes.BUBBLE, d0, d1, d2, 0, 0, 0);
                    new ParticleUtil(ParticleTypes.BUBBLE_COLUMN_UP, d0, d1, d2, 0.0D, 0.04D, 0.0D);
                }
            } else {
                for (int p = 0; p < 6; ++p) {
                    new ParticleUtil(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                    new ParticleUtil(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
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
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.saveMetadataAndItems(new CompoundNBT());
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    private boolean checkCage() {
        assert this.level != null;
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
        BlockState blockState = this.level.getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())){
            TileEntity tileentity = this.level.getBlockEntity(pos);
            if (tileentity instanceof CursedCageTileEntity){
                this.cursedCageTile = (CursedCageTileEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Optional<FurnaceRecipe> getRecipes(ItemStack pStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, new Inventory(pStack), this.level);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        Optional<FurnaceRecipe> optional = this.getRecipes(pItemStack);
        if (!optional.isPresent()) return false;
        if (!this.checkCage()) return false;
        assert this.level != null;
        return !this.level.isClientSide && this.placeItem(pItemStack, optional.get().getCookingTime());
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }
}
