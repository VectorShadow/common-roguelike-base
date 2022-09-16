package org.vsdl.common.rl.world.map;

import org.vsdl.common.rl.world.asset.Feature;
import org.vsdl.common.rl.world.util.Angle;
import org.vsdl.common.rl.world.util.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapReader {
    private final Field map;


    public MapReader(Field f) {
        map = f;
    }

    /**
     * Check whether a location on this field allows line of sight to pass through.
     * @param mapLoc field coordinates to be checked.
     * @return true if both terrain and any present feature permit vision, otherwise false.
     */
    public boolean checkVision(MapLoc mapLoc) {
        map.validateLocation(mapLoc);
        byte t = map.getTerrainAt(mapLoc);
        Feature f = map.getFeatureAt(mapLoc);
        return Permissions.allowVision((byte)(f == null ? t : t & f.getState()));
    }

    /**
     * Check whether a location on this field allows actors and projectiles to pass through.
     * @param mapLoc field coordinates to be checked.
     * @return true if both terrain and any present feature permit occupation, otherwise false.
     */
    public boolean checkOccupation(MapLoc mapLoc) {
        map.validateLocation(mapLoc);
        byte t = map.getTerrainAt(mapLoc);
        Feature f = map.getFeatureAt(mapLoc);
        return Permissions.allowOccupation((byte)(f == null ? t : t & f.getState()));
    }

    public double checkDistance(MapLoc origin, MapLoc destination) {
        int colDif = destination.getCol() - origin.getCol();
        int rowDif = destination.getRow() - origin.getRow();
        return Math.sqrt(colDif * colDif + rowDif * rowDif);
    }

    /**
     * Generate a list of coordinates specifying a direct line from the origin to the destination.
     * Direct paths are validated by Vision checks rather than Movement.
     * @param origin the initial coordinate on the path
     * @param destination the final coordinate on the path
     * @param includeFirstFailure whether to include the first coordinate which fails the check or not.
     * @return a list of all coordinates from the origin to the destination, or up to the first point of failure.
     * This list may be empty if the origin is the first point of failure and this point is not included.
     */
    public List<MapLoc> directPath(MapLoc origin, MapLoc destination, boolean includeFirstFailure) {
        List<MapLoc> path = new ArrayList<>();
        int colDif = destination.getCol() - origin.getCol();
        int rowDif = destination.getRow() - origin.getRow();
        double pathLength = checkDistance(origin, destination);
        MapLoc step = origin;
        double dCol = colDif / pathLength;
        double dRow = rowDif / pathLength;
        double colProgress = 0;
        double rowProgress = 0;
        do {
            //current step must be valid to continue
            if (!checkVision(step)) {
                if (includeFirstFailure) {
                    path.add(step);
                }
                break;
            }
            colProgress += dCol;
            rowProgress += dRow;
            short colShift = (short)(colProgress >= 0.5 ? 1 : colProgress <= -0.5 ? -1 : 0);
            short rowShift = (short)(rowProgress >= 0.5 ? 1 : rowProgress <= -0.5 ? -1 : 0);
            //always progress to a new location
            if (colShift == 0 && rowShift == 0) {
                double absColProg = Math.abs(colProgress);
                double absRowProg = Math.abs(rowProgress);
                if (absColProg > absRowProg) {
                    colShift = 1;
                } else if (absRowProg > absColProg) {
                    rowShift = 1;
                } else {
                    colShift = 1;
                    rowShift = 1;
                }
            }
            colProgress -= colShift;
            rowProgress -= rowShift;
            path.add(step);
            step = new MapLoc((step.getRow() + rowShift), (step.getCol() + colShift));
        } while(path.isEmpty() || !path.get(path.size() - 1).equals(destination));
        return path;
    }

    /**
     * Generate a list of coordinates specifying a direct line from origin to destination only if a valid path exists.
     * @param origin the initial coordinate on the path
     * @param destination the final coordinate on the path
     * @return the path if it exists, null if not.
     */
    public List<MapLoc> validDirectPath(MapLoc origin, MapLoc destination) {
        List<MapLoc> path = directPath(origin, destination, false);
        return path.get(path.size() - 1).equals(destination) ? path : null;
    }

    /**
     * Generate a list of coordinates specifying a path from the origin to the destination.
     * This path need not be a straight line.
     * Indirect paths are validated by Movement checks rather than Vision.
     * This method may be expensive and should not be called synchronously in a realtime engine cycle.
     * @param origin the initial coordinate on the path
     * @param destination the final coordinate on the path
     * @param includeFirstFailure whether to include the first coordinate which fails the check or not.
     * @return a list of all coordinates from the origin to the destination, or up to the first point of failure.
     * This list may be empty if the origin is the first point of failure and this point is not included.
     */
    public List<MapLoc> indirectPath(MapLoc origin, MapLoc destination, boolean includeFirstFailure) {
        List<MapLoc> path = new ArrayList<>();
        //todo - checkOccupation on origin, find path to destination, check occupation at each step.
        return path;
    }

    /**
     * Generate a list of coordinates specifying a path from origin to destination only if a valid path exists.
     * This method may be expensive and should not be called synchronously in a realtime engine cycle.
     * @param origin the initial coordinate on the path
     * @param destination the final coordinate on the path
     * @return the path if it exists, null if not.
     */
    public List<MapLoc> validIndirectPath(MapLoc origin, MapLoc destination) {
        List<MapLoc> path = indirectPath(origin, destination, false);
        return path.get(path.size() - 1).equals(destination) ? path : null;
    }

    private List<MapLoc> propagate(MapLoc origin, MapLoc at, Direction direction, Angle angle, int limit) {
        List<MapLoc> locs = new ArrayList<>();
        if (!map.checkBounds(at)) return locs;
        locs.add(at);
        if (!checkVision(at) || checkDistance(origin, at) > limit) return locs;
        if (!angle.equals(Angle.LEFT)) locs.addAll(propagate(origin, new MapLoc(at, direction.rotateClockwise()), direction, Angle.RIGHT, limit));
        if (angle.equals(Angle.TRUE)) locs.addAll(propagate(origin, new MapLoc(at, direction), direction, Angle.TRUE, limit));
        if (!angle.equals(Angle.RIGHT)) locs.addAll(propagate(origin, new MapLoc(at, direction.rotateCounterClockwise()), direction, Angle.LEFT, limit));
        return locs;
    }

    /**
     * Generate a set of all coordinates to which there is a direct line of sight from the origin of length radius or less.
     * Visible coordinates are not necessarily reachable by a directPath.
     * @param origin the point from which all prospective lines of sight are drawn.
     * @param radius the maximum length to which any line of sight may be extended.
     * @return the set of all coordinates visible from the origin with the specified radius.
     */
    public Set<MapLoc> getVisibleLocations(MapLoc origin, int radius) {
        Set<MapLoc> locSet = new HashSet<>();
        locSet.add(origin);
        for (Direction d : Direction.values()) {
            MapLoc target = new MapLoc(origin, d);
            locSet.addAll(propagate(origin, target, d, Angle.TRUE, radius));
        }
        return locSet;
    }

    /**
     * Confirm that a pre-existing path remains valid.
     * @param path a list of all coordinates in the path
     * @param los whether this path is based on vision(line of sight) or movement. True indicates vision should be used.
     * @return true if the path is still valid, otherwise false.
     */
    public boolean validatePath(List<MapLoc> path, boolean los) {
        for (MapLoc step : path) {
            if (!(los ? checkVision(step) : checkOccupation(step))) return false;
        }
        return true;
    }
}

