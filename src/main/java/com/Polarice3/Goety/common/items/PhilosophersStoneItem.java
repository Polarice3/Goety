package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.ItemConfig;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nonnull;

public class PhilosophersStoneItem extends Item implements IForgeItem {
    public PhilosophersStoneItem(){
        super(new Properties().tab(Goety.TAB).durability(64));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (ItemConfig.SoulRepair.get()) {
            if (entityIn instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entityIn;
                if (stack.isDamaged()) {
                    if (SEHelper.getSoulsContainer(player)){
                        if (SEHelper.getSoulsAmount(player, 1)){
                            if (player.tickCount % 20 == 0) {
                                stack.setDamageValue(stack.getDamageValue() - 1);
                                SEHelper.decreaseSouls(player, 1);
                            }
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        container.setDamageValue(itemStack.getDamageValue() + 1);
        return container;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() == Items.CRYING_OBSIDIAN || super.isValidRepairItem(pToRepair, pRepair);
    }

}
