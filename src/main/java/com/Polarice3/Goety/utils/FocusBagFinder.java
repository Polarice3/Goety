package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.compat.CuriosLoaded;
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
}
