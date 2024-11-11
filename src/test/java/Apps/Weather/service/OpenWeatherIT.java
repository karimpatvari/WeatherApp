package Apps.Weather.service;

import Apps.Weather.Json.Weather;
import Apps.Weather.Json.WeatherResponse;
import Apps.Weather.customExceptions.*;
import Apps.Weather.service.impl.OpenWeatherServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class OpenWeatherIT {

    private OpenWeatherService openWeatherService;
    private RestTemplate restTemplateMock;
    private String apiKey = "FAKE_API_KEY";
    private LocationService locationServiceMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        openWeatherService = new OpenWeatherServiceImpl(restTemplateMock, apiKey, locationServiceMock);
    }

    @Test
    public void AssertByCoordinatesThrowsInvalidWeatherRequestException() {
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