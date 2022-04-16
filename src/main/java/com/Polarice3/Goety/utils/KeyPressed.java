package com.Polarice3.Goety.utils;

public class KeyPressed {
    public static boolean wandandbag;
    public static boolean wand;
    public static boolean bag;

    public static void setWand(boolean wand) {
        KeyPressed.wand = wand;
    }

    public static void setBag(boolean bag){
        KeyPressed.bag = bag;
    }

    public static void setWandandbag(boolean wandandbag) {
        KeyPressed.wandandbag = wandandbag;
    }

    public static boolean openWandandBag(){
        return wandandbag;
    }

    public static boolean openWand(){
        return wand;
    }

    public static boolean openBag(){
        return bag;
    }

}
