package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.compat.curios.CuriosLoaded;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class GoldTotemFinder {

    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.GOLDTOTEM.get();
    }

    public static ArcaTileEntity FindArca(PlayerEntity playerEntity){
        ISoulEnergy soulEnergy = SEHelper.getCapability(playerEntity);
        TileEntity tileentity = playerEntity.level.getBlockEntity(soulEnergy.getArcaBlock());
        if (tileentity instanceof ArcaTileEntity){
            ArcaTileEntity arcaTileEntity = (ArcaTileEntity) tileentity;
            if (arcaTileEntity.getPlayer() == playerEntity){
                return arcaTileEntity;
            }
        }
        return null;
    }

    public static ItemStack FindTotem(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (!SEHelper.getSEActive(playerEntity)) {
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
        }
        return foundStack;
    }
}
