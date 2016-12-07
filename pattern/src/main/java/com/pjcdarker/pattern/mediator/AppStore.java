package com.pjcdarker.pattern.mediator;

/**
 * @author pjc
 * @create 2016-10-02
 */
public interface AppStore {

    void addSDK(BaseSDK sdk);

    int sdkStar(BaseSDK sdk);
}
