package com.Polarice3.Goety.common.effects;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class IllagueEffect extends ModEffect {

    public IllagueEffect() {
        super(EffectType.HARMFUL, 9804699);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
