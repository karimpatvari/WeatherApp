package Apps.Weather.repository;

import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.models.Location;
import Apps.Weather.models.User;
import Apps.Weather.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    private Location location;

    @BeforeEach
    void setUp() throws UserAlreadyExistsException {
        user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.save(user);

        location = new Location();
        location.setName("london");
        location.setLongitude(1.0);
        location.setLatitude(1.0);
        location.setUser(user);
        locationRepository.save(location);
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUser() {
        List<Location> byUser = locationRepository.findByUser(user);
        assertNotNull(byUser);
        assertNotNull(byUser.getFirst());

        Location first = byUser.getFirst();

        assertEquals("london", first.getName());
        assertEquals(1.0, first.getLongitude());
        assertEquals(1.0, first.getLatitude());
        assertEquals(user.getId(), first.getUser().getId());
    }

    @Test
    void findById() {
        Optional<Location> byId = locationRepository.findLocationById(location.getId());
        assertTrue(byId.isPresent());
        Location location1 = byId.get();

        assertEquals("london", location1.getName());
        assertEquals(1.0, location1.getLongitude());
        assertEquals(1.0, location1.getLatitude());
        assertEquals(user.getId(), location1.getUser().getId());

    }

}