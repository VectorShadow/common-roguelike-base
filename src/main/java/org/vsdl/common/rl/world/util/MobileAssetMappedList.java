package org.vsdl.common.rl.world.util;

import org.vsdl.common.rl.world.asset.MobileAsset;
import org.vsdl.common.rl.world.map.MapLoc;

import java.util.*;

public class MobileAssetMappedList<E extends MobileAsset> implements Iterable<E> {

    private long modCount = 0;

    private final ArrayList<E> list = new ArrayList<>();
    private final HashMap<MapLoc, List<E>> map = new HashMap<>();

    public List<E> getAssetsAt(MapLoc mapLoc) {
        return map.containsKey(mapLoc) ? map.get(mapLoc) : new ArrayList<>();
    }

    private void addToMap(E e) {
        MapLoc loc = e.getLocation();
        map.computeIfAbsent(loc, l -> new ArrayList<>());
        List<E> assets = map.get(loc);
        assets.add(e);
    }

    private void removeFromMap(E e) {
        MapLoc loc = e.getLocation();
        if (!map.containsKey(loc)) {
            throw new IllegalArgumentException("No asset found at expected location: " + loc);
        }
        List<E> assets = map.get(loc);
        if (!assets.contains(e)) {
            throw new IllegalArgumentException("Expected asset not found at location: " + loc);
        }
        assets.remove(e);
        if (assets.isEmpty()) {
            map.remove(loc);
        }
    }

    public void updateAssetLocation(E e, MapLoc l) {
        removeFromMap(e);
        e.setLocation(l);
        addToMap(e);
        ++modCount;
    }

    public void addAsset(E e) {
        if (list.contains(e)) {
            throw new IllegalStateException("Asset already exists.");
        }
        list.add(e);
        addToMap(e);
        ++modCount;
    }

    public void removeAsset(E e) {
        if (!list.contains(e)) {
            throw new IllegalStateException("Asset was not present.");
        }
        list.remove(e);
        removeFromMap(e);
        ++modCount;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            int cursor;
            int lastRet = -1;
            long expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor != list.size();
            }

            @Override
            public E next() {
                checkForComodification();
                int i = cursor;
                if (i >= list.size()) throw new NoSuchElementException();
                cursor = i + 1;
                return list.get(lastRet = i);
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                checkForComodification();
                E e = list.get(lastRet);
                MobileAssetMappedList.this.removeAsset(e);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            }

            void checkForComodification() {
                if (modCount != expectedModCount) throw new ConcurrentModificationException();
            }
        };
    }
}

