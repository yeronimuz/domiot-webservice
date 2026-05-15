package org.domiot.webservice.resources;

import org.domiot.webservice.services.DuplicateDeviceException;
import org.domiot.webservice.services.DuplicateSiteException;
import org.domiot.webservice.services.DuplicateUserException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DuplicateExceptionAdviceTest {

    private final DuplicateExceptionAdvice advice = new DuplicateExceptionAdvice();

    @Test
    void shouldMapDuplicateUserExceptionToConflict() {
        ResponseEntity<Void> response = advice.handleDuplicateException(new DuplicateUserException("duplicate user"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldMapDuplicateSiteExceptionToConflict() {
        ResponseEntity<Void> response = advice.handleDuplicateException(new DuplicateSiteException("duplicate site"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldMapDuplicateDeviceExceptionToConflict() {
        ResponseEntity<Void> response = advice.handleDuplicateException(new DuplicateDeviceException("duplicate device"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }
}

