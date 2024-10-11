package Apps.Weather.service.impl;

import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {}

    @Override
    public List<SessionDto> findAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream().map((session) -> mapSessionToSessionDto(session)).collect(Collectors.toList());
    }

    private SessionDto mapSessionToSessionDto(Session session) {
        SessionDto sessionDto = SessionDto.builder()
                .id(session.getId())
                .userId(session.getUser().getId())
                .sessionTime(session.getSessionTime())
                .build();
        return sessionDto;
    }

}
