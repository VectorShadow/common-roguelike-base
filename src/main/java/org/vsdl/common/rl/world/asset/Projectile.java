package org.vsdl.common.rl.world.asset;

import org.vsdl.common.rl.world.map.MapLoc;

public class Projectile implements MobileAsset {

    private final short archetypeId;
    private MapLoc location;
    private MapLoc destination;

    protected Projectile(short archetypeId) {
        this.archetypeId = archetypeId;
    }

    public MapLoc getDestination() {
        return destination;
    }

    public void setDestination(MapLoc destination) {
        this.destination = destination;
    }

    @Override
    public MapLoc getLocation() {
        return location;
    }

    @Override
    public void setLocation(MapLoc location) {
        this.location = location;
    }

    @Override
    public short getArchetypeId() {
        return archetypeId;
    }
}
