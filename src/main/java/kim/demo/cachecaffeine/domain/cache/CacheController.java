package kim.demo.cachecaffeine.domain.cache;

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
            Map<Object, Object> map = new HashMap<>();
            map.put(k, v);
            Cache cache = cacheManager.getCache(k);
            setNativeCacheDetails(cache, map);
            list.add(map);
        });

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{cacheName}")
    public ResponseEntity<Map<Object, Object>> listByCacheName(@PathVariable String cacheName) {
        Map<Object, Object> map = new HashMap<>();
        Cache cache = cacheManager.getCache(cacheName);
        setNativeCacheDetails(cache, map);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping(path = "/{cacheName}/{key}")
    public ResponseEntity<Map<Object, Object>> listByCacheKey(@PathVariable String cacheName, @PathVariable Object key) {
        Map<Object, Object> map = new HashMap<>();
        Cache cache = cacheManager.getCache(cacheName);
        map.put(key, cache.get(key));
        return ResponseEntity.ok().body(map);
    }

    private void setNativeCacheDetails(Cache cache, Map<Object, Object> map) {
        com.github.benmanes.caffeine.cache.Cache caffeineCache = (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
        caffeineCache.asMap().forEach((k, v) -> map.put(k, cache.get(k)));
        map.put("estimatedSize", caffeineCache.estimatedSize());
        map.put("stats", caffeineCache.stats().toString());
    }

}
