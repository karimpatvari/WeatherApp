package Apps.Weather.service;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void authenticateAndCreateSession_Success() throws UserAlreadyExistsException, UserNotFoundException, InvalidCredentialsException {

        User hashedUser = userService.validateAndSaveUser(new User("username", "password"));
        User user = new User("username", "password");

        authService.authenticateAndCreateSession(user);

        Optional<Session> sessionOptional = sessionService.findByUser(hashedUser);

        assertTrue(sessionOptional.isPresent());
        assertTrue(checkPassword(
                user.getPassword(),
                sessionOptional.get().getUser().getPassword()
        ));
    }

    @Test
    void authenticateAndCreateSession_ThrowsInvalidCredentialsException() throws UserAlreadyExistsException {
        userService.validateAndSaveUser(new User("username", "password"));
        User user = new User("username", "wrongPassword");

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticateAndCreateSession(user));
    }

    @Test
    void authenticateAndCreateSession_ThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> authService.authenticateAndCreateSession(new User()));
    }

    @Test
    void logout_DeletesSessionAndReturnsExpiredCookie() throws UserAlreadyExistsException, UserNotFoundException, InvalidCredentialsException {
        User hashedUser = userService.validateAndSaveUser(new User("username", "password"));
        User user = new User("username", "password");
        authService.authenticateAndCreateSession(user);
        Optional<Session> sessionOptional = sessionService.findByUser(hashedUser);

        assertTrue(sessionOptional.isPresent());
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("sessionId", sessionOptional.get().getId().toString());

        Cookie logoutCookie = authService.logout(cookies);

        assertInstanceOf(Cookie.class, logoutCookie);
        assertNull(logoutCookie.getValue());
        assertEquals(0, logoutCookie.getMaxAge());
        assertTrue(sessionRepository.findAll().isEmpty());
    }

    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}