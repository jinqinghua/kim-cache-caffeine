package kim.cachecaffeine;

import kim.cachecaffeine.domain.user.User;
import kim.cachecaffeine.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheCaffeineApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            userService.createUser(User.builder().id(Long.valueOf(i)).name("name" + i).age(20 + i).build());
        }

        userService.getUserById(1L);
        userService.getUserById(2L);
        userService.getUserById(4L);

        for (int i = 6; i <= 10; i++) {
            userService.createUser(User.builder().id(Long.valueOf(i)).name("name" + i).age(20 + i).build());
        }
    }
}
