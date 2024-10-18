package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.SessionNotFoundException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
    public List<SessionDto> findAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream().map((session) -> mapSessionToSessionDto(session)).collect(Collectors.toList());
    }

    @Override
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public Session createSessionForUser(User foundUser) {
        Session session = new Session();
        session.setUser(foundUser);
        session.setSessionTime(Timestamp.valueOf(LocalDateTime.now().plusHours(2)));
        return sessionRepository.save(session);
    }

    @Override
    public User findUserBySessionId(String sessionId) throws UserNotFoundException, SessionNotFoundException{

        Session byId = sessionRepository.findById(UUID.fromString(sessionId));

        if (byId == null) {
            throw new SessionNotFoundException();
        }else{
            if (byId.getUser() == null){
                throw new UserNotFoundException();
            }else {
                return byId.getUser();
            }
        }
    }

    @Override
    public Session findSessionByUser(User foundUser) {
        return sessionRepository.findByUser(foundUser);
    }

    @Transactional
    @Override
    public void deleteById(UUID uuid) {
        sessionRepository.deleteById(uuid);
    }

    private SessionDto mapSessionToSessionDto(Session session) {
        SessionDto sessionDto = SessionDto.builder()
                .id(String.valueOf(session.getId()))
                .userId(session.getUser().getId())
                .sessionTime(session.getSessionTime())
                .build();
        return sessionDto;
    }

}
