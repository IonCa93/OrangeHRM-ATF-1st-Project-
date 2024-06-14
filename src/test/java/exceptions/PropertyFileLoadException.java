package exceptions;

public class PropertyFileLoadException extends RuntimeException {

    public PropertyFileLoadException(String message) {
        super(message);
    }

    public PropertyFileLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}