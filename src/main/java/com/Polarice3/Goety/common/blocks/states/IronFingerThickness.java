package com.Polarice3.Goety.common.blocks.states;

import net.minecraft.util.IStringSerializable;

public enum IronFingerThickness implements IStringSerializable {
    TIP("tip"),
    FRUSTUM("frustum"),
    MIDDLE("middle"),
    BASE("base");

    private final String name;

    private IronFingerThickness(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
