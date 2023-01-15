package com.Polarice3.Goety.utils;

public class TickUtil {
    public static int ticksToSeconds(int pSeconds){
        return pSeconds * 20;
    }

    public static float ticksToSeconds(float pSeconds){
        return pSeconds * 20;
    }

    public static int ticksToMinutes(int pMinutes){
        return ticksToSeconds(pMinutes * 60);
    }

    public static float ticksToMinutes(float pMinutes){
        return ticksToSeconds(pMinutes * 60);
    }
}
