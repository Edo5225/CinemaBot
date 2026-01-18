package CinemaBot.CinemaBot.services;

import CinemaBot.CinemaBot.models.Users;
import CinemaBot.CinemaBot.models.WatchList;
import CinemaBot.CinemaBot.repositories.UserRepository;
import CinemaBot.CinemaBot.repositories.WatchlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WatchListService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;

    public WatchListService(WatchlistRepository watchlistRepository, UserRepository userRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public boolean checkFavoriteMovie(Long telegramId, Long movieTmdbId) {
        return watchlistRepository.existsByUser_TelegramIdAndMovieTmdbId(telegramId, movieTmdbId);
    }

    @Transactional
    public void addToFavorite(Long telegram_id, Long movieTmdbId, String movieTitle) {
        if (checkFavoriteMovie(telegram_id, movieTmdbId)) return;
        Users user = userRepository.findByTelegramId(telegram_id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setMovieTmdbId(movieTmdbId);
        watchList.setMovieTitle(movieTitle);
        watchlistRepository.save(watchList);
    }

    @Transactional
    public void removeFromFavorite(Long telegram_id, Long movieTmdbId) {
        watchlistRepository.deleteByUser_TelegramIdAndMovieTmdbId(telegram_id, movieTmdbId);
    }

    @Transactional
    public List<WatchList> findAllFavoriteMovies(Long telegram_id){
        return watchlistRepository.findAllByUser_TelegramId(telegram_id);
    }
}
