package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.client.inventory.crafting.DarkAltarRecipes;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeType;
import com.Polarice3.Goety.client.inventory.crafting.ModSoulCraftRecipe;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.DarkAltarBlock;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public class DarkAltarTileEntity extends ModTileEntity implements IClearable, ITickableTileEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[1];
    private final int[] cookingTime = new int[1];
    private CursedCageTileEntity cursedCageTile;

    public DarkAltarTileEntity() {
        super(ModTileEntityType.DARK_ALTAR.get());
    }

    @Override
    public void tick() {
        boolean flag = checkCage();
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
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(DarkAltarBlock.LIT, this.checkCage()), 3);
    }

    private void work() {
        Minecraft MINECRAFT = Minecraft.getInstance();
        assert MINECRAFT.level != null;
        long t = MINECRAFT.level.getGameTime();
        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                if (this.cursedCageTile != null){
                    IInventory iinventory = new Inventory(itemstack);
                    assert this.level != null;
                    ItemStack itemstack1 = this.level.getRecipeManager()
                            .getRecipeFor(ModRecipeType.DARK_ALTAR_RECIPES, iinventory, this.level)
                            .map((recipes) -> recipes.assemble(iinventory)).orElse(itemstack);
                    Optional<Object> craftType = this.level.getRecipeManager()
                            .getRecipeFor(ModRecipeType.DARK_ALTAR_RECIPES, iinventory, this.level)
                            .map(ModSoulCraftRecipe::getCraftType);
                    Optional<Integer> soulCost = this.level.getRecipeManager()
                            .getRecipeFor(ModRecipeType.DARK_ALTAR_RECIPES, iinventory, this.level)
                            .map(ModSoulCraftRecipe::getSoulCost);
                    int souls = this.cursedCageTile.getSouls();
                    if (craftType.toString().contains("animalis")){
                        this.findAnimaStructure();
                        if (this.checkAnimaRequirements()){
                            if (itemstack != itemstack1 && souls > 0){
                                this.cookingProgress[i]++;
                                if (t % 20L == 0L) {
                                    this.cursedCageTile.decreaseSouls(soulCost.orElse(1));
                                }
                                this.makeWorkParticles();
                                this.clearData();
                            }
                            if (this.cookingProgress[i] == 5){
                                this.level.playSound(null, this.getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                            if (this.cookingProgress[i] >= this.cookingTime[i]) {
                                this.items.set(i, itemstack1);
                                this.level.playSound(null, this.getBlockPos(), SoundEvents.BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                this.clearData();
                                this.cookingProgress[i] = 0;
                            }
                        } else {
                            this.clearData();
                            this.cookingProgress[i] = 0;
                        }
                    }
 /*                   if (craftType.toString().contains("necroturgy")){
                        this.findPedestals();
                        if (this.checkPedestals()) {
                            if (pedestalitem.size() >= 8) {
                                for (TileEntity tileEntity : this.pedestalitem) {
                                    PedestalTileEntity pedestalTileEntity = (PedestalTileEntity) tileEntity;
                                    if (pedestalTileEntity.getItem().getItem() == Items.ROTTEN_FLESH) {
                                        if (itemstack != itemstack1 && souls > 0) {
                                            this.cookingProgress[i]++;
                                            if (t % 20L == 0L) {
                                                this.cursedCageTile.decreaseSouls(soulCost.orElse(1));
                                            }
                                            this.makeWorkParticles();
                                            this.clearData();
                                        }
                                        if (this.cookingProgress[i] == 5) {
                                            this.level.playSound(null, this.getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                        }
                                        if (this.cookingProgress[i] >= this.cookingTime[i]) {
                                            this.items.set(i, itemstack1);
                                            this.level.playSound(null, this.getBlockPos(), SoundEvents.BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                            ((PedestalTileEntity) tileEntity).clearContent();
                                            this.clearData();
                                            this.cookingProgress[i] = 0;
                                        }
                                    } else {
                                        this.clearData();
                                        this.cookingProgress[i] = 0;
                                    }
                                }
                            } else {
                                this.clearData();
                                this.cookingProgress[i] = 0;
                            }
                        } else {
                            this.clearData();
                            this.cookingProgress[i] = 0;
                        }
                    }*/
                }
            }
        }

    }

    private void clearData(){
        this.markUpdated();
        this.pumpkin.clear();
        this.ladders.clear();
        this.rails.clear();
        this.pedestals.clear();
    }

    public boolean placeItem(ItemStack pStack, int pCookTime) {
        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = pCookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, pStack.split(1));
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

    public Optional<DarkAltarRecipes> getRecipes(ItemStack pStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(ModRecipeType.DARK_ALTAR_RECIPES, new Inventory(pStack), this.level);
    }

    private void makeParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (t % 20L == 0L) {
                for (int p = 0; p < 4; ++p) {
                    MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.45, 0.45, 0.45);
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
            for (int p = 0; p < 4; ++p) {
                MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.45, 0.45, 0.45);
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    private boolean checkCage() {
        assert this.level != null;
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
        BlockState blockState = this.level.getBlockState(pos);
        if (blockState.is(ModRegistry.CURSED_CAGE_BLOCK.get())){
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

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
