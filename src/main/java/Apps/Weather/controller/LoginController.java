package Apps.Weather.controller;

import Apps.Weather.customExceptions.InvalidPasswordException;
import Apps.Weather.customExceptions.SessionNotFoundException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.service.CookieService;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class LoginController {

    private final SessionRepository sessionRepository;
    private UserService userService;
    private SessionService sessionService;
    private CookieService cookieService;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService, CookieService cookieService, SessionRepository sessionRepository) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login-page";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User userForm, Model model, HttpServletResponse response){

        try {
            // Find user by login
            User foundUser = userService.findBylogin(userForm.getLogin());

            // Check password correctness
            if (!userService.checkPassword(userForm.getPassword(), foundUser.getPassword())) {
                throw new InvalidPasswordException();
            }

            // Check if session exists for the user
            Session session = sessionService.findSessionByUser(foundUser);

            if (session == null) {
                // Create a new session if not found
                session = sessionService.createSessionForUser(foundUser);
            }

            // Add session ID to a cookie
            Cookie cookie = new Cookie("sessionId", session.getId().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Redirect to dashboard
            return "redirect:/dashboard";

        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", "User not found");
            return "login-page";

        } catch (InvalidPasswordException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login-page";
        }
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response){

        try{
            Cookie sessionCookie = cookieService.getSessionCookie(request.getCookies());
            sessionService.deleteById(UUID.fromString(sessionCookie.getValue()));

            Cookie cookie = new Cookie("sessionId", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return "redirect:/dashboard";
        } catch (SessionNotFoundException e) {
            return "redirect:/dashboard";
        }

    }


}
