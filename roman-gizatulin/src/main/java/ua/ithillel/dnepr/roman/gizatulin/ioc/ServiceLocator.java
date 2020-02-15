package ua.ithillel.dnepr.roman.gizatulin.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ServiceLocator {
    private final static Map<String, Map<Class<?>, Object>> KEY_INSTANCES = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    public static <T> void register(Class<? extends T> clazz, T instance) {
        register(null, clazz, instance);
    }

    public static <T> void register(String key, Class<? extends T> clazz, T instance) {
        try {
            lock.lock();
            if (!KEY_INSTANCES.containsKey(key)) {
                KEY_INSTANCES.put(key, new HashMap<>());
            }
            KEY_INSTANCES.get(key).put(clazz, instance);
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("...")
    public static <T> T getInstance(String key, Class<? extends T> clazz) {
        T result = null;
        try {
            lock.lock();
            if (!KEY_INSTANCES.containsKey(key)) {
                KEY_INSTANCES.put(key, new HashMap<>());
            }
            result = (T) KEY_INSTANCES.get(key).get(clazz);
        } finally {
            lock.unlock();
        }
        return result;
    }

    public static <T> T getInstance(Class<? extends T> clazz) {
        return getInstance(null, clazz);
    }
}
