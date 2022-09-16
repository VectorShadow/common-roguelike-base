package org.vsdl.common.rl.world.asset;

import org.vsdl.common.rl.world.map.MapLoc;

public interface MobileAsset extends WorldAsset {
    public MapLoc getLocation();
    public void setLocation(MapLoc location);
}
