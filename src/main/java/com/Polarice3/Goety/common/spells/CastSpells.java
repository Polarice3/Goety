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
            case 15:
                return new FireballSpell();
            case 16:
                return new LavaballSpell();
            case 17:
                return new PoisonBallSpell();
            case 18:
                return new IllusionSpell();
            case 19:
                return new SoulShieldSpell();
            case 20:
                return new FireBreathSpell();
            case 21:
                return new SoulLightSpell();
            case 22:
                return new GlowLightSpell();
            case 23:
                return new IceStormSpell();
            case 24:
                return new FrostBreathSpell();
            case 25:
                return new UndeadWolfSpell();
            case 26:
                return new LaunchSpell();
            case 27:
                return new SonicBoomSpell();
            default:
                return null;
        }
    }
}
