package com.pjcdarker.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author pjc
 * @create 2016-10-08
 */
public class GreatestCommonDivisorTest {

    private static GreatestCommonDivisor GREATEST_COMMON_DIVISOR;

    @BeforeAll
    public static void init() {
        GREATEST_COMMON_DIVISOR = new GreatestCommonDivisor();
    }

    @Test
    public void testViolentEnum() {
        int numA = 12, numB = 18, negativeC = -12, negativeD = -18;
        int gcd = GREATEST_COMMON_DIVISOR.violentEnum(numA,numB);
        System.out.println("gcd is : " + gcd);
        int negativeGCD = GREATEST_COMMON_DIVISOR.violentEnum(negativeC,negativeD);
        System.out.println("negativeGCD is : " + negativeGCD);
    }

    @Test
    public void testDivision() {
        int numA = 12, numB = 18, negativeC = -12, negativeD = -18;
        int gcd = GREATEST_COMMON_DIVISOR.division(numA, numB);
        System.out.println("gcd is : " + gcd);
        int negativeGCD = GREATEST_COMMON_DIVISOR.division(negativeC, negativeD);
        System.out.println("negativeGCD is : " + negativeGCD);
    }

    @Test
    public void testSubtractedLoss() {
        int numA = 12, numB = 18, negativeC = -12, negativeD = -18;
        int gcd = GREATEST_COMMON_DIVISOR.subtractedLoss(numA, numB);
        System.out.println("gcd is : " + gcd);
        int negativeGCD = GREATEST_COMMON_DIVISOR.subtractedLoss(negativeC, negativeD);
        System.out.println("negativeGCD is : " + negativeGCD);
    }


    @Test
    public void testDivisionAndSubtractedLoss() {
        int numA = 18, numB = 12, negativeC = -12, negativeD = -18;
        int gcd = GREATEST_COMMON_DIVISOR.divisionAndSubtractedLoss(numA, numB);
        System.out.println("gcd is : " + gcd);
        int negativeGCD = GREATEST_COMMON_DIVISOR.divisionAndSubtractedLoss(negativeC, negativeD);
        System.out.println("negativeGCD is : " + negativeGCD);
    }

    @Test
    public void test() {
        System.out.println(1/-8);
        System.out.println(1%-8);
    }

}
