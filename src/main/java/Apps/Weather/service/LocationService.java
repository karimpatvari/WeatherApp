package Apps.Weather.service;

import Apps.Weather.dto.LocationDto;
import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    Location saveLocation(Location location);

    List<Location> findAllLocationsByUser(User user);

    void deleteLocation(Integer locationId);

    Optional<Location> getLocationById(Integer locationId);
}
