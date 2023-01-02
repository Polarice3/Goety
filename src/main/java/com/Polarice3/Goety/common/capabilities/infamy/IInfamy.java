package com.Polarice3.Goety.common.capabilities.infamy;

/**
 * Learned Player Capabilities from codes by @stal111
 */
public interface IInfamy {
    int getInfamy();
    void setInfamy(int infamy);
    boolean increaseInfamy(int increase);
    boolean decreaseInfamy(int decrease);
}
