package exceptions;

public class PropertyKeyNotFoundException extends RuntimeException {

    public PropertyKeyNotFoundException(String message) {
        super(message);
    }

    public PropertyKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}