package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.items.handler.FocusBagItemHandler;
import com.Polarice3.Goety.common.items.magic.MagicFocusItem;
import com.Polarice3.Goety.compat.curios.CuriosLoaded;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class FocusBagFinder {
    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.FOCUSBAG.get();
    }
    public static ItemStack findBag(PlayerEntity playerEntity) {
        ItemStack foundStack = ItemStack.EMPTY;
        if (CuriosLoaded.CURIOS.isLoaded()) {
            foundStack = CuriosApi.getCuriosHelper().findEquippedCurio(FocusBagFinder::isMatchingItem, playerEntity).map(
                    ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
        }
        for (int i = 0; i <= playerEntity.inventory.getContainerSize(); i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        return foundStack;
    }

    public static ItemStack findFocusInBag(PlayerEntity player){
        ItemStack foundStack = ItemStack.EMPTY;
        if (!findBag(player).isEmpty()){
            FocusBagItemHandler focusBagItemHandler = FocusBagItemHandler.get(findBag(player));
            for (int i = 1; i < focusBagItemHandler.getSlots(); ++i){
                ItemStack itemStack = focusBagItemHandler.getStackInSlot(i);
                if (itemStack.getItem() instanceof MagicFocusItem){
                    foundStack = itemStack;
                }
            }
        }
        return foundStack;
    }

    public static int getFocusBagTotal(PlayerEntity player){
        int num = 0;
        if (!findBag(player).isEmpty()){
            FocusBagItemHandler focusBagItemHandler = FocusBagItemHandler.get(findBag(player));
            for (int i = 1; i < focusBagItemHandler.getSlots(); ++i){
                ItemStack itemStack = focusBagItemHandler.getStackInSlot(i);
                if (itemStack.getItem() instanceof MagicFocusItem){
                    ++num;
                }
            }
        }
        return num;
    }

    public static boolean hasEmptyBagSpace(PlayerEntity player){
        return getFocusBagTotal(player) < 10;
    }

    public static boolean hasFocusInBag(PlayerEntity player){
        return !findFocusInBag(player).isEmpty();
    }

    public static boolean canOpenWandCircle(PlayerEntity player){
        return hasFocusInBag(player) || WandUtil.hasFocusInInv(player) || !WandUtil.findFocus(player).isEmpty();
    }
}
