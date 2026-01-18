package CinemaBot.CinemaBot.repositories;

import CinemaBot.CinemaBot.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    @Modifying
    @Query(
            value = "DELETE FROM user_session WHERE create_date < NOW() - INTERVAL '60 seconds'",
            nativeQuery = true
    )    void deleteAllSession();
}
