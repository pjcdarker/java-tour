package com.pjcdarker.pattern.strategy.imp;

import com.pjcdarker.pattern.strategy.ADCalculationStrategy;

/**
 * @author pjc
 * @Created 12/20/2016.
 */
public class CPI implements ADCalculationStrategy {

    @Override
    public double calculate(double balance) {
        return balance * 100;
    }
}
