package ua.ithillel.dnepr.roman.gizatulin.spi;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ServiceProvider {
    private static final Map<Class<?>, ServiceLoader<?>> SERVICE_LOADERS = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    @SuppressWarnings("unchecked")
    public static <T> List<T> getServices(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        ServiceLoader<T> serviceLoader;
        if (!SERVICE_LOADERS.containsKey(clazz)) {
            synchronized (LOCK) {
                SERVICE_LOADERS.put(
                        clazz,
                        ServiceLoader.load(clazz, ServiceProvider.class.getClassLoader()));
            }
        }
        serviceLoader = (ServiceLoader<T>) SERVICE_LOADERS.get(clazz);
        serviceLoader.forEach(result::add);

        return Collections.unmodifiableList(result);
    }

    @SuppressWarnings("unchecked")
    public static <T> void reloadServices(Class<T> clazz) {
        ServiceLoader<T> serviceLoader;
        boolean firstAdded = false;
        if (!SERVICE_LOADERS.containsKey(clazz)) {
            synchronized (LOCK) {
                firstAdded = true;
                SERVICE_LOADERS.put(
                        clazz,
                        ServiceLoader.load(clazz, ServiceProvider.class.getClassLoader()));
            }
        }
        if (!firstAdded) {
            serviceLoader = (ServiceLoader<T>) SERVICE_LOADERS.get(clazz);
            serviceLoader.reload();
        }
    }
}
