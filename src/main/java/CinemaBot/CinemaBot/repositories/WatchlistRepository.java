package CinemaBot.CinemaBot.repositories;

import CinemaBot.CinemaBot.models.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, Long> {
    boolean existsByUser_TelegramIdAndMovieTmdbId(Long telegramId, Long movieTmdbId);

    List<WatchList> findAllByUser_TelegramId(Long telegramId);

    void deleteByUser_TelegramIdAndMovieTmdbId(Long telegramId, Long movieTmdbId);
}