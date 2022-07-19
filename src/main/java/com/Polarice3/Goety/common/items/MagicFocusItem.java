package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MagicFocusItem extends Item{
    public static final String FOCUS = "Focus";
    public static final String SOULCOST = "Soul Cost";
    public int soulcost;

    public MagicFocusItem(int soulcost){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
        this.soulcost = soulcost;
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if (stack.getItem() == ModItems.TELEPORTFOCUS.get()){
            return enchantment == ModEnchantments.RANGE.get();
        }
        if (stack.getItem() == ModItems.SOULSHIELDFOCUS.get()){
            return enchantment == ModEnchantments.DURATION.get();
        }
        if (stack.getItem() == ModItems.DRAGONFIREBALLFOCUS.get()){
            return enchantment == ModEnchantments.RADIUS.get()
                    || enchantment == ModEnchantments.DURATION.get();
        }
        if (stack.getItem() == ModItems.SPIDERLINGFOCUS.get()
                || stack.getItem() == ModItems.CREEPERLINGFOCUS.get()
                || stack.getItem() == ModItems.VEXINGFOCUS.get()
                || stack.getItem() == ModItems.ROTTINGFOCUS.get()
                || stack.getItem() == ModItems.OSSEOUSFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.DURATION.get();
        }
        if (stack.getItem() == ModItems.ICESTORMFOCUS.get()
                || stack.getItem() == ModItems.FROSTBREATHFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.DURATION.get()
                    || enchantment == ModEnchantments.RANGE.get();
        }
        if (stack.getItem() == ModItems.BITINGFOCUS.get()) {
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.RANGE.get()
                    || enchantment == ModEnchantments.BURNING.get()
                    || enchantment == ModEnchantments.ABSORB.get();
        }
        if (stack.getItem() == ModItems.FEASTFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.BURNING.get()
                    || enchantment == ModEnchantments.ABSORB.get();
        }
        if (stack.getItem() == ModItems.FIREBALLFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.BURNING.get();
        }
        if (stack.getItem() == ModItems.FIREBREATHFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.BURNING.get()
                    || enchantment == ModEnchantments.RANGE.get();
        }
        if (stack.getItem() == ModItems.LAVABALLFOCUS.get()
            || stack.getItem() == ModItems.ROARINGFOCUS.get()
            || stack.getItem() == ModItems.SOULSKULLFOCUS.get()){
            return enchantment == ModEnchantments.POTENCY.get()
                    || enchantment == ModEnchantments.RADIUS.get()
                    || enchantment == ModEnchantments.BURNING.get();
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putString(FOCUS, stack.getItem().getDescriptionId());
        compound.putInt(SOULCOST, soulcost);
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (soulcost != 0) {
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.focuscost", soulcost));
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.focuscost", 0));
        }
    }

}
