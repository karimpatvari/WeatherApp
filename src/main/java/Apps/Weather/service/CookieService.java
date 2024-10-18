package Apps.Weather.service;

import Apps.Weather.customExceptions.CookieNotFoundException;
import Apps.Weather.customExceptions.SessionNotFoundException;
import jakarta.servlet.http.Cookie;

public interface CookieService {
    Cookie getSessionCookie(Cookie[] cookies) throws CookieNotFoundException;
}
