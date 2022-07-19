package com.Polarice3.Goety.common.items.handler;

import com.Polarice3.Goety.common.items.MagicFocusItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class FocusBagItemHandler extends ItemStackHandler {
    private final ItemStack itemStack;
    private int slot;

    public FocusBagItemHandler(ItemStack itemStack) {
        super(11);
        this.itemStack = itemStack;
    }

    public ItemStack extractItem() {
        return extractItem(slot, 1, false);
    }

    public ItemStack insertItem(ItemStack insert) {
        return insertItem(slot, insert, false);
    }

    public ItemStack getSlot() {
        return getStackInSlot(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return stack.getItem() instanceof MagicFocusItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 11;
    }

    public NonNullList<ItemStack> getContents(){
        return stacks;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("slot", slot);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT itemTags = tagList.getCompound(i);
            if (nbt.contains("slot")) {
                slot = nbt.getInt("slot");
                stacks.set(slot, ItemStack.of(itemTags));
            }
        }
        onLoad();

    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("goety-dirty", !nbt.getBoolean("goety-dirty"));
    }

    public static FocusBagItemHandler orNull(ItemStack stack) {
        return (FocusBagItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    public static LazyOptional<FocusBagItemHandler> getOptional(ItemStack stack) {
        LazyOptional<IItemHandler> itemHandlerOpt = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (itemHandlerOpt.isPresent()) {
            IItemHandler handler = itemHandlerOpt.orElse(null);
            if (handler instanceof FocusBagItemHandler)
                return LazyOptional.of(() -> (FocusBagItemHandler) handler);
        }
        return LazyOptional.empty();
    }

    public static FocusBagItemHandler get(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("ItemStack is missing item capability"));
        return (FocusBagItemHandler) handler;
    }
}
