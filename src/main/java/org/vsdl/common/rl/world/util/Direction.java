package org.vsdl.common.rl.world.util;

public enum Direction {
    NORTH(0, -1),
    NEAST(1, -1),
    EAST(1, 0),
    SEAST(1, 1),
    SOUTH(0, 1),
    SWEST(-1, 1),
    WEST(-1, 0),
    NWEST(-1, -1);

    private final int colChange;
    private final int rowChange;

    Direction(int colChange, int rowChange) {
        this.colChange = colChange;
        this.rowChange = rowChange;
    }

    public int getColChange() {
        return colChange;
    }

    public int getRowChange() {
        return rowChange;
    }

    public Direction rotateClockwise() {
        if (this.ordinal() == Direction.values().length - 1) return Direction.values()[0];
        return Direction.values()[this.ordinal() + 1];
    }

    public Direction rotateCounterClockwise() {
        if (this.ordinal() == 0) return Direction.values()[Direction.values().length - 1];
        return Direction.values()[this.ordinal() - 1];
    }
}
