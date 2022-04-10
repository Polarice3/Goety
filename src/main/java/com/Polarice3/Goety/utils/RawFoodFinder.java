package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModItems;
import net.minecraft.item.Item;

import java.util.Objects;

public class RawFoodFinder {
    public static boolean findRawFood(Item item){
        return item.isEdible() && Objects.requireNonNull(item.getFoodProperties()).isMeat() && item != ModItems.DEADBAT.get();
    }

    public static boolean findMutatedRaw(Item item){
        return item == ModItems.MUTATED_CHICKEN_UNCOOKED.get() || item == ModItems.MUTATED_MUTTON_UNCOOKED.get() ||
                item == ModItems.MUTATED_PORKCHOP_UNCOOKED.get() || item == ModItems.MUTATED_RABBIT_UNCOOKED.get() ||
                item == ModItems.MUTATED_STEAK_UNCOOKED.get();
    }
}
