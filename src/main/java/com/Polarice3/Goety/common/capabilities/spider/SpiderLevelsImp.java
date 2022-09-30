package com.Polarice3.Goety.common.capabilities.spider;

public class SpiderLevelsImp implements ISpiderLevels {

    private int spiderLevels = 0;

    @Override
    public int getSpiderLevel() {
        return this.spiderLevels;
    }

    @Override
    public void setSpiderLevel(int spiderLevel) {
        this.spiderLevels = spiderLevel;
    }

    @Override
    public boolean increaseSpiderLevel(int increase) {
        if (this.spiderLevels >= 10) {
            return false;
        }
        this.spiderLevels = Math.min(this.spiderLevels + increase, 10);
        return true;
    }

    @Override
    public boolean decreaseSpiderLevel(int decrease) {
        if (this.spiderLevels == 0) {
            return false;
        }
        this.spiderLevels = Math.max(this.spiderLevels - decrease, 0);
        return true;
    }
}
