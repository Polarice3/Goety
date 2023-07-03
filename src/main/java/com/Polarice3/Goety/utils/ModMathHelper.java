package com.Polarice3.Goety.utils;

public class ModMathHelper {
    public static int secondsToTicks(int pSeconds){
        return pSeconds * 20;
    }

    public static float secondsToTicks(float pSeconds){
        return pSeconds * 20;
    }

    public static int minutesToTicks(int pMinutes){
        return secondsToTicks(pMinutes * 60);
    }

    public static float minutesToTicks(float pMinutes){
        return secondsToTicks(pMinutes * 60);
    }

    public static float modelDegrees(float degree){
        return (float) ((degree * Math.PI)/180.0F);
    }
}
