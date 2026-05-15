package org.domiot.webservice.services;

/**
 * Raised when a user create request violates logical/DB uniqueness constraints.
 */
public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}

