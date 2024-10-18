package Apps.Weather.service;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;

public interface AuthService {
    Session authenticateAndCreateSession(User user);
}
