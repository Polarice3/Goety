package com.Polarice3.Goety.common.items.curios;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.compat.curios.CuriosLoaded;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PendantOfHungerItem extends AmuletItem {
    private static final String ROTTEN_FLESH = "Rotten Flesh Count";

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            boolean allow;
            if (CuriosLoaded.CURIOS.isLoaded()){
                allow = CuriosFinder.findAmulet(player).getItem() instanceof PendantOfHungerItem;
            } else {
                allow = true;
            }
            if (allow) {
                if (!stack.hasTag()) {
                    stack.setTag(new CompoundNBT());
                    stack.getOrCreateTag().putInt(ROTTEN_FLESH, 0);
                } else {
                    if (getRottenFleshAmount(stack) < MainConfig.PendantOfHungerLimit.get()) {
                        if (!ItemHelper.findItem(player, Items.ROTTEN_FLESH).isEmpty()) {
                            increaseRottenFlesh(stack);
                            ItemHelper.findItem(player, Items.ROTTEN_FLESH).shrink(1);
                        }
                    }
                    if (getRottenFleshAmount(stack) > 0) {
                        if (MobUtil.playerValidity(player, true)) {
                            player.addEffect(new EffectInstance(Effects.HUNGER, 600, 0, false, false));
                            if (player.getFoodData().needsFood()) {
                                player.eat(player.level, new ItemStack(Items.ROTTEN_FLESH));
                                decreaseRottenFlesh(stack);
                            }
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, World pLevel, PlayerEntity pPlayer) {
        CompoundNBT compound = pStack.getOrCreateTag();
        compound.putInt(ROTTEN_FLESH, 0);
    }

    public void increaseRottenFlesh(ItemStack stack){
        if (stack.getTag() != null) {
            stack.getOrCreateTag().putInt(ROTTEN_FLESH, getRottenFleshAmount(stack) + 1);
        }
    }

    public void decreaseRottenFlesh(ItemStack stack){
        if (stack.getTag() != null) {
            stack.getOrCreateTag().putInt(ROTTEN_FLESH, getRottenFleshAmount(stack) - 1);
        }
    }

    public int getRottenFleshAmount(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getOrCreateTag().getInt(ROTTEN_FLESH);
        } else {
            return 0;
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int rottenFlesh = stack.getTag().getInt(ROTTEN_FLESH);
            tooltip.add(new TranslationTextComponent("info.goety.hunger_pendent.amount", rottenFlesh));
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.hunger_pendent.amount", 0));
        }
    }

}
