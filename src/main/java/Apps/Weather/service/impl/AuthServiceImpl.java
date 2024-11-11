package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.SessionExpiredException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.service.AuthService;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;
    private SessionService sessionService;

    @Autowired
    public AuthServiceImpl(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Override
    public Session authenticateAndCreateSession(User userForm) throws UserNotFoundException, InvalidCredentialsException {

        User existingUser = userService.findBylogin(userForm.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User with login " + userForm.getLogin() + " not found"));

        if (!checkPassword(userForm.getPassword(), existingUser.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        return sessionService.createForUser(existingUser);
    }

    @Override
    public Cookie logout(Cookie[] cookieArray) {

        getSessionCookie(cookieArray).ifPresent(cookie -> {
                    sessionService.findById(UUID.fromString(cookie.getValue()))
                            .ifPresent(session -> sessionService.delete(session));
                }
        );

        Cookie cookie = new Cookie("sessionId", null);
        cookie.setMaxAge(0);
        return cookie;

    }

    @Override
    public Session AuthenticateGetSession(Cookie[] cookieArray) throws SessionExpiredException {

        Cookie cookie = getSessionCookie(cookieArray).orElseThrow(() -> new SessionExpiredException("Your session expired"));

        UUID sessionId = UUID.fromString(cookie.getValue());
        return checkSession(sessionId);

    }

    private Session checkSession(UUID sessionId) throws SessionExpiredException {

        Session session = sessionService.findById(sessionId).orElseThrow(
                () -> new SessionExpiredException("Your session expired"));

        Timestamp expiresAt = session.getExpiresAt();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        if (now.after(expiresAt)) {
            sessionService.delete(session);
            throw new SessionExpiredException("Your session expired");
        }

        return session;
    }

    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    private Optional<Cookie> getSessionCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .findFirst();
    }

}
