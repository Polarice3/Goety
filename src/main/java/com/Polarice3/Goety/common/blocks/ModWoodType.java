package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.WoodType;

public class ModWoodType extends WoodType {
    public static final WoodType HAUNTED = register(new ModWoodType("haunted"));

    protected ModWoodType(String pName) {
        super(pName);
    }
}
