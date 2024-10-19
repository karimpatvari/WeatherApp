package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.UserRepository;
import Apps.Weather.service.AuthService;
import Apps.Weather.service.CookieService;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private CookieService cookieService;
    private UserService userService;
    private SessionService sessionService;

    @Autowired
    public AuthServiceImpl(CookieService cookieService, UserService userService, SessionService sessionService) {
        this.cookieService = cookieService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Override
    public Session authenticateAndCreateSession(User userForm) throws UserNotFoundException, InvalidCredentialsException {

        User existingUser = userService.findBylogin(userForm.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User with login " + userForm.getLogin() + " not found"));

        if (!userService.checkPassword(userForm.getPassword(), existingUser.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        return sessionService.createForUser(existingUser);
    }

    @Override
    public void logout(Cookie sessionCookie) {
        Optional<Session> byId = sessionService.findById(UUID.fromString(sessionCookie.getValue()));

        if (byId.isPresent()) {
            sessionService.delete(byId.get());
        }
    }

    @Override
    public Optional<User> processCookieAndGetUser(Cookie[] cookieArray) {

        Optional<Cookie> cookieOptional = cookieService.getSessionCookie(cookieArray);

        if (cookieOptional.isPresent()) {
            Cookie cookie = cookieOptional.get();
            Optional<Session> sessionOptional = sessionService.findById(UUID.fromString(cookie.getValue()));

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                Optional<User> userOptional = userService.findById(session.getUser().getId());

                if (userOptional.isPresent()) {
                    return userOptional;
                }
            }
        }

        return Optional.empty();
    }


}
