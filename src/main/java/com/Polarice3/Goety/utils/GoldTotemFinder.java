package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.compat.CuriosLoaded;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class GoldTotemFinder {
    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == ModRegistry.GOLDTOTEM.get();
    }

    public static ItemStack FindTotem(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (CuriosLoaded.CURIOS.isLoaded()) {
            foundStack = CuriosApi.getCuriosHelper().findEquippedCurio(GoldTotemFinder::isMatchingItem, playerEntity).map(
                    ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
        }

        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        return foundStack;
    }
}
