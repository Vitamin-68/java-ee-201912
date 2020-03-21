package ua.ithillel.dnepr.roman.gizatulin.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import javax.cache.spi.CachingProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Program00 {
    public static <KEY, VALUE> Cache<KEY, VALUE> createCache(String cacheName) {
        final String cacheProviderClassName = "org.....JCacheProvider";
        // CachingProvider cachingProvider = Caching.getCachingProvider(cacheProviderClassName);
        final CachingProvider cachingProvider = Caching.getCachingProvider();
        final CacheManager cacheManager = cachingProvider.getCacheManager();
        final Configuration<KEY, VALUE> config = new MutableConfiguration<KEY, VALUE>()
                .setStoreByValue(false)
                .setCacheLoaderFactory(() -> new CacheLoader<>() {
                    @Override
                    public VALUE load(KEY key) throws CacheLoaderException {
                        return loadAll(Collections.singletonList(key)).get(key);
                    }

                    @Override
                    public Map<KEY, VALUE> loadAll(Iterable<? extends KEY> keys) throws CacheLoaderException {
                        Map<KEY, VALUE> result = new HashMap<>();
                        //TODO: get and return data from database
                        return result;
                    }
                })
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
        return cacheManager.createCache(cacheName, config);
    }

    public static void main(String[] args) {
        Cache<Integer, String> cache = createCache("test_cache_name");
        for (int i = 0; i < 10; i++) {
            cache.put(i, "value_" + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(cache.get(i));
        }
    }
}
