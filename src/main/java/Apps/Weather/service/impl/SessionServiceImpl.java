package Apps.Weather.service.impl;

import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session createForUser(User user) {
        // Check if a session already exists for the user
        Optional<Session> existingSession = sessionRepository.findByUser(user);

        // Delete the old session
        existingSession.ifPresent(session -> sessionRepository.delete(session));

        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusHours(2)));
        return sessionRepository.save(session);
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public Optional<Session> findByUser(User user) {
        return sessionRepository.findByUser(user);
    }

    @Transactional @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Scheduled(fixedRate = 3600000) @Override
    public void cleanExpiredSessions() {
        sessionRepository.deleteAllByExpiresAtBefore(Timestamp.valueOf(LocalDateTime.now()));
    }

    private SessionDto mapSessionToSessionDto(Session session) {
        SessionDto sessionDto = SessionDto.builder()
                .id(String.valueOf(session.getId()))
                .userId(session.getUser().getId())
                .expiresAt(session.getExpiresAt())
                .build();
        return sessionDto;
    }

}
