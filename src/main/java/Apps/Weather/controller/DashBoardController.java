package Apps.Weather.controller;

import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.service.AuthService;
import Apps.Weather.service.CookieService;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Controller
public class DashBoardController {

    private final SessionRepository sessionRepository;
    private UserService userService;
    private CookieService cookieService;
    private SessionService sessionService;
    private AuthService authService;

    @Autowired
    public DashBoardController(SessionRepository sessionRepository, UserService userService, CookieService cookieService, SessionService sessionService, AuthService authService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.cookieService = cookieService;
        this.sessionService = sessionService;
        this.authService = authService;
    }

    @GetMapping("/")
    public String showDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {

        // process the cookie from the request
        Optional<User> userOptional = authService.processCookieAndGetUser(request.getCookies());

        userOptional.ifPresentOrElse(user -> {
            model.addAttribute("login", user.getLogin());
            model.addAttribute("isLoggedIn", true);
            },
                () -> model.addAttribute("isLoggedIn", false)
        );

        // Always return the dashboard view
        return "dashboard";

    }
}
