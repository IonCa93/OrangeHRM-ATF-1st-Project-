package exceptions;

public class ContextException extends RuntimeException {

    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }
}