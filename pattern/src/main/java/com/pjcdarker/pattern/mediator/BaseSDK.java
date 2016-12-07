package com.pjcdarker.pattern.mediator;

/**
 * @author pjc
 * @create 2016-10-02
 */
public abstract class BaseSDK implements SDK {

    protected String name;
    protected BaseStore baseStore;

    protected BaseSDK(String name) {
        this.name = name;
    }

    @Override
    public void jionStore(BaseStore baseStore) {
        this.baseStore = baseStore;
    }

    @Override
    public void showStar() {
        System.out.println(baseStore.getName() + ": " + this.baseStore.sdkStar(this));
    }

    public abstract void functionIntroduce();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
