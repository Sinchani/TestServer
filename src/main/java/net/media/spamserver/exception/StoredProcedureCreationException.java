package net.media.spamserver.exception;

/**
 * Created by vivek on 7/29/15.
 */
public class StoredProcedureCreationException extends Exception {
    private String message = null;

    public StoredProcedureCreationException() {
        super();
    }

    public StoredProcedureCreationException(String message) {
        super(message);
        this.message = message;
    }

    public StoredProcedureCreationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}