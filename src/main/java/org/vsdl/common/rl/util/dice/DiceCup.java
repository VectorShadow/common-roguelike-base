package org.vsdl.common.rl.util.dice;

import java.security.SecureRandom;

public class DiceCup {
    private static final SecureRandom SRNG = new SecureRandom();

    private long value = 0;

    private DiceCup() {}

    public static DiceCup get() {
        return new DiceCup();
    }

    public Double unitReal() {
        return SRNG.nextDouble();
    }

    private int d(int sides) {
        return SRNG.nextInt(sides) + 1;
    }

    public DiceCup roll(int numberOfDice, int sidesPerDie, boolean openEnded) {
        int totalRollValue = 0;
        for (int i = 0; i < numberOfDice; ++i) {
            int singleRollValue = 0;
            int openRollValue = 0;
            do {
                singleRollValue = d(sidesPerDie);
                openRollValue += singleRollValue;
            } while (openEnded && singleRollValue == sidesPerDie);
            totalRollValue += openRollValue;
        }
        value += totalRollValue;
        return this;
    }

    public DiceCup roll(int numberOfDice, int sidesPerDie) {
        return roll(numberOfDice, sidesPerDie, false);
    }

    public DiceCup roll(int sides, boolean openEnded) {
        return roll(1, sides, openEnded);
    }

    public DiceCup roll(int sides) {
        return roll(sides, false);
    }

    public long checkRollValue() {
        return value;
    }

    public long resetRollValue() {
        long v = value;
        value = 0;
        return v;
    }
}
