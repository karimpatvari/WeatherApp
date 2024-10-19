package Apps.Weather.customExceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException() {
        super();
    }
}
