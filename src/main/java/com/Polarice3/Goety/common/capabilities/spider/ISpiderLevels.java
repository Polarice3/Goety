package com.Polarice3.Goety.common.capabilities.spider;

public interface ISpiderLevels {
    int getSpiderLevel();
    void setSpiderLevel(int spiderLevel);
    boolean increaseSpiderLevel(int increase);
    boolean decreaseSpiderLevel(int decrease);
}
