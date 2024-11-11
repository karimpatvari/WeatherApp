package Apps.Weather.repository;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(new User("user", "password"));
    }

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUser() {
        sessionRepository.save(new Session(savedUser, Timestamp.valueOf(LocalDateTime.now())));

        Optional<Session> sessionOptional = sessionRepository.findByUser(savedUser);
        assertTrue(sessionOptional.isPresent());
        assertEquals(sessionOptional.get().getUser().getId(), savedUser.getId());
    }

    @Test
    void findById() {
        Session save = sessionRepository.save(new Session(savedUser, Timestamp.valueOf(LocalDateTime.now())));

        Optional<Session> sessionOptional = sessionRepository.findById(save.getId());
        assertTrue(sessionOptional.isPresent());
        assertEquals(sessionOptional.get().getId(), save.getId());
    }

    @Test
    void sessionWillBeDeletedIfExpired() {
        sessionRepository.save(new Session(savedUser, Timestamp.valueOf(LocalDateTime.now().minusMinutes(1))));

        sessionRepository.deleteAllByExpiresAtBefore(Timestamp.valueOf(LocalDateTime.now()));
        assertTrue(sessionRepository.findAll().isEmpty());
    }
}