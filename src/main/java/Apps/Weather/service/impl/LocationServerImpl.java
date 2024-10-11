package Apps.Weather.service.impl;

import Apps.Weather.dto.LocationDto;
import Apps.Weather.models.Location;
import Apps.Weather.repository.LocationRepository;
import Apps.Weather.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServerImpl implements LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationServerImpl(LocationRepository locationRepository) {}

    @Override
    public List<LocationDto> findAllLocations() {
       List<Location> locations = locationRepository.findAll();
       return locations.stream().map((location) -> mapLocationToLocationDto(location)).collect(Collectors.toList());
    }

    private LocationDto mapLocationToLocationDto(Location location) {
        LocationDto locationDto = LocationDto.builder()
                .id(location.getId())
                .userId(location.getUser().getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .name(location.getName())
                .build();
        return locationDto;
    }
}
