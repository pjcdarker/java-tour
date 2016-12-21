package com.pjcdarker.pattern.strategy;

import com.pjcdarker.pattern.strategy.imp.CPA;
import com.pjcdarker.pattern.strategy.imp.CPC;
import com.pjcdarker.pattern.strategy.imp.CPI;
import com.pjcdarker.pattern.strategy.imp.CPM;

/**
 * @author pjc
 * @Created 12/20/2016.
 */
public class CalculationFactory {

    public static ADCalculationStrategy calculate(BillMethod billMethod) {
        switch (billMethod) {
            case CPA:
                return new CPA();
            case CPC:
                return new CPC();
            case CPI:
                return new CPI();
            case CPM:
                return new CPM();
            default:
                throw new RuntimeException("not support this " + billMethod + " BillMethod ");
        }
    }

}
