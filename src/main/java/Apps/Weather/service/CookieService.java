package Apps.Weather.service;

import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface CookieService {
    Optional<Cookie> getSessionCookie(Cookie[] cookies);
}
