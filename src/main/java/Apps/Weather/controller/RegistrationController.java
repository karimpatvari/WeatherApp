package Apps.Weather.controller;

import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.models.User;
import Apps.Weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration-page";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute("user") User user, Model model){

        try {
            userService.validateAndSaveUser(user);
            return "redirect:/login";

        } catch (UserAlreadyExistsException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "registration-page";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            return "registration-page";
        }
    }

}
