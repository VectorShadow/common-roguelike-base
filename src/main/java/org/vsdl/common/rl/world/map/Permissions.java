package org.vsdl.common.rl.world.map;

public class Permissions {

    private static final byte SIGHT_MASK = 0b0000_0001;
    private static final byte FEATURE_MASK = 0b0000_0010;
    private static final byte OCCUPY_MASK = 0b0000_0100;
    private static final byte EFFECT_MASK = 0b0000_1000;


    public static boolean allowVision(byte b) {
        return (b & SIGHT_MASK) == SIGHT_MASK;
    }

    public static boolean allowFeatures(byte b) {
        return (b & FEATURE_MASK) == FEATURE_MASK;
    }

    public static boolean allowOccupation(byte b) {
        return (b & OCCUPY_MASK) == OCCUPY_MASK;
    }

    public static boolean allowEffects(byte b) {
        return (b & EFFECT_MASK) == EFFECT_MASK;
    }
}
