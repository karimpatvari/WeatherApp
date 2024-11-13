package Apps.Weather.controller;

import Apps.Weather.Json.GeoResponse;
import Apps.Weather.customExceptions.SessionExpiredException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.customExceptions.WeatherServiceException;
import Apps.Weather.models.Location;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.service.AuthService;
import Apps.Weather.service.LocationService;
import Apps.Weather.service.OpenWeatherService;
import Apps.Weather.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SearchPageController {

    private OpenWeatherService openWeatherService;
    private LocationService locationService;
    private AuthService authService;

    @Autowired
    public SearchPageController(LocationService locationService, AuthService authService, OpenWeatherService openWeatherService) {
        this.locationService = locationService;
        this.authService = authService;
        this.openWeatherService = openWeatherService;
    }

    @PostMapping("/save")
    public String saveLocation(
            @RequestParam("name") String name,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            Model model,
            HttpServletRequest request) {

        try {
            // Retrieve the user from cookies
            Session session = authService.AuthenticateGetSession(request.getCookies());
            User user = session.getUser();

            // Validate input fields
            if (user.getId() == null || name == null || name.isEmpty() || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("Invalid location details.");
            }

            // Save the location
            locationService.saveLocation(new Location(name, user, latitude, longitude));

            return "redirect:/";  // Redirect to the home page on success

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        } catch (SessionExpiredException e) {
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationId") Integer locationId, Model model, HttpServletRequest request) {

        try {
            Session session = authService.AuthenticateGetSession(request.getCookies());

            if (locationId == null) {
                throw new BadRequestException("Invalid location ID.");
            }

            Optional<Location> locationById = locationService.getLocationById(locationId);

            if (locationById.isPresent()) {

                if (Objects.equals(locationById.get().getUser().getId(), session.getUser().getId())) {
                    locationService.deleteLocation(locationId);
                } else {
                    throw new AccessDeniedException("Access denied");
                }
            }

            return "redirect:/";

        } catch (AccessDeniedException | BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        } catch (SessionExpiredException e) {
            return "redirect:/dashboard";
        }
    }


    @GetMapping("/search")
    public String showResultsForm(@RequestParam String locationStr, Model model, HttpServletRequest request) {

        try {
            // process the cookie from the request
            Session session = authService.AuthenticateGetSession(request.getCookies());
            User user = session.getUser();

            List<GeoResponse> listOfGeoResponses = openWeatherService.getListOfGeoResponsesByLocName(locationStr);
            model.addAttribute("listOfGeoResponses", listOfGeoResponses);
            model.addAttribute("login", user.getLogin());

            return "results-page";

        } catch (SessionExpiredException e) {
            model.addAttribute("isLoggedIn", false);
            return "dashboard";
        } catch (WeatherServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
}
