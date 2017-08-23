package com.ninegold.ninegoldapi.exceptions;

/**
 * The base exception for the application. Thrown if there is an error during CRUD operations.
 */
public class NineGoldException extends Exception {

    /**
     * <p>
     * This is the constructor of <code>NineGoldException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public NineGoldException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>NineGoldException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public NineGoldException(String message, Throwable cause) {
        super(message, cause);
    }
}

