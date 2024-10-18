package Apps.Weather.service.impl;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public Session authenticateAndCreateSession(User user) {

    }
}
