package Apps.Weather.service;

import Apps.Weather.Json.GeoResponse;
import Apps.Weather.Json.WeatherResponse;
import Apps.Weather.customExceptions.*;
import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import org.apache.coyote.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface OpenWeatherService {

    WeatherResponse getWeatherByCoordinates(double latitude, double longitude, Optional<String> units) throws WeatherServiceException, WeatherNotFoundException, WeatherRateLimitException;

    List<GeoResponse> getListOfGeoResponsesByLocName(String locationName) throws WeatherServiceException;

    List<WeatherResponse> getWeatherByListOfLocations(List<Location> allLocationsByUser) throws WeatherServiceException;
}
