package com.Polarice3.Goety.common.potions;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class IllagueEffect extends ModEffects{

    public IllagueEffect() {
        super(EffectType.HARMFUL, 9804699);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
