package net.wanji.business.util;

import net.wanji.business.domain.WoPostion;
import net.wanji.business.entity.TjScenelib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneLibMap {

    private static Map<Long, TjScenelib> map = new ConcurrentHashMap<>();

    private static Map<String, WoPostion> endMap = new ConcurrentHashMap<>();

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

    public static void putEnd(String id, WoPostion woPostion) {
        endMap.put(id, woPostion);
    }

    public static WoPostion getEnd(String id) {
        WoPostion woPostion = endMap.get(id);
        endMap.remove(id);
        return woPostion;
    }

}
