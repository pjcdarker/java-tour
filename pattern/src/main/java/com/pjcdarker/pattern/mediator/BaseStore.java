package com.pjcdarker.pattern.mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pjc
 * @create 2016-10-02
 */
public abstract class BaseStore implements AppStore {

    protected List<BaseSDK> baseSDKs = new ArrayList<>();
    protected Map<String, Integer> starMap = new ConcurrentHashMap<>();

    @Override
    public void addSDK(BaseSDK sdk) {
        baseSDKs.add(sdk);
        sdk.jionStore(this);
        starMap.put(sdk.getName(), new Random().nextInt(100000));
    }

    @Override
    public int sdkStar(BaseSDK sdk) {
        return this.starMap.get(sdk.getName());
    }

    public abstract String getName();
}
