package Apps.Weather.controller;

import Apps.Weather.customExceptions.CookieNotFoundException;
import Apps.Weather.customExceptions.SessionNotFoundException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.models.User;
import Apps.Weather.service.CookieService;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    private UserService userService;
    private CookieService cookieService;
    private SessionService sessionService;

    @Autowired
    public DashBoardController(UserService userService, CookieService cookieService, SessionService sessionService) {
        this.userService = userService;
        this.cookieService = cookieService;
        this.sessionService = sessionService;
    }

    @GetMapping("/")
    public String showDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {

        try {
            // Retrieve the session cookie from the request
            Cookie sessionCookie = cookieService.getSessionCookie(request.getCookies());
            model.addAttribute("sessionCookie", sessionCookie);

            // Find the user by session ID
            User user = sessionService.findUserBySessionId(sessionCookie.getValue());

            // Add user's login to the model
            model.addAttribute("login", user.getLogin());

        } catch (CookieNotFoundException | UserNotFoundException exception) {}

        // Always return the dashboard view, regardless of whether exceptions were thrown
        return "dashboard";

    }
}
