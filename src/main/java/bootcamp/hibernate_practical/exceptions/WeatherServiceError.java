package bootcamp.hibernate_practical.exceptions;

public class WeatherServiceError extends RuntimeException {
    public WeatherServiceError(String message) {
        super(message);
    }
}
