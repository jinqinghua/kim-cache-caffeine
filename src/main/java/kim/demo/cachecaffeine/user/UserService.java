package kim.demo.cachecaffeine.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache 还是放在 Service 里比较好，不要放 Controller
 */
@Slf4j
@CacheConfig(cacheNames = "usersCache")
@Service
public class UserService {
    private static Map<Long, User> usersStore = new ConcurrentHashMap<>(16);

    @PostConstruct
    public void init() {
        usersStore.computeIfAbsent(1L, k -> User.builder().id(k).name("name1").age(21).build());
        usersStore.computeIfAbsent(2L, k -> User.builder().id(k).name("name2").age(22).build());
        usersStore.computeIfAbsent(3L, k -> User.builder().id(k).name("name3").age(23).build());
        usersStore.computeIfAbsent(4L, k -> User.builder().id(k).name("name4").age(24).build());
        usersStore.computeIfAbsent(5L, k -> User.builder().id(k).name("name5").age(25).build());
    }

    @Cacheable
    public List<User> listUsers() {
        log.info("Load user from usersStore...");
        return new ArrayList<>(usersStore.values());
    }

    @Cacheable(key = "#id")
    public User getUserById(Long id) {
        log.info("Load user from usersStore...");
        return usersStore.get(id);
    }

    @CachePut(key = "#user.id")
    public void createUser(User user) {
        usersStore.put(user.getId(), user);
    }

    @CachePut(key = "#id")
    public User updateUser(Long id, User user) {
        User u = usersStore.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        usersStore.put(id, u);
        return u;
    }

    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        usersStore.remove(id);
    }
}
