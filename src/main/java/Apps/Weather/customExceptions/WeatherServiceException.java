package Apps.Weather.customExceptions;

public class WeatherServiceException extends Exception{
    public WeatherServiceException() {
        super();
    }

    public WeatherServiceException(String message) {
        super(message);
    }
}
