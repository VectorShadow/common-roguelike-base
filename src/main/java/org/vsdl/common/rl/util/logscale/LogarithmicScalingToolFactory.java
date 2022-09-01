package org.vsdl.common.rl.util.logscale;

class LogarithmicScalingToolFactory {
    protected static LogarithmicScalingTool manufactureTool(LogScale.ScalingStrategy strategy, Long initialValue) {
        switch (strategy) {
            case FIXED_POINT: return new FixedPointLogarithmicScalingTool(initialValue);
            case GROWTH_RATE: return new GrowthRateLogarithmicScalingTool(initialValue);
            default: throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }
}
