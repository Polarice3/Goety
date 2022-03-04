package com.Polarice3.Goety.common.items;

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
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FocusBagItem extends Item implements ICurioItem {
    public FocusBagItem(){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
    }

    public boolean canEquipFromUse(SlotContext slot, ItemStack stack) {
        return false;
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

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT result = new CompoundNBT();
        CompoundNBT tag = super.getShareTag(stack);
        CompoundNBT cap = FocusBagItemHandler.get(stack).serializeNBT();
        if (tag != null) {
            result.put("tag", tag);
        }
        if (cap != null) {
            result.put("cap", cap);
        }
        return result;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        assert nbt != null;
        stack.setTag(nbt.getCompound("tag"));
        FocusBagItemHandler.get(stack).deserializeNBT(nbt.getCompound("cap"));
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
