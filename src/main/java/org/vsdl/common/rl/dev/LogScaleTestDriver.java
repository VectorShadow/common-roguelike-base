package org.vsdl.common.rl.dev;

import org.vsdl.common.rl.util.logscale.LogScale;

import java.util.List;

public class LogScaleTestDriver {
    public static void main(String[] args) {
        List<Long> xpByLevel = LogScale
                .scale()
//                .using(LogScale.ScalingStrategy.FIXED_POINT, 10L)
//                .addEntry(15, 1_000.0)
//                .addEntry(99, 1_000_000.0)
//                .addEntry(127, 100_000_000.0)
//                .addEntry(255, 1_000_000_000_000.0)

                .using(LogScale.ScalingStrategy.GROWTH_RATE, 16L)
                .addEntry(15, 2.5, 3.5)
                .addEntry(99, 2.0, 7.0)
                .addEntry(127, 2.1, 7.0)
                .addEntry(255, 2.2, 7.0)

                .generateScaledList();
        for (int i = 0; i < xpByLevel.size(); ++i) {
            System.out.println("Level " + i + ": " + xpByLevel.get(i) + " xp");
        }
    }
}
