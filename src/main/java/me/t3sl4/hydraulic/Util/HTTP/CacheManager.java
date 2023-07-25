package me.t3sl4.hydraulic.Util.HTTP;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static Map<String, String> cache = new HashMap<>();

    public static String getFromCache(String key) {
        return cache.get(key);
    }

    public static void addToCache(String key, String value) {
        cache.put(key, value);
    }
}