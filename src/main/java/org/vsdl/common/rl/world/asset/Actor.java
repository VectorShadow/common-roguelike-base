package org.vsdl.common.rl.world.asset;

import org.vsdl.common.rl.world.map.MapLoc;

public class Actor implements MobileAsset {

    private final short archetypeId;
    private MapLoc location;
    //todo - character sheet!

    protected Actor(short archetypeId) {
        this.archetypeId = archetypeId;
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
