package exceptions;

public class NoPreviousResponseException extends RuntimeException {
    public NoPreviousResponseException(String message) {
        this(message, null);
    }

    public NoPreviousResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}