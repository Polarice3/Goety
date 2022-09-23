package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.items.curios.AmuletItem;
import com.Polarice3.Goety.common.items.curios.RingItem;
import com.Polarice3.Goety.compat.curios.CuriosLoaded;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosFinder {
    private static boolean isAmulet(ItemStack itemStack) {
        return itemStack.getItem() instanceof AmuletItem;
    }

    public static ItemStack findAmulet(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (CuriosLoaded.CURIOS.isLoaded()) {
            foundStack = CuriosApi.getCuriosHelper().findEquippedCurio(CuriosFinder::isAmulet, playerEntity).map(
                    ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
        } else {
            for (int i = 0; i <= playerEntity.inventory.getContainerSize(); i++) {
                ItemStack itemStack = playerEntity.inventory.getItem(i);
                if (!itemStack.isEmpty() && isAmulet(itemStack)) {
                    foundStack = itemStack;
                    break;
                }
            }
        }

        return foundStack;
    }

    private static boolean isRing(ItemStack itemStack) {
        return itemStack.getItem() instanceof RingItem;
    }

    public static ItemStack findRing(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (CuriosLoaded.CURIOS.isLoaded()) {
            foundStack = CuriosApi.getCuriosHelper().findEquippedCurio(CuriosFinder::isRing, playerEntity).map(
                    ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
        } else {
            for (int i = 0; i <= playerEntity.inventory.getContainerSize(); i++) {
                ItemStack itemStack = playerEntity.inventory.getItem(i);
                if (!itemStack.isEmpty() && isRing(itemStack)) {
                    foundStack = itemStack;
                    break;
                }
            }
        }

        return foundStack;
    }
}
