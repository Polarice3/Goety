package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.api.items.magic.IFocus;
import com.Polarice3.Goety.common.magic.Spells;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MagicFocusItem extends Item implements IFocus {
    public static final String SOULCOST = "Soul Cost";
    public Spells spell;
    public int soulcost;

    public MagicFocusItem(Spells spell){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
        this.spell = spell;
        this.soulcost = spell.defaultSoulCost();
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof IFocus){
            IFocus magicFocus = (IFocus) stack.getItem();
            if (magicFocus.getSpell() != null){
                if (!magicFocus.getSpell().acceptedEnchantments().isEmpty()){
                    return magicFocus.getSpell().acceptedEnchantments().contains(enchantment);
                }
            }
        }
        return false;
    }

    public Spells getSpell(){
        return this.spell;
    }

    @Override
    public void fillItemCategory(ItemGroup pGroup, NonNullList<ItemStack> pItems) {
        if (this.allowdedIn(pGroup)){
            ItemStack stack = new ItemStack(this);
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULCOST, soulcost);
            pItems.add(stack);
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, World pLevel, PlayerEntity pPlayer) {
        CompoundNBT compound = pStack.getOrCreateTag();
        compound.putInt(SOULCOST, soulcost);
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (this.soulcost != 0) {
            tooltip.add(new TranslationTextComponent("info.goety.focus.cost", this.soulcost));
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.focus.cost", 0));
        }
        tooltip.add(new TranslationTextComponent("info.goety.focus.spellType", spell.getSpellType().getName()));
    }

}
