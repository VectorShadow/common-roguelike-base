package org.vsdl.common.rl.util.logscale;

import java.util.ArrayList;
import java.util.List;

abstract class LogarithmicScalingTool {
    protected final long initialValue;

    protected List<Integer> keyList;
    protected List<List<Double>> valueLists;

    protected void validate() {
        if (keyList.get(0) <= 1) {
            throw new IllegalStateException("Key=1 should not be explicitly provided - please use initialValue to set the value for this key.");
        }
        int lastKey = 1;
        for (int key : keyList) {
            int thisKey = key;
            if (thisKey <= lastKey) {
                throw new IllegalStateException("Keys must be entered in ascending order.");
            }
            lastKey = thisKey;
        }
    }

    protected LogarithmicScalingTool(long initialValue) {
        this.initialValue = initialValue;
        keyList = new ArrayList<>();
        valueLists = new ArrayList<>();
    }

    protected abstract void addEntry(Integer key, Double... values);

    protected abstract double getRateOfGrowth(int index);
}