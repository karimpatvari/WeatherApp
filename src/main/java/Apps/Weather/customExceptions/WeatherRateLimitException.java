package Apps.Weather.customExceptions;

public class WeatherRateLimitException extends Exception{
    public WeatherRateLimitException() {
        super();
    }

    public WeatherRateLimitException(String message) {
        super(message);
    }
}
