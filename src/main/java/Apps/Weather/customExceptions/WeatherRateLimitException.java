package Apps.Weather.customExceptions;

public class WeatherRateLimitException extends WeatherServiceException{
    public WeatherRateLimitException() {
        super();
    }

    public WeatherRateLimitException(String message) {
        super(message);
    }
}
