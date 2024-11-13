package Apps.Weather.service;

import Apps.Weather.Json.WeatherResponse;
import Apps.Weather.customExceptions.*;
import Apps.Weather.service.impl.OpenWeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class OpenWeatherServiceTest {

    private OpenWeatherService openWeatherService;
    private RestTemplate restTemplateMock;
    private String apiKey = "FAKE_API_KEY";

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        openWeatherService = new OpenWeatherServiceImpl(restTemplateMock, apiKey);
    }

    @Test
    void getWeatherByCoordinates_UNAUTHORIZED() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "unauthorized", null, null, null));

        //then
        assertThrows( WeatherServiceException.class,

        //when
            () -> {
                try{
                openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());
                }catch (Exception e){
                assertEquals("Unauthorized request: Please check your API key.", e.getMessage());
                throw e;
                }
        });
    }

    @Test
    void getWeatherByCoordinates_FORBIDDEN() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(HttpClientErrorException.create(HttpStatus.FORBIDDEN, "forbidden", null, null, null));

        //then
        assertThrows( WeatherServiceException.class,

                //when
                () -> {
                    try{
                        openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());
                    }catch (Exception e){
                        assertEquals("Access denied: You may be exceeding your subscription limits.", e.getMessage());
                        throw e;
                    }
                });
    }

    @Test
    void getWeatherByCoordinates_NOT_FOUND() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "forbidden", null, null, null));

        //then
        assertThrows( WeatherServiceException.class,

                //when
                () -> {
                    try{
                        openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());
                    }catch (Exception e){
                        assertEquals("Weather for this latitude and longitude not found. Please check the input.", e.getMessage());
                        throw e;
                    }
                });
    }

    @Test
    void getWeatherByCoordinates_TOO_MANY_REQUESTS() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(HttpClientErrorException.create(HttpStatus.TOO_MANY_REQUESTS, "forbidden", null, null, null));

        //then
        assertThrows( WeatherServiceException.class,

                //when
                () -> {
                    try{
                        openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());
                    }catch (Exception e){
                        assertEquals("Rate limit exceeded. Please try again later.", e.getMessage());
                        throw e;
                    }
                }
        );
    }

    @Test
    void getWeatherByCoordinates_Success() throws WeatherServiceException {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        String mockedJsonResponse = "{\"coord\":{\"lon\":30,\"lat\":30},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"main\":{\"temp\":287.39,\"feels_like\":287.04,\"temp_min\":287.39,\"temp_max\":287.39,\"pressure\":1021,\"humidity\":83,\"sea_level\":1021,\"grnd_level\":1002},\"visibility\":10000,\"wind\":{\"speed\":3.83,\"deg\":23,\"gust\":4.64},\"clouds\":{\"all\":84},\"dt\":1731459689,\"sys\":{\"country\":\"EG\",\"sunrise\":1731471822,\"sunset\":1731510307},\"timezone\":7200,\"id\":353223,\"name\":\"Madīnat as Sādāt\",\"cod\":200}";

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenReturn(new ResponseEntity<String>(mockedJsonResponse, HttpStatus.OK));

        //when
        WeatherResponse weatherByCoordinates = openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());

        //then
        assertNotNull(weatherByCoordinates);
        assertEquals("Madīnat as Sādāt", weatherByCoordinates.getName());
        assertEquals(10000, weatherByCoordinates.getVisibility());
    }

    @Test
    void getWeatherByCoordinates_default() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 30.0)
                .queryParam("lon", 30.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(HttpClientErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "anyOtherStatusCode", null, null, null));

        //then
        assertThrows( WeatherServiceException.class,

                //when
                () -> {
                    try{
                        openWeatherService.getWeatherByCoordinates(30, 30, Optional.empty());
                    }catch (Exception e){
                        assertTrue(e.getMessage().contains("HTTP error while fetching weather data: "));
                        throw e;
                    }
                }
        );
    }

    @Test
    void getWeatherByCoordinates_ThrowsInvalidWeatherRequestException() {
        String uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", 200.0)
                .queryParam("lon", 200.0)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        //given
        when(restTemplateMock.getForEntity(uri, String.class))
                .thenThrow(IllegalArgumentException.class);

        //then
        assertThrows(IllegalArgumentException.class, () ->
                //when
                openWeatherService.getWeatherByCoordinates(200.0, 200.0, Optional.empty())
        );
    }




}