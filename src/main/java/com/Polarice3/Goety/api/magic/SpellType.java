package com.Polarice3.Goety.api.magic;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum SpellType implements net.minecraftforge.common.IExtensibleEnum {
    NONE("none"),
    NECROMANCY("necromancy"),
    FEL("fel"),
    NETHER("nether"),
    ILL("ill"),
    ENDER("ender"),
    FROST("frost");

    private final ITextComponent name;

    SpellType(String name){
        this.name = new TranslationTextComponent("spell.goety." + name);
    }

    public static SpellType create(String name, String spellType){
        throw new IllegalStateException("Enum not extended");
    }

    public ITextComponent getName(){
        return name;
    }
}
