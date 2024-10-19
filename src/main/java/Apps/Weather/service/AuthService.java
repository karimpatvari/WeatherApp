package Apps.Weather.service;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface AuthService {
    Session authenticateAndCreateSession(User user);

    void logout(Cookie sessionCookie);

    Optional<User> processCookieAndGetUser(Cookie[] cookieArray);
}
