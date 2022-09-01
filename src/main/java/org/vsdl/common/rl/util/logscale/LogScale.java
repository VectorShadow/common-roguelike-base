package org.vsdl.common.rl.util.logscale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogScale {
    public enum ScalingStrategy {
        FIXED_POINT,
        GROWTH_RATE;
    }

    private LogarithmicScalingTool tool;

    private LogScale() {}

    public static LogScale scale() {
        return new LogScale();
    }

    public LogScale using(ScalingStrategy strategy, Long initialValue) {
        tool = LogarithmicScalingToolFactory.manufactureTool(strategy, initialValue);
        return this;
    }

    public LogScale addEntry(Integer key, Double... values) {
        tool.addEntry(key, values);
        return this;
    }

    public List<Long> generateScaledList() {
        tool.validate();
        int valueListSize = tool.valueLists.get(0).size();
        if (tool.keyList.size() != tool.valueLists.get(0).size()) {
            throw new IllegalArgumentException("Provided lists must be of the same length(" + valueListSize +
                    " was not " + valueListSize);
        }
        if (tool.keyList.isEmpty()) {
            throw new IllegalArgumentException("Provided lists must not be empty.");
        }
        int size = tool.keyList.size();
//        Collections.sort(tool.keyList);
//        for (List<Double> valueList : tool.valueLists) {
//            Collections.sort(valueList);
//        }
        List<Long> scaledList = new ArrayList<>();
        scaledList.add(0L);
        scaledList.add(tool.initialValue);
        int lastFixedKey = 1;
        long lastFixedValue = tool.initialValue;
        for (int i = 0; i < size; ++i) {
            int nextFixedKey = tool.keyList.get(i);
            double r = tool.getRateOfGrowth(i);
            for (int j = lastFixedKey + 1; j < nextFixedKey + 1; ++j) {
                scaledList.add((long)(lastFixedValue * Math.pow(Math.E, r * (j - lastFixedKey))));
            }
            lastFixedKey = nextFixedKey;
            lastFixedValue = scaledList.get(scaledList.size() - 1);
        }
        return scaledList;
    }
}