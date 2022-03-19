package com.Polarice3.Goety.common.spells;

public class CastSpells {
    private final int spellint;

    public CastSpells(int spellint){
        this.spellint = spellint;
    }

    public Spells getSpell(){
        switch (spellint){
            case 0:
                return new VexSpell();
            case 1:
                return new FangSpell();
            case 2:
                return new RoarSpell();
            case 3:
                return new ZombieSpell();
            case 4:
                return new SkeletonSpell();
            case 5:
                return new WitchGaleSpell();
            case 6:
                return new SpiderlingSpell();
            case 7:
                return new BrainEaterSpell();
            case 8:
                return new TeleportSpell();
            case 9:
                return new SoulSkullSpell();
            case 10:
                return new FeastSpell();
            case 11:
                return new TemptingSpell();
            case 12:
                return new DragonFireballSpell();
            case 13:
                return new CreeperlingSpell();
            case 14:
                return new BreathSpell();
            default:
                return null;
        }
    }
}
