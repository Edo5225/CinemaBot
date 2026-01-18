package CinemaBot.CinemaBot.services;

import CinemaBot.CinemaBot.models.MovieView;
import CinemaBot.CinemaBot.models.UserSession;
import CinemaBot.CinemaBot.repositories.UserSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;

    public UserSessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional
    public void saveSession(long chatId, String query, MovieView movieView) {
        var session = userSessionRepository.findById(chatId).orElse(new UserSession());
        session.setChatId(chatId);
        session.setLastQuery(query);
        if (movieView != null){
            session.setLastMovieId(movieView.getId());
            session.setLastMovieTitle(movieView.getTitle());
        }
        userSessionRepository.save(session);
    }
    @Transactional
    public UserSession getSession(long chatId) {
        return userSessionRepository.findById(chatId).orElse(null);
    }

    @Transactional(readOnly = true)
    public String getLastQuery(Long chatId) {
        return userSessionRepository.findById(chatId).map(UserSession::getLastQuery).orElse(null);
    }
}
