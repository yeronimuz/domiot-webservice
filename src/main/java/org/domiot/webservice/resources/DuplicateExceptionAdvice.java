package org.domiot.webservice.resources;

import org.domiot.webservice.services.DuplicateDeviceException;
import org.domiot.webservice.services.DuplicateSiteException;
import org.domiot.webservice.services.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DuplicateExceptionAdvice {

    @ExceptionHandler({DuplicateUserException.class, DuplicateSiteException.class, DuplicateDeviceException.class})
    public ResponseEntity<Void> handleDuplicateException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
