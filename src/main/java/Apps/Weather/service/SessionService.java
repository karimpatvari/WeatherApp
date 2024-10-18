package Apps.Weather.service;

import Apps.Weather.customExceptions.SessionNotFoundException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import jakarta.servlet.http.Cookie;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    List<SessionDto> findAllSessions();

    Session save(Session session);

    Session createSessionForUser(User foundUser);

    User findUserBySessionId(String sessionId) throws UserNotFoundException, SessionNotFoundException;

    Session findSessionByUser(User foundUser);

    void deleteById(UUID uuid);
}
