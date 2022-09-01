package org.vsdl.common.rl.util.logscale;

import java.util.ArrayList;
import java.util.List;

class GrowthRateLogarithmicScalingTool extends LogarithmicScalingTool {

    private static final int GROWTH_FACTOR_INDEX = 0;
    private static final int GROWTH_INTERVAL_INDEX = 1;

    protected GrowthRateLogarithmicScalingTool(long initialValue) {
        super(initialValue);
        List<Double> growthRates = new ArrayList<>();
        List<Double> intervals = new ArrayList<>();
        super.valueLists.add(growthRates);
        super.valueLists.add(intervals);
    }

    @Override
    protected void addEntry(Integer key, Double... values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("Expected 2 value argument, actual: " + values.length);
        }
        keyList.add(key);
        valueLists.get(GROWTH_FACTOR_INDEX).add(values[GROWTH_FACTOR_INDEX]);
        valueLists.get(GROWTH_INTERVAL_INDEX).add(values[GROWTH_INTERVAL_INDEX]);
    }

    @Override
    protected double getRateOfGrowth(int index) {
        return Math.log(valueLists.get(GROWTH_FACTOR_INDEX).get(index) ) / valueLists.get(GROWTH_INTERVAL_INDEX).get(index);
    }
}
