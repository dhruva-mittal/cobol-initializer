package org.dm.cobol.exception;

/**
 * Exception thrown when errors occur during COBOL record parsing.
 */
public class CobolParseException extends Exception {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public CobolParseException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public CobolParseException(String message, Throwable cause) {
        super(message, cause);
    }
}