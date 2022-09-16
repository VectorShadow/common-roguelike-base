package org.vsdl.common.rl.world.asset;

public class Effect implements WorldAsset{

    private final short archetypeId;
    private final long expires;

    protected Effect(short archetypeId, long expirationTimestamp) {
        this.archetypeId = archetypeId;
        this.expires = expirationTimestamp;
    }

    public long getExpires() {
        return expires;
    }

    @Override
    public short getArchetypeId() {
        return archetypeId;
    }
}
