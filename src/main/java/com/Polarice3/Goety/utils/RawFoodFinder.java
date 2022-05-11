package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.item.Item;

import java.util.Objects;

public class RawFoodFinder {
    public static boolean findRawFood(Item item){
        return item.isEdible() && Objects.requireNonNull(item.getFoodProperties()).isMeat() && item != ModItems.DEADBAT.get();
    }

    public static boolean findMutatedRaw(Item item){
        return item.is(ModTags.Items.RAW_MUTATED_MEAT);
    }
}
