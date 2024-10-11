package Apps.Weather.repository;

import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
