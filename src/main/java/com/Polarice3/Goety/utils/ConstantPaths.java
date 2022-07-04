package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.Goety;
import net.minecraft.util.ResourceLocation;

public class ConstantPaths {

    public static ResourceLocation getSalvagedFort(){
        return Goety.location("salvaged_fort");
    }

    public static ResourceLocation getDecrepitFort(){
        return Goety.location("decrepit_fort");
    }

    public static ResourceLocation getDarkManor(){
        return Goety.location("dark_manor");
    }

    public static String readNetherBook(){
        return "goety:readNetherBook";
    }

    public static String readScroll(){
        return "goety:readScroll";
    }

    public static String secretCultist(){
        return "goety:cultist";
    }

    public static String revealedCultist(){
        return "goety:revealed";
    }

    public static String structureMob(){
        return "goety:structure";
    }

}
