package Apps.Weather.service.impl;

import Apps.Weather.Json.GeoResponse;
import Apps.Weather.Json.Main;
import Apps.Weather.Json.Weather;
import Apps.Weather.Json.WeatherResponse;
import Apps.Weather.customExceptions.*;
import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import Apps.Weather.service.LocationService;
import Apps.Weather.service.OpenWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OpenWeatherServiceImpl implements OpenWeatherService {

    private static ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final LocationService locationService;

    @Autowired
    public OpenWeatherServiceImpl(RestTemplate restTemplate, @Value("${openweather.api.key}") String apiKey, LocationService locationService) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        objectMapper = new ObjectMapper();
        this.locationService = locationService;
    }

    @Override
    public WeatherResponse getWeatherByCoordinates(double latitude, double longitude, Optional<String> units) throws WeatherServiceException, WeatherNotFoundException, WeatherRateLimitException {

        // Validate coordinates to ensure they are within acceptable ranges
        validateCoordinates(latitude, longitude);

        // Construct the URI for the OpenWeather API request
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParamIfPresent("units", units)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        try {
            // Make the API request and get the response
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            // Check if the response is successful and contains a body
            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                throw new WeatherNotFoundException("Weather for this latitude and longitude not found. Please check the input.");
            }

            // Parse the JSON response into a WeatherResponse object
            return objectMapper.readValue(responseEntity.getBody(), WeatherResponse.class);

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new WeatherServiceException("Unauthorized request: Please check your API key.");

        } catch (HttpClientErrorException.Forbidden e) {
            throw new WeatherServiceException("Access denied: You may be exceeding your subscription limits.");

        } catch (HttpClientErrorException.NotFound e) {
            throw new WeatherNotFoundException("Weather for this latitude and longitude not found. Please check the input.");

        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new WeatherRateLimitException("Rate limit exceeded. Please try again later.");

        } catch (HttpServerErrorException e) {
            throw new WeatherServiceException("OpenWeather API server error: " + e.getStatusCode());

        } catch (HttpClientErrorException e) {
            throw new WeatherServiceException("HTTP error while fetching weather data: " + e.getStatusCode());

        } catch (JsonProcessingException e) {
            throw new WeatherServiceException("Error processing weather data" + e.getMessage());

        } catch (Exception e) {
            throw new WeatherServiceException("An unexpected error occurred" + e.getMessage());
        }
    }

    @Override
    public List<GeoResponse> getListOfGeoResponsesByLocName(String locationName) throws WeatherServiceException {

        String uri = UriComponentsBuilder
                .fromUriString("http://api.openweathermap.org/geo/1.0/direct")
                .queryParam("q", locationName)
                .queryParam("limit", "10")
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            return objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<GeoResponse>>() {});

        } catch (Exception e) {
            throw new WeatherServiceException("Error processing weather data" + e.getMessage());
        }
    }

    @Override
    public List<WeatherResponse> getWeatherForUser(User user) throws WeatherServiceException, WeatherNotFoundException, WeatherRateLimitException {
        List<Location> allLocationsByUser = locationService.findAllLocationsByUser(user);
        return getWeatherByListOfLocations(allLocationsByUser);
    }

    private List<WeatherResponse> getWeatherByListOfLocations(List<Location> allLocationsByUser) throws WeatherRateLimitException, WeatherNotFoundException, WeatherServiceException {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        for (Location location : allLocationsByUser) {
            WeatherResponse weatherByCoordinates = null;

            try{
                weatherByCoordinates = getWeatherByCoordinates(location.getLatitude(), location.getLongitude(), Optional.of("metric"));
            } catch (Exception e){
                throw e;
            }

            weatherByCoordinates.setLocationId(location.getId());
            weatherByCoordinates.setName(location.getName());
            weatherResponses.add(weatherByCoordinates);

        }

        return weatherResponses;
    }

    private void validateCoordinates(double latitude, double longitude) throws IllegalArgumentException {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180 ){
            throw new IllegalArgumentException("Invalid input for weather request, try changing latitude or longitude");
        }
    }



}
