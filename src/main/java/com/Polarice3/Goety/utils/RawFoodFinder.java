package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.item.Item;

import java.util.Objects;

public class RawFoodFinder {
    public static boolean findRawFood(Item item){
        return item.isEdible() && Objects.requireNonNull(item.getFoodProperties()).isMeat() && item != ModRegistryHandler.DEADBAT.get();
    }

    public static boolean findMutatedRaw(Item item){
        return item == ModRegistryHandler.MUTATED_CHICKEN_UNCOOKED.get() || item == ModRegistryHandler.MUTATED_MUTTON_UNCOOKED.get() ||
                item == ModRegistryHandler.MUTATED_PORKCHOP_UNCOOKED.get() || item == ModRegistryHandler.MUTATED_RABBIT_UNCOOKED.get() ||
                item == ModRegistryHandler.MUTATED_STEAK_UNCOOKED.get();
    }
}
