package org.vsdl.common.rl.world.asset;

public class Emplacement implements WorldAsset {

    private final short archetypeId;

    protected Emplacement(short archetypeId) {
        this.archetypeId = archetypeId;
    }

    @Override
    public short getArchetypeId() {
        return archetypeId;
    }
}
