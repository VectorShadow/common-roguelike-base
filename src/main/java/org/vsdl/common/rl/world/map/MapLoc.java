package org.vsdl.common.rl.world.map;

import org.vsdl.common.rl.world.util.Direction;

import java.io.Serializable;

public class MapLoc implements Serializable {
    private final short R;
    private final short C;

    public static MapLoc at(int row, int col) {
        return new MapLoc(row, col);
    }

    public MapLoc(short row, short col) {
        R = row;
        C = col;
    }

    public MapLoc(int row, int col) {
        if (row > Short.MAX_VALUE || row < Short.MIN_VALUE || col > Short.MAX_VALUE || col < Short.MIN_VALUE) {
            throw new IllegalArgumentException();
        }
        R = (short)row;
        C = (short)col;
    }

    public MapLoc(MapLoc origin, Direction shift) {
        this(origin.getRow() + shift.getRowChange(), origin.getCol() + shift.getColChange());
    }

    public short getCol() {
        return C;
    }

    public short getRow() {
        return R;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MapLoc && ((MapLoc) o).C == C && ((MapLoc) o).R == R;
    }

    @Override
    public int hashCode() {
        return (1 + Short.MAX_VALUE) * R + C;
    }

    @Override
    public String toString() {
        return "(" + R + "," + C +")";
    }
}
