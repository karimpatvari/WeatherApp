package Apps.Weather.service.impl;

import Apps.Weather.dto.LocationDto;
import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import Apps.Weather.repository.LocationRepository;
import Apps.Weather.service.LocationService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> findAllLocationsByUser(User user) {
        return locationRepository.findByUser(user);
    }

    @Override
    public void deleteLocation(Integer locationId) {
        locationRepository.deleteById(Long.valueOf(locationId));
    }

    @Override
    public Optional<Location> getLocationById(Integer locationId) {
        return locationRepository.findLocationById(locationId);
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
