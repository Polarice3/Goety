package com.Polarice3.Goety.common.infamy;

import com.Polarice3.Goety.MainConfig;

public class InfamyImp implements IInfamy{

    private int infamy = 0;

    @Override
    public int getInfamy() {
        return this.infamy;
    }

    @Override
    public void setInfamy(int infamy) {
        this.infamy = infamy;
    }

    @Override
    public boolean increaseInfamy(int increase) {
        if (this.infamy >= MainConfig.InfamyMax.get()) {
            return false;
        }
        this.infamy = Math.min(this.infamy + increase, MainConfig.InfamyMax.get());
        return true;
    }

    @Override
    public boolean decreaseInfamy(int decrease) {
        if (this.infamy <= 0) {
            return false;
        }
        this.infamy = Math.max(this.infamy - decrease, 0);
        return true;
    }

}
