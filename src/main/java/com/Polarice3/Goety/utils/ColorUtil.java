package com.Polarice3.Goety.utils;

public class ColorUtil {
    public float red;
    public float green;
    public float blue;
    public float alpha;

    public ColorUtil(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public ColorUtil(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1.0F;
    }

    public ColorUtil(int red, int green, int blue, float alpha) {
        this.red = red / 255.0F;
        this.green = green / 255.0F;
        this.blue = blue / 255.0F;
        this.alpha = alpha;
    }

    public float red() {
        return this.red;
    }

    public float green() {
        return this.green;
    }

    public float blue() {
        return this.blue;
    }

    public float alpha() {
        return this.alpha;
    }

    public static double rgbToSpeed(double colorCode){
        return colorCode/255.0D;
    }

    /* Test this */
    public static double[] rgbParticle(int colorCode){
        return new double[]{((colorCode >> 16) & 0xff) / 255F, ((colorCode >> 8) & 0xff) / 255f, (colorCode & 0xff) / 255f};
    }
}
