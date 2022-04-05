package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.LivingEntity;

public abstract class ChargingSpells extends Spells{

    public abstract int Cooldown();

    public int CastDuration() {
        return 72000;
    }

    public boolean ArachnoPower(LivingEntity entityLiving){
        return RobeArmorFinder.FindArachnoArmor(entityLiving);
    }


}
