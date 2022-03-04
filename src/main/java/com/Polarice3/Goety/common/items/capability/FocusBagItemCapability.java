package com.Polarice3.Goety.common.items.capability;

import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FocusBagItemCapability implements ICapabilitySerializable<INBT> {
    private final ItemStack stack;
    private final LazyOptional<IItemHandler> holder = LazyOptional.of(this::getHandler);
    private FocusBagItemHandler handler;

    public FocusBagItemCapability(ItemStack stack) {
        this.stack = stack;
    }

    @Nonnull
    private FocusBagItemHandler getHandler() {
        if (handler == null) {
            handler = new FocusBagItemHandler(stack);
        }
        return handler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? holder.cast() : LazyOptional.empty();
    }

    public INBT serializeNBT() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(getHandler(), null);
    }

    public void deserializeNBT(INBT nbt) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(getHandler(), null, nbt);
    }

    private void legacyDeserialize(ListNBT nbt) {
        if (nbt.size() < 1)
            return;
        CompoundNBT compound = nbt.getCompound(0);
        getHandler().insertItem(0, ItemStack.of(compound), false);
    }
}
