package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.api.UserApi;
import org.lankheet.domiot.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserResource implements UserApi {

    @Override
    public ResponseEntity<User> addUser(User user) {
        log.info("User creation is not implemented yet");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<User> getUser(Long userId) {
        log.info("User lookup is not implemented yet for userId={}", userId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<User>> updateUser(Long userId, User user) {
        log.info("User update is not implemented yet for userId={}", userId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}

