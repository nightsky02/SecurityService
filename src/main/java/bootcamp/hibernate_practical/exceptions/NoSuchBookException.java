package bootcamp.hibernate_practical.exceptions;

public class NoSuchBookException extends RuntimeException {
    public NoSuchBookException(String message) {
        super(message);
    }
}
