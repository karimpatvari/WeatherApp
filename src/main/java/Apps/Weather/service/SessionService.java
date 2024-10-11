package Apps.Weather.service;

import Apps.Weather.dto.SessionDto;
import Apps.Weather.models.Session;

import java.util.List;

public interface SessionService {
    List<SessionDto> findAllSessions();
}
