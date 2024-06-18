package net.wanji.business.util;

import net.wanji.business.entity.TjScenelib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneLibMap {

    private static Map<Long, TjScenelib> map = new ConcurrentHashMap<>();

    public static void put(Long id, TjScenelib tjScenelib) {
        map.put(id, tjScenelib);
    }

    public static TjScenelib get(Long id) {
        TjScenelib tjScenelib = map.get(id);
        map.remove(id);
        return tjScenelib;
    }

    public static boolean isExist(Long id) {
        return map.containsKey(id);
    }

}
