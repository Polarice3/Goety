package com.Polarice3.Goety.api.magic;

public interface IChargingSpell extends ISpell {
    int Cooldown();

    default int defaultCastDuration() {
        return 72000;
    }

    default int defaultSpellCooldown() {
        return 0;
    }
}
