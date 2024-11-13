package Apps.Weather.customExceptions;

public class WeatherNotFoundException extends WeatherServiceException {
    public WeatherNotFoundException() {
        super();
    }

    public WeatherNotFoundException(String message) {
        super(message);
    }
}
