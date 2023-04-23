package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.container.FocusBagContainer;
import com.Polarice3.Goety.common.items.capability.FocusBagItemCapability;
import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FocusBagItem extends Item {
    public FocusBagItem(){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
    }

    @Nonnull
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                    (id, inventory, player) -> new FocusBagContainer(id, inventory, FocusBagItemHandler.get(itemstack), itemstack), getName(itemstack));
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, provider, (buffer) -> {});
        }
        return ActionResult.pass(itemstack);
    }

    /**
     * Found Creative Server Bug fix from @mraof's Minestuck Music Player Weapon code.
     */
    private static IItemHandler getItemHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
                new IllegalArgumentException("Expected an item handler for the Magic Focus item, but " + itemStack + " does not expose an item handler."));
    }

    public CompoundNBT getShareTag(ItemStack stack) {
        IItemHandler iitemHandler = getItemHandler(stack);
        CompoundNBT nbt = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        if(iitemHandler instanceof ItemStackHandler) {
            ItemStackHandler itemHandler = (ItemStackHandler) iitemHandler;
            nbt.put("cap", itemHandler.serializeNBT());
        }
        return nbt;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(nbt == null) {
            stack.setTag(null);
        } else {
            IItemHandler iitemHandler = getItemHandler(stack);
            if(iitemHandler instanceof ItemStackHandler) {
                ItemStackHandler itemHandler = (ItemStackHandler) iitemHandler;
                itemHandler.deserializeNBT(nbt.getCompound("cap"));
            }
            stack.setTag(nbt);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FocusBagItemCapability(stack);
    }
}
