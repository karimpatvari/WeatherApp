package Apps.Weather.service.impl;

import Apps.Weather.service.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Optional<Cookie> getSessionCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .findFirst();
    }

}
