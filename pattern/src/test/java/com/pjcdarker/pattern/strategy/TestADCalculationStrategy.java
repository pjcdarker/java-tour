package com.pjcdarker.pattern.strategy;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pjc
 * @Created 12/20/2016.
 */
public class TestADCalculationStrategy {

    private static ADCalculator adCalculator;

    @BeforeClass
    public static void before() {
        adCalculator = new ADCalculator();
    }

    @Test
    public void test1() {
        double cpa = adCalculator.calculator(BillMethod.CPA, 10);
        System.out.println(cpa);

        double cpc = adCalculator.calculator(BillMethod.CPC, 10);
        System.out.println(cpc);
    }
}
