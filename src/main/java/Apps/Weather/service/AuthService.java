package Apps.Weather.service;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.SessionExpiredException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import jakarta.servlet.http.Cookie;

public interface AuthService {

    Session authenticateAndCreateSession(User user) throws UserNotFoundException, InvalidCredentialsException;

    Cookie logout(Cookie[] cookieArray);

    Session AuthenticateGetSession(Cookie[] cookieArray) throws SessionExpiredException;

}
