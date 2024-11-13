package Apps.Weather.controller;

import Apps.Weather.Json.GeoResponse;
import Apps.Weather.Json.WeatherResponse;
import Apps.Weather.customExceptions.*;
import Apps.Weather.models.Location;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashBoardController {

    private final AuthService authService;
    private final OpenWeatherService openWeatherService;
    private final LocationService locationService;

    @Autowired
    public DashBoardController(AuthService authService, OpenWeatherService openWeatherService, LocationService locationService) {
        this.authService = authService;
        this.openWeatherService = openWeatherService;
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String showDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {

        try {
            // process the cookie from the request
            Session session = authService.AuthenticateGetSession(request.getCookies());
            User user = session.getUser();

            model.addAttribute("login", user.getLogin());
            model.addAttribute("isLoggedIn", true);

            List<Location> allLocationsByUser = locationService.findAllLocationsByUser(user);
            List<WeatherResponse> WeatherResponsesList = openWeatherService.getWeatherByListOfLocations(allLocationsByUser);

            model.addAttribute("weatherResponsesList", WeatherResponsesList);

        } catch (SessionExpiredException e) {
            model.addAttribute("isLoggedIn", false);


        } catch (WeatherServiceException e) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("error", "An error occurred while loading the weather data. Please try again.");

        } catch (Exception e){
            model.addAttribute("error", "An internal server error occurred while loading the dashboard. Please try again.");

        }

        return "dashboard";
    }

}
