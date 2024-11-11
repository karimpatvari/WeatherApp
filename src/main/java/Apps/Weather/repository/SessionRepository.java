package Apps.Weather.repository;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUser(User user);

    Optional<Session> findById(UUID sessionId);

    @Transactional
    void deleteAllByExpiresAtBefore(Timestamp time);
}
