package org.domiot.webservice.services;

/**
 * Raised when a device create request violates uniqueness constraints.
 */
public class DuplicateDeviceException extends RuntimeException {

    public DuplicateDeviceException(String message) {
        super(message);
    }

    public DuplicateDeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}

