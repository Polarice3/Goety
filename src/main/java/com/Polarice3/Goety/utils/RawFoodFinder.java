package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.item.Item;

import java.util.Objects;

public class RawFoodFinder {
    public static boolean findRawFood(Item item){
        return item.isEdible() && Objects.requireNonNull(item.getFoodProperties()).isMeat() && item != ModRegistry.DEADBAT.get();
    }

    public static boolean findMutatedRaw(Item item){
        return item == ModRegistry.MUTATED_CHICKEN_UNCOOKED.get() || item == ModRegistry.MUTATED_MUTTON_UNCOOKED.get() ||
                item == ModRegistry.MUTATED_PORKCHOP_UNCOOKED.get() || item == ModRegistry.MUTATED_RABBIT_UNCOOKED.get() ||
                item == ModRegistry.MUTATED_STEAK_UNCOOKED.get();
    }
}
