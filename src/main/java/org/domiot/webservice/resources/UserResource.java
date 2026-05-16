package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.UserService;
import org.domiot.api.UserApi;
import org.domiot.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserResource implements UserApi {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> addUser(@RequestBody(required = false) User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Creating user: {}", user);
        return ResponseEntity.ok(userService.addUser(user));
    }

    @Override
    public ResponseEntity<User> getUser(Long userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<User>> updateUser(Long userId, @RequestBody(required = false) User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return userService.updateUser(userId, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

