package org.domiot.webservice.resources;

import org.domiot.webservice.services.DuplicateUserException;
import org.domiot.webservice.services.UserService;
import org.domiot.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserResource userResource;

    @Test
    void addUserShouldReturnBadRequestWhenUserIsNull() {
        ResponseEntity<User> response = userResource.addUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).addUser(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void addUserShouldPropagateDuplicateWhenServiceThrowsDuplicateUserException() {
        User input = new User();
        when(userService.addUser(input)).thenThrow(new DuplicateUserException("duplicate"));

        assertThrows(DuplicateUserException.class, () -> userResource.addUser(input));
    }

    @Test
    void addUserShouldReturnOkWhenServiceSucceeds() {
        User input = new User();
        User created = new User();
        when(userService.addUser(input)).thenReturn(created);

        ResponseEntity<User> response = userResource.addUser(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(created, response.getBody());
    }
}

