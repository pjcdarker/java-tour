package com.pjcdarker.pattern.mediator;

/**
 * @author pjc
 * @create 2016-10-02
 */
public class GameSDK extends BaseSDK {

    public GameSDK(String name) {
        super(name);
    }

    @Override
    public void functionIntroduce() {
        System.out.println("GameSDK...");
    }
}
