package kim.demo.cachecaffeine.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/caches")
@Slf4j
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{cacheName}/{key}")
    public ResponseEntity<User> listByName(@PathVariable String cacheName, @PathVariable Long key) {
        log.info("cacheNames is {}", cacheManager.getCacheNames());
        Cache cache = cacheManager.getCache(cacheName);
        log.info("cache name is {}", cache.getName());

        com.github.benmanes.caffeine.cache.Cache caffeineCache = (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
        caffeineCache.asMap().forEach((k, v) -> log.info("k={};v={}", k, v));

        // 手动从 cache 中取缓存数据，如果没有，则用 Service 取
        User user = cache.get(key, () -> userService.getUserById(key));
        return ResponseEntity.ok(user);
    }

}
