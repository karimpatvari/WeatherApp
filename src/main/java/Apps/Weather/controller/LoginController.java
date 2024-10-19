package Apps.Weather.controller;

import Apps.Weather.customExceptions.InvalidCredentialsException;
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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController {

    private AuthService authService;
    private CookieService cookieService;

    @Autowired
    public LoginController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login-page";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User userForm, Model model, HttpServletResponse response){
        try {
            // Authenticate and create session
            Session session = authService.authenticateAndCreateSession(userForm);

            // Set session ID in a secure cookie
            Cookie sessionCookie = new Cookie("sessionId", session.getId().toString());
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(2 * 60 * 60); // Session lasts 2 hours
            response.addCookie(sessionCookie);

            // Redirect to dashboard
            return "redirect:/dashboard";
        }catch (UserNotFoundException | InvalidCredentialsException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "login-page";
        }
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response){

        Optional<Cookie> sessionCookie = cookieService.getSessionCookie(request.getCookies());

        if(sessionCookie.isPresent()){

            authService.logout(sessionCookie.get());

            Cookie cookie = new Cookie("sessionId", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "redirect:/dashboard";
    }


}
