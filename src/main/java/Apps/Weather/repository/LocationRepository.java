package Apps.Weather.repository;

import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository  extends JpaRepository<Location, Long> {
    List<Location> findByUser(User user);

    Optional<Location> findLocationById(Integer id);

}
