package com.pjcdarker.pattern.mediator;

import org.junit.jupiter.api.Test;

/**
 * @author pjc
 * @create 2016-10-02
 */
public class MediatorTest {

    @Test
    public void testIOS() {
        IOSStore iosStore = new IOSStore();
        AdSDK adSDK = new AdSDK("adSDk");
        iosStore.addSDK(adSDK);
        adSDK.showStar();
    }
}
