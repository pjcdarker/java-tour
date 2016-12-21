package com.pjcdarker.pattern.strategy;

/**
 * @author pjc
 * @Created 12/20/2016.
 */
public class ADCalculator {

    public double calculator(BillMethod method, double balance) {
        ADCalculationStrategy adCalculationStrategy = CalculationFactory.calculate(method);
        return adCalculationStrategy.calculate(balance);
    }
}
