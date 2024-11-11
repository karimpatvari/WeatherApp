package Apps.Weather.service;

import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    Session createForUser(User user);

    Optional<Session> findById(UUID sessionId);

    Optional<Session> findByUser(User user);

    void delete(Session session);

    void cleanExpiredSessions();
}
