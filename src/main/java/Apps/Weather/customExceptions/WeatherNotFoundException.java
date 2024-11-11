package Apps.Weather.customExceptions;

public class WeatherNotFoundException extends Exception {
    public WeatherNotFoundException() {
        super();
    }

    public WeatherNotFoundException(String message) {
        super(message);
    }
}
