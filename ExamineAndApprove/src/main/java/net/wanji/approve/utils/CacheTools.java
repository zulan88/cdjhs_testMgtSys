package net.wanji.approve.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheTools {

    private static Map<Integer, String> cache = new ConcurrentHashMap<>();

    public static void put(Integer key, String value) {
        cache.put(key, value);
    }

    public static String get(Integer key) {
        return cache.get(key);
    }

    public static void remove(Integer key) {
        cache.remove(key);
    }

}
