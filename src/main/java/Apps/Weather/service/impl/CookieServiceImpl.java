package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.CookieNotFoundException;
import Apps.Weather.customExceptions.SessionNotFoundException;
import Apps.Weather.service.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Cookie getSessionCookie(Cookie[] cookies) throws CookieNotFoundException {
        Cookie sessionCookie = null;
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")){
                    sessionCookie = cookie;
                }
            }
        }catch (Exception e){}
        if (sessionCookie == null) {
            throw new CookieNotFoundException();
        }else {
            return sessionCookie;
        }
    }

}
