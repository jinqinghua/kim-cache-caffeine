package com.example.democachecaffeine.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/users")
@CacheConfig(cacheNames = "usersCache")
@Slf4j
public class UserController {
    private static Map<Long, User> users = new ConcurrentHashMap<>(16);

    @GetMapping
    @Cacheable
    public ResponseEntity<List<User>> listUser() {
        log.info("--->List user from repository...");
        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    @GetMapping(value = "/{id}")
    @Cacheable(key = "#id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("--->Get user from repository...");
        return ResponseEntity.ok(users.get(id));
    }

    @PostMapping
    @CachePut(key = "#user.id")
    public ResponseEntity postUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return ResponseEntity.created(null).build();
    }

    @PutMapping(value = "/{id}")
    @CachePut(key = "#id")
    public ResponseEntity putUser(@PathVariable Long id, User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(key = "#id")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        users.remove(id);
        return ResponseEntity.noContent().build();
    }
}
