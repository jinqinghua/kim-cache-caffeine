package kim.cachecaffeine.domain.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/caches")
@Slf4j
public class CacheController {

    @Qualifier("cacheSpec")
    @Autowired
    Map<String, String> cacheSpec;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping(path = "")
    public ResponseEntity<List<Map<Object, Object>>> listAll() {
        List<Map<Object, Object>> list = new ArrayList<>();

        cacheSpec.forEach((k, v) -> {
            Cache cache = cacheManager.getCache(k);
            list.add(getCacheDetails(cache));
        });

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{cacheName}")
    public ResponseEntity<Map<Object, Object>> listByCacheName(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        return ResponseEntity.ok().body(getCacheDetails(cache));
    }

    @GetMapping(path = "/{cacheName}/{key}")
    public ResponseEntity<Map<Object, Object>> listByCacheKey(@PathVariable String cacheName, @PathVariable Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        return ResponseEntity.ok().body(getCacheDetails(cache, key));
    }

    private Map<Object, Object> getCacheDetails(Cache cache) {
        return getCacheDetails(cache, null);
    }

    private Map<Object, Object> getCacheDetails(Cache cache, Object key) {
        Object nativeCache = cache.getNativeCache();

        Map<Object, Object> detailsMap = new HashMap<>();
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache nativeCaffeineCache = (com.github.benmanes.caffeine.cache.Cache) nativeCache;

            nativeCaffeineCache.asMap().forEach((k, v) -> {
                if (null == key) {
                    detailsMap.put(k, nativeCaffeineCache.getIfPresent(k));
                } else {
                    if (key.equals(k)) {
                        detailsMap.put(k, nativeCaffeineCache.getIfPresent(k));
                    }
                }
            });

            detailsMap.put("estimatedSize", nativeCaffeineCache.estimatedSize());
            detailsMap.put("policy", nativeCaffeineCache.policy().toString());
            detailsMap.put("stats", nativeCaffeineCache.stats().toString());
        }
        return detailsMap;
    }

}
