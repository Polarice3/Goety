package com.Polarice3.Goety.common.tileentities;
/*

import com.Polarice3.Goety.client.inventory.container.SoulForgeContainer;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.common.items.crafting.SoulForgeRecipe;
import com.Polarice3.Goety.init.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;



public class SoulForgeTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private Item ingredientID;
    private int cookTime;
    private int cookTimeTotal;
    private int fuel;
    public final IIntArray soulforgeData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return cookTime;
                case 1:
                    return fuel;
                case 2:
                    return cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    cookTime = value;
                    break;
                case 1:
                    fuel = value;
                    break;
                case 2:
                    cookTimeTotal = value;
            }

        }

        public int size() {
            return 3;
        }
    };

    public SoulForgeTileEntity() {
        super(ModTileEntityType.SOULFORGE.get());
    }

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

    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.level.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            return stack.getItem() == RegistryHandler.GOLDTOTEM.get();
        }
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {

    }

    public void clear() {
        this.items.clear();
    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void onCrafting(PlayerEntity player) {
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }

    }

    public void tick() {
        ItemStack itemstack = this.items.get(1);
        if (this.fuel <= 0 && itemstack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
            if (GoldTotemItem.currentSouls(itemstack) > 0){
                ++this.fuel;
                GoldTotemItem.decreaseSouls(itemstack, 1);
                this.markDirty();
            }
        }
        boolean flag = this.canSmelt();
        boolean flag1 = this.cookTime > 0;
        ItemStack itemstack1 = this.items.get(0);
        if (flag1) {
            --this.cookTime;
            boolean flag2 = this.cookTime == 0;
            if (flag2 && flag) {
                this.smelt();
                this.markDirty();
            } else if (!flag) {
                this.cookTime = 0;
                this.markDirty();
            } else if (this.ingredientID != itemstack1.getItem()) {
                this.cookTime = 0;
                this.markDirty();
            }
        } else if (flag && this.fuel > 0) {
            --this.fuel;
            this.cookTime = 200;
            this.ingredientID = itemstack1.getItem();
            this.markDirty();
        }
    }

    private void smelt() {
        if (this.canSmelt()) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = (SoulForgeRecipe.getOutput(itemstack));
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
        }
    }

    protected boolean canSmelt() {
        ItemStack itemstack = this.items.get(0);
        if (!itemstack.isEmpty()) return SoulForgeRecipe.canbeCrafted(items, SLOTS_HORIZONTAL);
        if (!this.items.get(0).isEmpty()) {
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getstacksTo()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getstacksTo();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return SLOTS_UP;
        } else {
            return side == Direction.DOWN ? SLOTS_DOWN : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.goety.soulforge");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new SoulForgeContainer(id, player, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT){
        super.save(compoundNBT);
        return compoundNBT;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compoundNBT){
        super.read(blockState,compoundNBT);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }
}

*/
