package com.Polarice3.Goety.common.magic;

import com.Polarice3.Goety.api.magic.IBreathingSpell;

public abstract class SpewingSpell extends ChargingSpells implements IBreathingSpell {
    @Override
    public int Cooldown() {
        return 0;
    }
}
