package com.Polarice3.Goety.common.items.focus;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.spells.Spells;
import com.Polarice3.Goety.common.spells.VexSpell;

public class VexingFocus extends MagicFocus {

    public VexingFocus() {
        super(MainConfig.VexCost.get());
    }

    @Override
    public Spells getSpell() {
        return new VexSpell();
    }

    public boolean isFast(){
        return this.getUpgrades().contains("speed");
    }

}
