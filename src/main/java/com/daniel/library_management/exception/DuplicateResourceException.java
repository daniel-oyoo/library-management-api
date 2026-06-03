package com.daniel.library_management.exception;

/**
 * Exception thrown when a duplicate resource is being created.
 * 
 * @author Daniel
 * @version 1.0.0
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}