package org.domiot.webservice.services;

/**
 * Raised when a site create request violates uniqueness constraints.
 */
public class DuplicateSiteException extends RuntimeException {

    public DuplicateSiteException(String message) {
        super(message);
    }

    public DuplicateSiteException(String message, Throwable cause) {
        super(message, cause);
    }
}

