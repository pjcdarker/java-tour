package com.pjcdarker.util;

/**
 * @author pjc
 * @create 2016-10-08
 */
public class GreatestCommonDivisor {

    public int violentEnum(int numA, int numB) {
        if (numA <= 1 || numB <= 1) {
            return 1;
        }
        int min = numA < numB ? numA : numB;
        int max = numA >= numB ? numA : numB;
        if (max % min == 0) {
            return min;
        }
        int gcd = 1;
        for (int i = 2; i <= max / 2; i++) {
            if (min % i == 0 && max % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }

    public int division(int numA, int numB) {
        if (numA <= 1 || numB <= 1) {
            return 1;
        }
        int num = numA % numB;
        if (num == 0) {
            return numB;
        }
        return division(numB, num);
    }

    public int subtractedLoss(int numA, int numB) {
        if (numA <= 1 || numB <= 1) {
            return 1;
        }
        int num = numA - numB;
        if (numB == num || num == 0) {
            return numB;
        }
        if (numA < numB) {
            return subtractedLoss(numB, numA);
        }
        return subtractedLoss(numB, num);
    }

    public int divisionAndSubtractedLoss(int numA, int numB) {
        if (numA < 0 || numB < 0) {
            return 1;
        }
        if (numA == 0 || (numA ^ numB) == 0) {
            return numB;
        }
        if (numB == 0) {
            return numA;
        }
        if (numA < numB) {
            return divisionAndSubtractedLoss(numB, numA);
        }
        if (numA % numB == 0) {
            return numB;
        }
        if ((numA & 1) != 1 && (numB & 1) != 1) {
            return divisionAndSubtractedLoss(numA >> 1, numB >> 1) << 1;
        }
        if ((numA & 1) != 1) {
            return divisionAndSubtractedLoss(numA >> 1, numB);
        }
        if ((numB & 1) != 1) {
            return divisionAndSubtractedLoss(numA, numB >> 1 );
        }
        return divisionAndSubtractedLoss(numB, numA - numB);
    }
}
