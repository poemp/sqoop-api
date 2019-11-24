package org.poem.exception;

/**
 * SqoopSessionException
 */
public class SqoopSessionException extends Exception {

    /**
     * Constructs a {@code SqoopSessionException} with no detail message.
     */
    public SqoopSessionException() {
        super();
    }

    /**
     * Constructs a {@code SqoopSessionException} with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public SqoopSessionException(String s) {
        super(s);
    }
}
