package exceptions;

public class NoPreviousResponseException extends RuntimeException {

    public NoPreviousResponseException(String message) {
        super(message);
    }
}
