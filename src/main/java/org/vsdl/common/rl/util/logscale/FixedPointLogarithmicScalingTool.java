package org.vsdl.common.rl.util.logscale;

import java.util.ArrayList;
import java.util.List;

class FixedPointLogarithmicScalingTool extends LogarithmicScalingTool {

    private static final int VALUE_INDEX = 0;

    protected FixedPointLogarithmicScalingTool(long initialValue) {
        super(initialValue);
        List<Double> valueFixedPoints = new ArrayList<>();
        super.valueLists.add(valueFixedPoints);
    }

    @Override
    protected void validate() {
        super.validate();
        double lastValue = -1;
        for (double value : valueLists.get(VALUE_INDEX)) {
            double thisValue = value;
            if (value <= lastValue) {
                throw new IllegalStateException("Values must be entered in ascending order.");
            }
            lastValue = thisValue;
        }
    }

    @Override
    protected void addEntry(Integer key, Double... values) {
        if (values.length != 1) {
            throw new IllegalArgumentException("Expected 1 value argument, actual: " + values.length);
        }
        keyList.add(key);
        valueLists.get(VALUE_INDEX).add(values[VALUE_INDEX]);
    }

    @Override
    protected double getRateOfGrowth(int index) {
        List<Double> values = valueLists.get(VALUE_INDEX);
        return Math.log(values.get(index) / (index == 0 ? initialValue : values.get(index - 1))) / (keyList.get(index) - (index == 0 ? 1 : keyList.get(index - 1)));
    }
}
