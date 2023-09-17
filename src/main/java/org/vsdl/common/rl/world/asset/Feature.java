package org.vsdl.common.rl.world.asset;

public class Feature implements WorldAsset {

    private final short archetypeId;
    private int durability = -1;
    private byte state = 0;

    protected Feature(short archetypeId) {
        this.archetypeId = archetypeId;
    }

    public int getDurability() {
        return durability;
    }

    public byte getState() {
        return state;
    }

    public boolean decreaseDurability(int amount) {
        return (durability -= amount) >= 0;
    }

    public void updateState(byte newState) {
        state = newState;
    }

    @Override
    public short getArchetypeId() {
        return archetypeId;
    }
}
