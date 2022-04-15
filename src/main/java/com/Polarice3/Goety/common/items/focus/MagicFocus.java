package com.Polarice3.Goety.common.items.focus;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.spells.Spells;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class MagicFocus extends Item {
    public static final String SOULCOST = "Soul Cost";
    public int soulcost;
    public CompoundNBT tag;

    public MagicFocus(int soulcost){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
        this.soulcost = soulcost;
    }

    public abstract Spells getSpell();

    public void setTag(@Nullable CompoundNBT p_77982_1_) {
        this.tag = p_77982_1_;
    }

    public CompoundNBT getOrCreateTag() {
        if (this.tag == null) {
            this.setTag(new CompoundNBT());
        }

        return this.tag;
    }

    public boolean hasTag() {
        return this.tag != null && !this.tag.isEmpty();
    }

    public ListNBT getUpgrades(){
        if (this.tag != null && this.tag.contains("Upgrades", 9)) {
            return this.tag.getList("Upgrades", 10);
        } else {
            return null;
        }
    }

    public boolean addUpgrades(INBT inbt){
        ListNBT listNBT = this.tag.getList("Upgrades", 10);
        return listNBT.add(inbt);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CompoundNBT compound = stack.getOrCreateTag();
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
