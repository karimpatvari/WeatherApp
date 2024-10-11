package Apps.Weather.service;

import Apps.Weather.dto.LocationDto;
import Apps.Weather.models.Location;

import java.util.List;

public interface LocationService {
    List<LocationDto> findAllLocations();
}
