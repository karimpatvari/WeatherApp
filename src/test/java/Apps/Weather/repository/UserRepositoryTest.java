package Apps.Weather.repository;

import Apps.Weather.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByLogin() {
        userRepository.save(new User("Login", "Password"));
        Optional<User> user = userRepository.findBylogin("Login");
        assertTrue(user.isPresent());
    }

    @Test
    void findById(){
        User savedUser = userRepository.save(new User("Login", "Password"));
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }
}