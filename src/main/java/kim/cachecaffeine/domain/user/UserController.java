package kim.cachecaffeine.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> listUser() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

//    @PostMapping
//    public ResponseEntity postUser(@RequestBody User user) {
//        userService.createUser(user);
//        return ResponseEntity.created(null).build();
//    }

    @PostMapping
    public ResponseEntity postUser(@RequestBody List<User> users) {
        users.stream().forEach(user -> userService.createUser(user));
        return ResponseEntity.created(null).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity putUser(@PathVariable Long id, User user) {
        userService.updateUser(id, user);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
