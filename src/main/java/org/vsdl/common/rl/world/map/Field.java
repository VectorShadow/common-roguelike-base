package org.vsdl.common.rl.world.map;

import org.vsdl.common.rl.world.asset.*;
import org.vsdl.common.rl.world.util.MobileAssetMappedList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Field implements Serializable {

    private final byte[][] terrain;

    private final HashMap<MapLoc, Feature> features;
    private final HashMap<MapLoc, List<Emplacement>> emplacements;
    private final HashMap<MapLoc, List<Effect>> effects;

    private final MobileAssetMappedList<Projectile> projectiles;
    private final MobileAssetMappedList<Actor> actors;

    public Field(int rows, int columns) {
        terrain = new byte[rows][columns];
        features = new HashMap<>();
        emplacements = new HashMap<>();
        effects = new HashMap<>();
        projectiles = new MobileAssetMappedList<>();
        actors = new MobileAssetMappedList<>();
    }

    public int getHeight() {
        return terrain.length;
    }

    public int getWidth() {
        return terrain[0].length;
    }

    public boolean checkBounds(MapLoc l) {
        int c = l.getCol();
        int r = l.getRow();
        return c >= 0 && c < getWidth() && r >= 0 && r < getHeight();
    }

    public void validateLocation(MapLoc l) {
        int c = l.getCol();
        int r = l.getRow();
        if (c < 0 || c >= getWidth()) {
            throw new IllegalArgumentException("Location " + l + " column out of bounds: " + c + " was not between 0 and " + terrain[0].length +".");
        }
        if (r < 0 || r >= getHeight()) {
            throw new IllegalArgumentException("Location " + l + " row out of bounds: " + r + " was not between 0 and " + terrain.length +".");
        }
    }

    public byte getTerrainAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        return terrain[mapLoc.getRow()][mapLoc.getCol()];
    }

    public void setTerrainAt(final MapLoc mapLoc, final byte value) {
        validateLocation(mapLoc);
        terrain[mapLoc.getRow()][mapLoc.getCol()] = value;
    }

    public void addFeatureAt(final MapLoc mapLoc, final Feature feature) {
        validateLocation(mapLoc);
        if (features.containsKey(mapLoc)) {
            throw new IllegalStateException("May not override feature at location: " + mapLoc);
        }
        features.put(mapLoc, feature);
    }

    public Feature getFeatureAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        return features.get(mapLoc);
    }

    public void removeFeatureAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        features.remove(mapLoc);
    }

    public void addEmplacementAt(final MapLoc mapLoc, final Emplacement emplacement) {
        validateLocation(mapLoc);
        emplacements.computeIfAbsent(mapLoc, l -> new ArrayList<>());
        List<Emplacement> emplacementList = emplacements.get(mapLoc);
        emplacementList.add(emplacement);
    }

    public List<Emplacement> getEmplacementsAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        return emplacements.get(mapLoc);
    }

    public void removeEmplacementAt(final MapLoc mapLoc, final Emplacement emplacement) {
        validateLocation(mapLoc);
        if (!emplacements.containsKey(mapLoc)) {
            throw new IllegalArgumentException("No emplacement found at expected location: " + mapLoc);
        }
        List<Emplacement> emplacementList = emplacements.get(mapLoc);
        if (!emplacementList.contains(emplacement)) {
            throw new IllegalArgumentException("Expected emplacement not found at location: " + mapLoc);
        }
        emplacementList.remove(emplacement);
        if (emplacementList.isEmpty()) {
            emplacements.remove(mapLoc);
        }
    }

    public void addEffectAt(final MapLoc mapLoc, final Effect effect) {
        validateLocation(mapLoc);
        effects.computeIfAbsent(mapLoc, l -> new ArrayList<>());
        List<Effect> effectList = effects.get(mapLoc);
        effectList.add(effect);
    }

    public List<Effect> getEffectsAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        return effects.get(mapLoc);
    }

    public void removeEffectAt(final MapLoc mapLoc, final Effect effect) {
        validateLocation(mapLoc);
        if (!effects.containsKey(mapLoc)) {
            throw new IllegalArgumentException("No effect found at expected location: " + mapLoc);
        }
        List<Effect> effectList = effects.get(mapLoc);
        if (!effectList.contains(effect)) {
            throw new IllegalArgumentException("Expected effect not found at location: " + mapLoc);
        }
        effectList.remove(effect);
        if (effectList.isEmpty()) {
            effects.remove(mapLoc);
        }
    }

    public void addProjectile(final Projectile projectile) {
        projectiles.addAsset(projectile);
    }

    public List<Projectile> getProjectilesAt(final MapLoc mapLoc) {
        validateLocation(mapLoc);
        return projectiles.getAssetsAt(mapLoc);
    }


    public void removeProjectile(final Projectile projectile) {
        projectiles.removeAsset(projectile);
    }

    public void addActor(final Actor actor) {
        actors.addAsset(actor);
    }

    public List<Actor> getActorsAt(MapLoc mapLoc) {
        validateLocation(mapLoc);
        return actors.getAssetsAt(mapLoc);
    }

    public void removeActor(final Actor actor) {
        actors.removeAsset(actor);
    }
}

