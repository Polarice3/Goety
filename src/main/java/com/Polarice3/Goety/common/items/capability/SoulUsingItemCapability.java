package com.Polarice3.Goety.common.items.capability;

import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Learned item capabilities from codes made by @vemerion & @MrCrayfish
 */
public class SoulUsingItemCapability implements ICapabilitySerializable<INBT> {
    private final ItemStack stack;
    private final LazyOptional<IItemHandler> holder = LazyOptional.of(this::getHandler);
    private SoulUsingItemHandler handler;

    public SoulUsingItemCapability(ItemStack stack) {
        this.stack = stack;
    }

    @Nonnull
    private SoulUsingItemHandler getHandler() {
        if (handler == null) {
            handler = new SoulUsingItemHandler(stack);
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
}
