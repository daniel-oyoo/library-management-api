package com.daniel.library_management.exception;

/**
 * Exception thrown when a resource is not found.
 * 
 * @author Daniel
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}